package com.justdavis.karl.usgs.water;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Element;

import net.opengis.waterml._2.CollectionType;
import net.opengis.waterml._2.MeasurementTimeseriesType;
import net.opengis.waterml._2.MeasurementTimeseriesType.Point;
import net.opengis.waterml._2.ObjectFactory;

/**
 * A client for interacting with the USGS'
 * <a href="http://waterservices.usgs.gov/rest/IV-Service.html">USGS
 * Instantaneous Values Web Service</a>.
 */
public final class InstantValuesClient {
	public String getMostRecentWaterTemperature(SiteNumber siteNumber) {
		ClientConfig config = new ClientConfig();
		config.register(WaterMlJaxbProvider.class);
		Client client = ClientBuilder.newClient(config);
		Builder requestBuilder = client.target("http://waterservices.usgs.gov/nwis/iv/")
				.queryParam("format", "waterml,2.0").queryParam("parameterCd", "00010")
				.queryParam("site", String.format("%s:%s", siteNumber.getAgencyCode(), siteNumber.getSiteNumber()))
				.request(MediaType.TEXT_XML_TYPE);

		Response response = requestBuilder.get();
		if (Status.Family.familyOf(response.getStatus()) != Status.Family.SUCCESSFUL)
			throw new RuntimeException(response.getStatusInfo().getReasonPhrase());

		// Pull back the result, unmarshalling it (via JAXB).
		CollectionType wmlCollection = response.readEntity(CollectionType.class);

		/*
		 * Unfortunately, the XJC-produced JAXB mapping is a bit limited here,
		 * as the schema says that `om:OM_Observation/om:result` is of type
		 * 'any'. To cope, we drill into the results as far as we can, and then
		 * unmarshall the result data itself.
		 */
		Element resultElement = (Element) wmlCollection.getObservationMember().get(0).getOMObservation().getResult();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			MeasurementTimeseriesType measurement = jaxbUnmarshaller
					.unmarshal(resultElement.getFirstChild(), MeasurementTimeseriesType.class).getValue();
			Point resultDataPoint = measurement.getPoint().get(1);
			return "" + resultDataPoint.getMeasurementTVP().getValue().getValue().getValue();
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}
	}
}

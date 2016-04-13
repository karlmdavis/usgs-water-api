package com.justdavis.karl.usgs.water;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.opengis.waterml._2.ObjectFactory;

@Provider
public final class WaterMlJaxbProvider implements ContextResolver<JAXBContext> {
	public WaterMlJaxbProvider() {
		System.out.println("context created");
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		System.err.println("context called for: " + type);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			return jaxbContext;
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}
	}
}
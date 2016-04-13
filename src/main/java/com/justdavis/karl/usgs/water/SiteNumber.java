package com.justdavis.karl.usgs.water;

/**
 * Enumerates some specific USGS water station IDs.
 */
public enum SiteNumber {
	/**
	 * PATUXENT RIVER BELOW BRIGHTON DAM NEAR BRIGHTON,MD
	 */
	PATUXENT_BRIGHTON_DAM_BELOW("USGS", "01591610");

	private final String agencyCode;
	private final String siteNumber;

	/**
	 * Enum constant constructor.
	 * 
	 * @param agencyCode
	 *            the value to use for {@link #getAgencyCode()}
	 * @param siteNumber
	 *            the value to use for {@link #getSiteNumber()}
	 */
	private SiteNumber(String agencyCode, String siteNumber) {
		this.agencyCode = agencyCode;
		this.siteNumber = siteNumber;
	}

	/**
	 * @return the agency code for this {@link SiteNumber}, or <code>null</code>
	 *         if it's unknown or not applicable
	 */
	public String getAgencyCode() {
		return agencyCode;
	}

	/**
	 * @return the actual site number string for this {@link SiteNumber}
	 *         (represented as a string, as it will often contain leading
	 *         zeroes)
	 */
	public String getSiteNumber() {
		return siteNumber;
	}
}

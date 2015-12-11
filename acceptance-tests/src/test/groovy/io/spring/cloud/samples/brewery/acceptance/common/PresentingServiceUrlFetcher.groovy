package io.spring.cloud.samples.brewery.acceptance.common

class PresentingServiceUrlFetcher {

	private static final String LOCAL_MODE_PROP = 'LOCAL_MODE'
	private static final String LOCAL_MODE_URL_PROP = 'LOCAL_MODE_URL'
	private static final String SERVICE_DISCOVERY_URL = "http://presenting"

	String presentingServiceUrl() {
		if (hasProp(LOCAL_MODE_PROP)) {
			return "http://localhost:9991"
		} else if (hasProp(LOCAL_MODE_URL_PROP)) {
			return getProp(LOCAL_MODE_URL_PROP)
		}
		return SERVICE_DISCOVERY_URL
	}

	private boolean hasProp(String propName) {
		return System.getenv().containsKey(propName) ?: System.getProperties().containsKey(propName)
	}

	private String getProp(String propName) {
		return System.getenv(propName) ?: System.getProperty(propName)
	}
}

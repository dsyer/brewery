package io.spring.cloud.samples.brewery.acceptance.common.tech

class ServiceUrlFetcher {

	private static final String LOCAL_MODE_PROP = 'LOCAL'
	private static final String LOCAL_MODE_URL_PROP = 'LOCAL_URL'
	private static final String LOCALHOST = "http://localhost"

	String presentingServiceUrl() {
		String rootUrl = getRootUrlForRibbon()
		if (hasProp(LOCAL_MODE_PROP) || hasProp(LOCAL_MODE_URL_PROP)) {
			return "$rootUrl:9991"
		}
		return "$rootUrl/presenting"
	}

	private String getRootUrlForRibbon() {
		if (hasProp(LOCAL_MODE_PROP)) {
			return LOCALHOST
		} else if (hasProp(LOCAL_MODE_URL_PROP)) {
			return getProp(LOCAL_MODE_URL_PROP)
		}
		return "http://"
	}

	private boolean hasProp(String propName) {
		return System.getenv().containsKey(propName) ?: System.getProperties().containsKey(propName)
	}

	private String getProp(String propName) {
		return System.getenv(propName) ?: System.getProperty(propName)
	}
}

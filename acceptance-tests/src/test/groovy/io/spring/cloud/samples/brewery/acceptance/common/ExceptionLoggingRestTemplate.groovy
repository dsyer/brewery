package io.spring.cloud.samples.brewery.acceptance.common

import org.springframework.web.client.RestTemplate

class ExceptionLoggingRestTemplate extends RestTemplate {

	ExceptionLoggingRestTemplate() {
		errorHandler = new ExceptionLoggingErrorHandler()
	}
}

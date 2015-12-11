package io.spring.cloud.samples.brewery.acceptance.common

class TestConditions {
	public static final Closure<Boolean> WHAT_TO_TEST = { WhatToTest whatToTest ->
		return getAndLogWhatToTestSystemProp() == whatToTest.name()
	}

	private static String getAndLogWhatToTestSystemProp() {
		String whatToTestProp = System.getProperty(WhatToTest.WHAT_TO_TEST)
		println "What to test system prop equals [$whatToTestProp]"
		return whatToTestProp
	}

	public static final Closure<Boolean> SERVICE_DISCOVERY = {
		String whatToTestProp = getAndLogWhatToTestSystemProp()
		return [WhatToTest.CONSUL, WhatToTest.EUREKA, WhatToTest.ZOOKEEPER].any {
			it.toString().compareToIgnoreCase(whatToTestProp)
		}
	}
}

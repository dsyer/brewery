/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.cloud.samples.brewery.acceptance

import groovy.json.JsonSlurper
import io.spring.cloud.samples.brewery.acceptance.model.CommunicationType
import io.spring.cloud.samples.brewery.acceptance.model.IngredientType
import io.spring.cloud.samples.brewery.acceptance.model.Order
import io.spring.cloud.samples.brewery.acceptance.model.ProcessState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.http.*
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.JdkIdGenerator
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

@ContextConfiguration(classes = TestConfiguration, loader = SpringApplicationContextLoader)
@WebIntegrationTest(randomPort = true)
class BreweryAcceptanceSpec extends Specification implements PollingUtils {

	private static final Long TWO_SECONDS = 2L

	@Autowired @LoadBalanced RestTemplate restTemplate
	PollingConditions conditions = new PollingConditions()

	@Unroll
	def 'should successfully brew the beer via [#communicationType] and processId [#referenceProcessId]'() {
		given:
		    RequestEntity requestEntity = an_order_for_all_ingredients_with_process_id(referenceProcessId, communicationType)
		when: 'the presenting service has been called with such an order'
			presenting_service_has_been_called(requestEntity)
		then: 'eventually beer for that process id will be brewed'
			conditions.within TWO_SECONDS, willPass {
				ResponseEntity<String> process = beer_has_been_brewed_for_process_id(referenceProcessId)
				assert process.statusCode == HttpStatus.OK
				assert stateFromJson(process) == ProcessState.DONE.name()
			}
		where:
		    // will add FEIGN once REST_TEMPLATE tests stabilize
			communicationType << [CommunicationType.REST_TEMPLATE]
			referenceProcessId = new JdkIdGenerator().generateId().toString()
	}

	private String stateFromJson(ResponseEntity<String> process) {
		return new JsonSlurper().parseText(process.body).state.toUpperCase()
	}

	private RequestEntity an_order_for_all_ingredients_with_process_id(String processId, CommunicationType communicationType) {
		HttpHeaders headers = new HttpHeaders()
		headers.add("PROCESS-ID", processId)
		headers.add("TEST-COMMUNICATION-TYPE", communicationType.name())
		URI uri = URI.create("http://presenting/present/order")
		return new RequestEntity<>(allIngredients(), headers, HttpMethod.POST, uri)
	}

	private ResponseEntity<String> presenting_service_has_been_called(RequestEntity requestEntity) {
		return restTemplate.exchange(requestEntity, String)
	}

	private Order allIngredients() {
		return new Order(items: IngredientType.values())
	}

	private ResponseEntity<String> beer_has_been_brewed_for_process_id(String processId) {
		URI uri = URI.create("http://presenting/feed/process/$processId")
		HttpHeaders headers = new HttpHeaders()
		return restTemplate.exchange(new RequestEntity<>(headers, HttpMethod.GET, uri), String)
	}

}
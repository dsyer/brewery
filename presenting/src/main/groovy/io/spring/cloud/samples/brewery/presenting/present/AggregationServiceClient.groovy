package io.spring.cloud.samples.brewery.presenting.present

import io.spring.cloud.samples.brewery.presenting.config.Versions
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import static io.spring.cloud.samples.brewery.common.TestConfigurationHolder.TEST_COMMUNICATION_TYPE_HEADER_NAME

@FeignClient("aggregating")
@RequestMapping(value = "/ingredients",
        consumes = Versions.AGGREGATING_CONTENT_TYPE_V1, produces = MediaType.APPLICATION_JSON_VALUE)
interface AggregationServiceClient {

    @RequestMapping(method = RequestMethod.POST)
    String getIngredients(String body,
                          @RequestHeader("PROCESS-ID") String processId,
                          @RequestHeader(TEST_COMMUNICATION_TYPE_HEADER_NAME) String testCommunicationType)
}
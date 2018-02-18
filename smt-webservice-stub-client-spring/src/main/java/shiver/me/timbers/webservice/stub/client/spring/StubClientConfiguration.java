/*
 *    Copyright 2018 Karl Bennett
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package shiver.me.timbers.webservice.stub.client.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import shiver.me.timbers.webservice.stub.client.GivenStub;
import shiver.me.timbers.webservice.stub.client.ThenStub;

import javax.ws.rs.client.ClientBuilder;

/**
 * @author Karl Bennett
 */
@Configuration
@ComponentScan
public class StubClientConfiguration {

    @Bean
    public SpringBootTestUrl stubberUrl(
        @Value("${soap.stubber.path:stubber}") String path,
        @Value("${soap.stubber.url:}") String url
    ) {
        return new SpringBootTestUrl(path, url);
    }

    @Bean
    public SpringBootTestUrl stubUrl(
        @Value("${soap.stub.path:stub}") String path,
        @Value("${soap.stub.url:}") String url
    ) {
        return new SpringBootTestUrl(path, url);
    }

    @Bean
    public SpringBootTestUrl verifierUrl(
        @Value("${soap.verifying.path:verifier}") String path,
        @Value("${soap.verifying.url:}") String url
    ) {
        return new SpringBootTestUrl(path, url);
    }

    @Bean
    public GivenStub givenSoapStub(@Qualifier("stubberUrl") SpringBootTestUrl stubbingUrl) {
        return new GivenStub(stubbingUrl, ClientBuilder.newClient());
    }

    @Bean
    public ThenStub thenSoapStub(@Qualifier("verifierUrl") SpringBootTestUrl stubbingUrl) {
        return new ThenStub(stubbingUrl, ClientBuilder.newClient());
    }
}

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

package shiver.me.timbers.webservice.stub.client.soap.example;

import org.junit.Test;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import java.util.List;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class ExampleServiceFactoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_example_SOAP_service() {

        final String namespace = someString();
        final SoapHeaderHandler handler = mock(SoapHeaderHandler.class);
        final ServiceFactory serviceFactory = mock(ServiceFactory.class);
        final String endpointUrl = someString();

        final Service service = mock(Service.class);
        final TestExampleService expected = mock(TestExampleService.class);
        final Map<String, Object> context = mock(Map.class);
        final Binding binding = mock(Binding.class);
        final List<Handler> handlerChain = mock(List.class);

        // Given
        given(serviceFactory.create("META-INF/wsdl/ExampleService.wsdl", namespace, ExampleService.class))
            .willReturn(service);
        given(service.getPort(ExampleService.class)).willReturn(expected);
        given(expected.getRequestContext()).willReturn(context);
        given(expected.getBinding()).willReturn(binding);
        given(binding.getHandlerChain()).willReturn(handlerChain);

        // When
        final ExampleService actual = new ExampleServiceFactory(namespace, handler, serviceFactory).create(endpointUrl);

        // Then
        then(context).should().put(ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        then(handlerChain).should().add(handler);
        then(binding).should().setHandlerChain(handlerChain);
        assertThat(actual, is(expected));
    }

    private interface TestExampleService extends ExampleService, BindingProvider {
    }
}
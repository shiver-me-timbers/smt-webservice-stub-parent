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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class DelegateExampleServiceTest {

    private ExampleService service;
    private DelegateExampleService delegateService;
    private SoapHeaderHandler handler;

    @Before
    public void setUp() {
        service = mock(ExampleService.class);
        handler = mock(SoapHeaderHandler.class);
        delegateService = new DelegateExampleService(handler, service);
    }

    @Test
    public void Instantiation_for_coverage() {
        new DelegateExampleService();
    }

    @Test
    public void Can_delegate_to_a_service() {

        final Argument1 argument1 = mock(Argument1.class);
        final Argument2 argument2 = mock(Argument2.class);

        final ExampleResponse expected = mock(ExampleResponse.class);

        // Given
        given(service.exampleMethod(argument1, argument2)).willReturn(expected);

        // When
        final ExampleResponse actual = delegateService.exampleMethod(argument1, argument2);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_add_soap_headers() {

        // Given
        final SoapHeader header1 = mock(SoapHeader.class);
        final SoapHeader header2 = mock(SoapHeader.class);
        final SoapHeader header3 = mock(SoapHeader.class);

        // When
        delegateService.addHeaders(header1, header2, header3);

        // Then
        then(handler).should().addSoapHeaders(header1, header2, header3);
    }

    @Test
    public void Can_clear_soap_headers() {

        // When
        delegateService.clearHeaders();

        // Then
        then(handler).should().clearSoapHeaders();
    }
}
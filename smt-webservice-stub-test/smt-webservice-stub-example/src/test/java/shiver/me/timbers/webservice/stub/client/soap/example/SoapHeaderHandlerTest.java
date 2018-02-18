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

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomStrings.someString;

/**
 * @author Karl Bennett
 */
public class SoapHeaderHandlerTest {

    private Set<SoapHeader> headers;
    private String namespace;
    private SoapHeaderHandler handler;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        headers = mock(Set.class);
        namespace = someString();
        handler = new SoapHeaderHandler(namespace, headers);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_headers() {

        // Given
        final SoapHeader header1 = mock(SoapHeader.class);
        final SoapHeader header2 = mock(SoapHeader.class);
        final SoapHeader header3 = mock(SoapHeader.class);

        // When
        handler.addSoapHeaders(header1, header2, header3);

        // Then
        then(headers).should().add(header1);
        then(headers).should().add(header2);
        then(headers).should().add(header3);
    }

    @Test
    public void Can_clear_all_the_headers() {

        // When
        handler.clearSoapHeaders();

        // Then
        then(headers).should().clear();
    }

    @Test
    public void Cannot_get_any_headers() {

        // When
        final Set<QName> actual = handler.getHeaders();

        // Then
        assertThat(actual, empty());
    }

    @Test
    public void Can_add_SOAP_headers_to_the_message() throws SOAPException {

        final SoapHeader header1 = mock(SoapHeader.class);
        final SoapHeader header2 = mock(SoapHeader.class);
        final SoapHeader header3 = mock(SoapHeader.class);
        final HashSet<SoapHeader> headers = new HashSet<>(asList(header1, header2, header3));
        final SOAPMessageContext context = mock(SOAPMessageContext.class);

        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();
        final String value1 = someString();
        final String value2 = someString();
        final String value3 = someString();
        final SOAPMessage message = mock(SOAPMessage.class);
        final SOAPHeader soapHeader = mock(SOAPHeader.class);
        final SOAPHeaderElement element1 = mock(SOAPHeaderElement.class);
        final SOAPHeaderElement element2 = mock(SOAPHeaderElement.class);
        final SOAPHeaderElement element3 = mock(SOAPHeaderElement.class);

        // Given
        given(header1.getKey()).willReturn(name1);
        given(header1.getValue()).willReturn(value1);
        given(header2.getKey()).willReturn(name2);
        given(header2.getValue()).willReturn(value2);
        given(header3.getKey()).willReturn(name3);
        given(header3.getValue()).willReturn(value3);
        given(context.getMessage()).willReturn(message);
        given(message.getSOAPHeader()).willReturn(soapHeader);
        given(soapHeader.addHeaderElement(new QName(namespace, name1))).willReturn(element1);
        given(soapHeader.addHeaderElement(new QName(namespace, name2))).willReturn(element2);
        given(soapHeader.addHeaderElement(new QName(namespace, name3))).willReturn(element3);

        // When
        final boolean actual = new SoapHeaderHandler(namespace, headers).handleMessage(context);

        // Then
        then(element1).should().addTextNode(value1);
        then(element2).should().addTextNode(value2);
        then(element3).should().addTextNode(value3);
        assertThat(actual, is(true));
    }

    @Test
    public void Can_fail_to_add_SOAP_headers_to_the_message() throws SOAPException {

        final SOAPMessageContext context = mock(SOAPMessageContext.class);

        final SOAPMessage message = mock(SOAPMessage.class);

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(context.getMessage()).willReturn(message);
        given(message.getSOAPHeader()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> handler.handleMessage(context));

        // Then
        assertThat(actual, instanceOf(SoapHeaderException.class));
        assertThat(actual.getMessage(), equalTo("Failed to add SOAP headers."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Will_do_nothing_if_the_headers_are_empty() {

        // Given
        final SOAPMessageContext context = mock(SOAPMessageContext.class);

        // When
        final boolean actual = new SoapHeaderHandler(namespace, emptySet()).handleMessage(context);

        // Then
        verifyZeroInteractions(context);
        assertThat(actual, is(true));
    }

    @Test
    public void Can_handle_a_fault() {

        // When
        final boolean actual = handler.handleFault(mock(SOAPMessageContext.class));

        // Then
        assertThat(actual, is(true));
    }

    @Test
    public void Closing_the_handler_does_nothing() {

        // Given
        final MessageContext context = mock(MessageContext.class);

        // When
        handler.close(context);

        // Then
        verifyZeroInteractions(headers, context);
    }
}
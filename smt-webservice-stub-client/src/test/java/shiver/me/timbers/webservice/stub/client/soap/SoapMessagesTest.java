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

package shiver.me.timbers.webservice.stub.client.soap;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Document;
import shiver.me.timbers.webservice.stub.client.soap.api.SoapHeader;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class SoapMessagesTest {

    private SoapMessageFactory messageFactory;
    private SoapHeaders soapHeaders;
    private SoapMessages messages;

    @Before
    public void setUp() {
        messageFactory = mock(SoapMessageFactory.class);
        soapHeaders = mock(SoapHeaders.class);
        messages = new SoapMessages(messageFactory, soapHeaders);
    }

    @Test
    public void Can_wrap_a_document_in_a_SOAP_envelope() throws SOAPException {

        final Document document = mock(Document.class);

        final SOAPMessage message = mock(SOAPMessage.class);
        final SOAPHeader header = mock(SOAPHeader.class);
        final SOAPBody body = mock(SOAPBody.class);

        // Given
        given(messageFactory.createMessage()).willReturn(message);
        given(message.getSOAPHeader()).willReturn(header);
        given(message.getSOAPBody()).willReturn(body);

        // When
        final SOAPMessage actual = messages.wrapInSoapEnvelope(document, emptySet());

        // Then
        then(header).should().detachNode();
        then(body).should().addDocument(document);
        verifyZeroInteractions(soapHeaders);
        assertThat(actual, is(message));
    }

    @Test
    public void Can_wrap_a_document_in_a_SOAP_envelope_with_headers() throws SOAPException {

        final Document document = mock(Document.class);
        final SoapHeader header1 = mock(SoapHeader.class);
        final SoapHeader header2 = mock(SoapHeader.class);
        final SoapHeader header3 = mock(SoapHeader.class);

        final SOAPMessage message = mock(SOAPMessage.class);
        final SOAPHeader header = mock(SOAPHeader.class);
        final SOAPBody body = mock(SOAPBody.class);
        final String namespace = someString();

        // Given
        given(messageFactory.createMessage()).willReturn(message);
        given(message.getSOAPHeader()).willReturn(header);
        given(message.getSOAPBody()).willReturn(body);
        given(soapHeaders.extractNamespace(document)).willReturn(namespace);

        // When
        final SOAPMessage actual = messages.wrapInSoapEnvelope(document, new HashSet<>(asList(header1, header2, header3)));

        // Then
        then(soapHeaders).should().addHeader(header, namespace, header1);
        then(soapHeaders).should().addHeader(header, namespace, header2);
        then(soapHeaders).should().addHeader(header, namespace, header3);
        then(body).should().addDocument(document);
        assertThat(actual, is(message));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_wrap_a_document_in_a_SOAP_envelope() throws SOAPException {

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(messageFactory.createMessage()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> messages.wrapInSoapEnvelope(mock(Document.class), mock(Set.class)));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to wrap the document in a SOAP envelope."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_convert_a_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final String expected = someString();

        // Given
        willAnswer(new Write(0, expected)).given(message).writeTo(any(OutputStream.class));

        // When
        final String actual = messages.toXmlString(message);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_write_the_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final IOException exception = mock(IOException.class);

        // Given
        willThrow(exception).given(message).writeTo(any(OutputStream.class));

        // When
        final Throwable actual = catchThrowable(() -> messages.toXmlString(message));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to convert the SOAP message to a string."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_fail_to_convert_the_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final SOAPException exception = mock(SOAPException.class);

        // Given
        willThrow(exception).given(message).writeTo(any(OutputStream.class));

        // When
        final Throwable actual = catchThrowable(() -> messages.toXmlString(message));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to convert the SOAP message to a string."));
        assertThat(actual.getCause(), is(exception));
    }

    private static class Write implements Answer<Void> {

        private final int index;
        private final String string;

        private Write(int index, String string) {
            this.index = index;
            this.string = string;
        }

        @Override
        public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            IOUtils.write(string, invocationOnMock.getArgument(index), Charset.forName("UTF-8"));
            return null;
        }
    }
}
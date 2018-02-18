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

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import shiver.me.timbers.webservice.stub.client.soap.api.SoapHeader;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class SoapHeadersTest {

    private SoapHeaders headers;

    @Before
    public void setUp() {
        headers = new SoapHeaders();
    }

    @Test
    public void Can_extract_a_namespace_from_a_document() {

        final Document document = mock(Document.class);

        final Element element = mock(Element.class);

        final String expected = someString();

        // Given
        given(document.getDocumentElement()).willReturn(element);
        given(element.getNamespaceURI()).willReturn(expected);

        // When
        final String actual = headers.extractNamespace(document);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_add_a_SOAP_header() throws SOAPException {

        final SOAPHeader soapHeader = mock(SOAPHeader.class);
        final String namespace = someString();
        final SoapHeader header = mock(SoapHeader.class);

        final String name = someString();
        final String value = someString();
        final SOAPHeaderElement headerElement = mock(SOAPHeaderElement.class);

        // Given
        given(header.getKey()).willReturn(name);
        given(header.getValue()).willReturn(value);
        given(soapHeader.addHeaderElement(new QName(namespace, name))).willReturn(headerElement);

        // When
        headers.addHeader(soapHeader, namespace, header);

        // Then
        then(headerElement).should().addTextNode(value);
    }

    @Test
    public void Can_fail_to_add_a_SOAP_header() throws SOAPException {

        final SOAPHeader soapHeader = mock(SOAPHeader.class);
        final String namespace = someString();
        final SoapHeader header = mock(SoapHeader.class);

        final String name = someString();
        final String value = someString();

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(header.getKey()).willReturn(name);
        given(header.getValue()).willReturn(value);
        given(soapHeader.addHeaderElement(new QName(namespace, name))).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> headers.addHeader(soapHeader, namespace, header));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to add SOAP header (%s:%s).", name, value)));
        assertThat(actual.getCause(), is(exception));
    }
}
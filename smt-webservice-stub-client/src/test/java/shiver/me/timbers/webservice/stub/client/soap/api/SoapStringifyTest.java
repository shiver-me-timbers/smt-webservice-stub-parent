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

package shiver.me.timbers.webservice.stub.client.soap.api;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.client.soap.Soaps;

import javax.xml.soap.SOAPMessage;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class SoapStringifyTest {

    private Soaps soaps;
    private SoapStringify stringify;

    @Before
    public void setUp() {
        soaps = mock(Soaps.class);
        stringify = new SoapStringify(soaps);
    }

    @Test
    public void Can_convert_a_SOAP_object_to_an_XML_string() {

        final Object object = someThing();

        final SOAPMessage message = mock(SOAPMessage.class);
        final String expected = someString();

        // Given
        given(soaps.marshalToSOAPMessage(object, emptySet())).willReturn(message);
        given(soaps.toXmlString(message)).willReturn(expected);

        // When
        final String actual = stringify.toString(object);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_convert_a_SOAP_object_to_an_XML_string_with_SOAP_headers() {

        final Object object = someThing();

        final SoapHeader header1 = mock(SoapHeader.class);
        final SoapHeader header2 = mock(SoapHeader.class);
        final SoapHeader header3 = mock(SoapHeader.class);
        final SOAPMessage message = mock(SOAPMessage.class);

        final String expected = someString();

        // Given
        stringify.addSoapHeaders(header1, header2, header3);
        given(soaps.marshalToSOAPMessage(object, new HashSet<>(asList(header1, header2, header3)))).willReturn(message);
        given(soaps.toXmlString(message)).willReturn(expected);

        // When
        final String actual = stringify.toString(object);

        // Then
        assertThat(actual, is(expected));
    }
}
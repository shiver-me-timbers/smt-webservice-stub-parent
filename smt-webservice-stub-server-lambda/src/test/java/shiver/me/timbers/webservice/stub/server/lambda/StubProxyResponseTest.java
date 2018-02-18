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

package shiver.me.timbers.webservice.stub.server.lambda;

import org.hamcrest.Matcher;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import static java.lang.String.format;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;

public class StubProxyResponseTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_stub_proxy_response() {

        final StringStubResponse response = mock(StringStubResponse.class);

        final int status = someInteger();
        final String hName1 = someString(2);
        final String hName2 = someString(3);
        final String hValue1 = someString(5);
        final String hValue2 = someString(8);
        final String hValue3 = someString(13);
        final String body = someString();

        // Given
        given(response.getStatus()).willReturn(status);
        given(response.getHeaders()).willReturn(new StubHeaders(h(hName1, hValue1), h(hName2, hValue2, hValue3)));
        given(response.getBody()).willReturn(body);

        // When
        final StubProxyResponse actual = new StubProxyResponse(response);

        // Then
        assertThat(actual.getStatusCode(), is(status));
        assertThat(actual.getHeaders(), (Matcher) allOf(
            hasEntry(hName1, hValue1),
            hasEntry(hName2, format("%s,%s", hValue2, hValue3))
        ));
        assertThat(actual.getBody(), is(body));
    }
}
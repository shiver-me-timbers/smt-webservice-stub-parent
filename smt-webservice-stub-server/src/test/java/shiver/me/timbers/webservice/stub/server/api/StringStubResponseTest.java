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

package shiver.me.timbers.webservice.stub.server.api;

import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.StubResponse;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;

public class StringStubResponseTest {

    @Test
    public void Can_create_a_default_string_response() {

        // When
        final StringStubResponse actual = new StringStubResponse();

        // Then
        assertThat(actual, hasField("status", 200));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasFieldThat("body", nullValue()));
    }

    @Test
    public void Can_create_a_string_response_with_a_body() {

        // Given
        final String body = someString();

        // When
        final StringStubResponse actual = new StringStubResponse(body);

        // Then
        assertThat(actual, hasField("status", 200));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_copy_a_string_response() {

        final int status = someInteger();
        final StubHeaders headers = mock(StubHeaders.class);
        final String body = someString();

        // Given
        final StubResponse<String, StringStubResponse> response = new StringStubResponse()
            .withStatus(status)
            .withHeaders(headers)
            .withBody(body);

        // When
        final StringStubResponse actual = response.copy();

        // Then
        assertThat(actual, not(sameInstance(response)));
        assertThat(actual, hasField("status", status));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }
}
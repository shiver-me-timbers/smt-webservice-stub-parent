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
import shiver.me.timbers.webservice.stub.api.StubQuery;
import shiver.me.timbers.webservice.stub.api.StubRequest;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;

public class StringStubRequestTest {

    @Test
    public void Can_create_a_default_GET_string_request() {

        // When
        final StringStubRequest actual = new StringStubRequest();

        // Then
        assertThat(actual, hasField("method", "GET"));
        assertThat(actual, hasField("path", ""));
        assertThat(actual, hasField("query", new StubQuery()));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasFieldThat("body", nullValue()));
    }

    @Test
    public void Can_create_a_default_POST_string_request() {

        // Given
        final String body = someString();

        // When
        final StringStubRequest actual = new StringStubRequest(body);

        // Then
        assertThat(actual, hasField("method", "POST"));
        assertThat(actual, hasField("path", ""));
        assertThat(actual, hasField("query", new StubQuery()));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasField("body", body));
    }

    @Test
    public void Can_copy_a_string_request() {

        final String method = someString();
        final String path = someString();
        final StubQuery query = mock(StubQuery.class);
        final StubHeaders headers = mock(StubHeaders.class);
        final String body = someString();

        // Given
        final StubRequest<String, StringStubRequest> request = new StringStubRequest()
            .withMethod(method)
            .withPath(path)
            .withQuery(query)
            .withHeaders(headers)
            .withBody(body);

        // When
        final StringStubRequest actual = request.copy();

        // Then
        assertThat(actual, not(sameInstance(request)));
        assertThat(actual, hasField("method", method));
        assertThat(actual, hasField("path", path));
        assertThat(actual, hasField("query", query));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }
}
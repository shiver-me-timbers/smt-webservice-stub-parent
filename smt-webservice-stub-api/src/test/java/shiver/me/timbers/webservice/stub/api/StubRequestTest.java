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

package shiver.me.timbers.webservice.stub.api;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.isABeanWithValidGettersAndSettersExcluding;
import static java.lang.String.format;
import static java.util.Map.Entry;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;
import static shiver.me.timbers.webservice.stub.api.StubQuery.q;

public class StubRequestTest {

    private Stringify stringify;
    private StubRequest<Object, StubRequest> request;

    @Before
    public void setUp() {
        stringify = mock(Stringify.class);
        request = new StubRequest<Object, StubRequest>(stringify) {
            @Override
            public StubRequest copy() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void Can_create_a_default_GET_request() {

        // Then
        assertThat(request, hasField("stringify", stringify));
        assertThat(request, hasField("method", "GET"));
        assertThat(request, hasField("path", ""));
        assertThat(request, hasField("query", new StubQuery()));
        assertThat(request, hasField("headers", new StubHeaders()));
        assertThat(request, hasFieldThat("body", nullValue()));
    }

    @Test
    public void Can_create_a_default_POST_request() {

        // Given
        final Object body = someThing();

        // When
        final StubRequest<Object, StubRequest> actual = new StubRequest<Object, StubRequest>(stringify, body) {
            @Override
            public String stringifyBody() {
                throw new UnsupportedOperationException();
            }

            @Override
            public StubRequest copy() {
                throw new UnsupportedOperationException();
            }
        };

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("method", "POST"));
        assertThat(actual, hasField("path", ""));
        assertThat(actual, hasField("query", new StubQuery()));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_custom_request() {

        // Given
        final String method = someString(3);
        final String path = someString(3);
        final StubQuery query = mock(StubQuery.class);
        final StubHeaders headers = mock(StubHeaders.class);
        final Object body = someThing();

        // When
        final StubMessage<Object, StubRequest> actual = request
            .withMethod(method)
            .withPath(path)
            .withQuery(query)
            .withHeaders(headers)
            .withBody(body);

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("method", method));
        assertThat(actual, hasField("path", path));
        assertThat(actual, hasField("query", query));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_copy_a_request() {

        final String method = someString();
        final String path = someString();
        final StubQuery query = mock(StubQuery.class);
        final StubHeaders headers = mock(StubHeaders.class);
        final Object body = someThing();

        // Given
        request
            .withMethod(method)
            .withPath(path)
            .withQuery(query)
            .withHeaders(headers)
            .withBody(body);

        // When
        final StubRequest<Object, StubRequest> actual = new StubRequest<Object, StubRequest>(request) {

            @Override
            public StubRequest copy() {
                throw new UnsupportedOperationException();
            }
        };

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("method", method));
        assertThat(actual, hasField("path", path));
        assertThat(actual, hasField("query", query));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_query_parameters() {

        // Given
        final Entry<String, List<String>> param1 = q(someString(3), someString(8));
        final Entry<String, List<String>> param2 = q(someString(5), someString(5));
        final Entry<String, List<String>> param3 = q(someString(8), someString(3));

        // When
        final StubRequest<Object, StubRequest> actual = request.withQuery(param1, param2, param3);

        // Then
        assertThat(actual, hasField("query", new StubQuery(param1, param2, param3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_query_parameters_as_a_string() {

        // Given
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final String value3 = someAlphanumericString(3);

        // When
        final StubRequest<Object, StubRequest> actual = request
            .withQuery(format("%s=%s&%s=%s&%s=%s", name1, value1, name2, value2, name3, value3));

        // Then
        assertThat(actual, hasField("query", new StubQuery(q(name1, value1), q(name2, value2), q(name3, value3))));
    }

    @Test
    public void Is_a_valid_bean() {
        assertThat(request, isABeanWithValidGettersAndSettersExcluding("contentType"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_to_string_a_stub_request() {

        // Given
        final String method = someString(3);
        final String path = someString(3);
        final StubQuery query = mock(StubQuery.class);
        final StubHeaders headers = new StubHeaders();
        final Object body = someThing();

        // When
        final String actual = request
            .withMethod(method)
            .withPath(path)
            .withQuery(query)
            .withHeaders(headers)
            .withBody(body).toString();

        // Then
        assertThat(actual, containsString(method));
        assertThat(actual, containsString(path));
        assertThat(actual, containsString(query.toString()));
        assertThat(actual, containsString(headers.toString()));
        assertThat(actual, containsString(body.toString()));
    }
}
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

import static com.google.code.beanmatchers.BeanMatchers.isABeanWithValidGettersAndSettersExcluding;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;

public class StubResponseTest {

    private Stringify stringify;
    private StubResponse<Object, StubResponse> response;

    @Before
    public void setUp() {
        stringify = mock(Stringify.class);
        response = new StubResponse<Object, StubResponse>(stringify) {
            @Override
            public StubResponse copy() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void Can_create_a_default_response() {

        // Then
        assertThat(response, hasField("stringify", stringify));
        assertThat(response, hasField("status", 200));
        assertThat(response, hasField("headers", new StubHeaders()));
        assertThat(response, hasFieldThat("body", nullValue()));
    }

    @Test
    public void Can_create_a_response_with_a_body() {

        // Given
        final Object body = someThing();

        // When
        final StubResponse<Object, StubResponse> actual = new StubResponse<Object, StubResponse>(stringify, body) {
            @Override
            public StubResponse copy() {
                throw new UnsupportedOperationException();
            }
        };

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("status", 200));
        assertThat(actual, hasField("headers", new StubHeaders()));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_custom_response() {

        // Given
        final int status = someInteger();
        final StubHeaders headers = mock(StubHeaders.class);
        final Object body = someThing();

        // When
        final StubMessage<Object, StubResponse> actual = response
            .withStatus(status)
            .withHeaders(headers)
            .withBody(body);

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("status", status));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_copy_a_response() {

        final int status = someInteger();
        final StubHeaders headers = mock(StubHeaders.class);
        final Object body = someThing();

        // Given
        response
            .withStatus(status)
            .withHeaders(headers)
            .withBody(body);

        // When
        final StubResponse<Object, StubResponse> actual = new StubResponse<Object, StubResponse>(response) {

            @Override
            public StubResponse copy() {
                throw new UnsupportedOperationException();
            }
        };

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("status", status));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    public void Is_a_valid_bean() {
        assertThat(response, isABeanWithValidGettersAndSettersExcluding("contentType"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_to_string_a_stub_response() {

        // Given
        final int status = someInteger();
        final StubHeaders headers = new StubHeaders();
        final Object body = someThing();

        // When
        final String actual = response
            .withStatus(status)
            .withHeaders(headers)
            .withBody(body).toString();

        // Then
        assertThat(actual, containsString(String.valueOf(status)));
        assertThat(actual, containsString(headers.toString()));
        assertThat(actual, containsString(body.toString()));
    }
}
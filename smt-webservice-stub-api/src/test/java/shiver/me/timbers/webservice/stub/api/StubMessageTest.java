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

import java.util.HashSet;
import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.isABeanWithValidGettersAndSettersExcluding;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Map.Entry;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.webservice.stub.api.StubContentType.CONTENT_TYPE;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;

public class StubMessageTest {

    private Stringify stringify;
    private StubHeaders headers;
    private Object body;
    private StubMessage<Object, StubMessage> message;

    @Before
    public void setUp() {
        stringify = mock(Stringify.class);
        headers = new StubHeaders();
        body = someThing();
        message = new StubMessage<Object, StubMessage>(stringify, headers, body) {
            @Override
            public StubMessage copy() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void Can_create_a_stub_message() {

        // Then
        assertThat(message, hasField("stringify", this.stringify));
        assertThat(message, hasField("headers", headers));
        assertThat(message, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_update_a_stub_message() {

        // Given
        final StubHeaders headers = mock(StubHeaders.class);
        final Object body = someThing();

        // When
        final StubMessage<Object, StubMessage> actual = message
            .withHeaders(headers)
            .withBody(body);

        // Then
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_copy_a_message() {

        // When
        final StubMessage<Object, StubMessage> actual = new StubMessage<Object, StubMessage>(message) {
            @Override
            public StubMessage copy() {
                throw new UnsupportedOperationException();
            }
        };

        // Then
        assertThat(actual, hasField("stringify", stringify));
        assertThat(actual, hasField("headers", headers));
        assertThat(actual, hasField("body", body));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_stringify_a_message_body() {

        final Object body = someThing();
        final StubMessage<Object, StubMessage> request = message.withBody(body);

        final String expected = someString();

        // Given
        given(stringify.toString(body)).willReturn(expected);

        // When
        final String actual = request.stringifyBody();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_headers() {

        // Given
        final Entry<String, List<String>> header1 = h(someString(3), someString(8));
        final Entry<String, List<String>> header2 = h(someString(5), someString(5));
        final Entry<String, List<String>> header3 = h(someString(8), someString(3));

        // When
        final StubMessage<Object, StubMessage> actual = message
            .withHeaders(new MultiValueTreeMap<>(header1))
            .withHeaders(new MultiValueTreeMap<>(header2, header3));

        // Then
        assertThat(actual, hasField("headers", new StubHeaders(header1, header2, header3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_headers_with_varargs() {

        // Given
        final Entry<String, List<String>> header1 = h(someString(3), someString(8));
        final Entry<String, List<String>> header2 = h(someString(5), someString(5));
        final Entry<String, List<String>> header3 = h(someString(8), someString(3));

        // When
        final StubMessage<Object, StubMessage> actual = message
            .withHeaders(header1)
            .withHeaders(header2, header3);

        // Then
        assertThat(actual, hasField("headers", new StubHeaders(header1, header2, header3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_headers_as_a_set() {

        // Given
        final Entry<String, List<String>> header1 = h(someString(3), someString(8));
        final Entry<String, List<String>> header2 = h(someString(5), someString(5));
        final Entry<String, List<String>> header3 = h(someString(8), someString(3));

        // When
        final StubMessage<Object, StubMessage> actual = message
            .withHeaders(new HashSet<>(singletonList(header1)))
            .withHeaders(new HashSet<>(asList(header2, header3)));

        // Then
        assertThat(actual, hasField("headers", new StubHeaders(header1, header2, header3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_retrieve_the_content_type_with_any_casing_of_the_header_name() {

        // Given
        final String contentType = someString(5);
        final Entry<String, List<String>> header1 = h(someString(3), someString(8));
        final Entry<String, List<String>> header2 =
            h(someThing(CONTENT_TYPE.toLowerCase(), CONTENT_TYPE.toUpperCase(), "CoNtEnT-tYpE"), contentType);
        final Entry<String, List<String>> header3 = h(someString(8), someString(3));

        // When
        final StubContentType actual = message.withHeaders(header1, header2, header3).getContentType();

        // Then
        assertThat(actual, equalTo(new StubContentType(contentType)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Cannot_retrieve_the_content_type_if_it_does_not_exist() {

        // Given
        final Entry<String, List<String>> header1 = h(someString(3), someString(8));
        final Entry<String, List<String>> header2 = h(someString(5), someString(5));
        final Entry<String, List<String>> header3 = h(someString(8), someString(3));

        // When
        final StubContentType actual = message.withHeaders(header1, header2, header3).getContentType();

        // Then
        assertThat(actual, nullValue());
    }

    @Test
    public void Is_a_valid_bean() {
        assertThat(message, isABeanWithValidGettersAndSettersExcluding("contentType"));
    }
}
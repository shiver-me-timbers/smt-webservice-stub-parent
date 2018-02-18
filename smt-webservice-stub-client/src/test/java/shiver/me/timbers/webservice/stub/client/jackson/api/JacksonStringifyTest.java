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

package shiver.me.timbers.webservice.stub.client.jackson.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
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
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class JacksonStringifyTest {

    private ObjectMapper mapper;
    private JacksonStringify stringify;

    @Before
    public void setUp() {
        mapper = mock(ObjectMapper.class);
        stringify = new JacksonStringify(mapper);
    }

    @Test
    public void Can_setup_the_stringify_object_mapper_correctly() {

        // Then
        then(mapper).should().configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        then(mapper).should().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        then(mapper).should().setSerializationInclusion(NON_NULL);
    }

    @Test
    public void Can_stringify_a_an_object_to_JSON() throws JsonProcessingException {

        final Object object = someThing();

        final String expected = someString();

        // Given
        given(mapper.writeValueAsString(object)).willReturn(expected);

        // When
        final String actual = stringify.toString(object);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_stringify_a_an_object_to_JSON() throws JsonProcessingException {

        final Object object = someThing();

        final JsonProcessingException exception = mock(JsonProcessingException.class);

        // Given
        given(mapper.writeValueAsString(object)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> stringify.toString(object));

        // Then
        assertThat(actual, instanceOf(JacksonException.class));
        assertThat(actual.getMessage(),
            equalTo(format("Could not serialise the (%s) object to JSON.", object.getClass()))
        );
        assertThat(actual.getCause(), is(exception));
    }
}
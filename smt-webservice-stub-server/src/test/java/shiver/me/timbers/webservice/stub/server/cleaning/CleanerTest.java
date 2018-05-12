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

package shiver.me.timbers.webservice.stub.server.cleaning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import java.util.HashSet;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Map.Entry;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;

public class CleanerTest {

    private String name1;
    private String name2;
    private BodyCleaner bodyCleaner1;
    private BodyCleaner bodyCleaner2;
    private ObjectMapper mapper;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        name1 = someString(3);
        name2 = someString(8);
        bodyCleaner1 = mock(BodyCleaner.class);
        bodyCleaner2 = mock(BodyCleaner.class);
        mapper = mock(ObjectMapper.class);
        cleaner = new Cleaner(new HashSet<>(asList(name1, name2)), asList(bodyCleaner1, bodyCleaner2), mapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_clean_the_request_headers() {

        final StringStubRequest request = mock(StringStubRequest.class);

        final Entry<String, List<String>> header1 = h(name1, someString(8));
        final Entry<String, List<String>> header2 = h(someString(5), someString(5));
        final Entry<String, List<String>> header3 = h(name2, someString(3));
        final StringStubRequest expected = mock(StringStubRequest.class);

        // Given
        given(request.getHeaders()).willReturn(new StubHeaders(header1, header2, header3));
        given(request.copy()).willReturn(expected);

        // When
        final StringStubRequest actual = cleaner.cleanHeaders(request);

        // Then
        then(expected).should().setHeaders(new StubHeaders(header1, header3));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_clean_the_request_body() {

        final StringStubRequest request = mock(StringStubRequest.class);

        final StringStubRequest expected = mock(StringStubRequest.class);

        // Given
        given(request.getBody()).willReturn(someString());
        given(bodyCleaner1.supports(request)).willReturn(false);
        given(bodyCleaner2.supports(request)).willReturn(true);
        given(bodyCleaner2.cleanBody(request)).willReturn(expected);

        // When
        final StringStubRequest actual = cleaner.cleanBody(request);

        // Then
        then(bodyCleaner1).should(never()).cleanBody(any(StringStubRequest.class));
        assertThat(actual, is(expected));
    }

    @Test
    public void Will_not_clean_an_unsupported_request_body() {

        final StringStubRequest expected = mock(StringStubRequest.class);

        // Given
        given(bodyCleaner1.supports(expected)).willReturn(false);
        given(bodyCleaner2.supports(expected)).willReturn(false);

        // When
        final StringStubRequest actual = cleaner.cleanBody(expected);

        // Then
        then(bodyCleaner1).should(never()).cleanBody(any(StringStubRequest.class));
        then(bodyCleaner2).should(never()).cleanBody(any(StringStubRequest.class));
        assertThat(actual, is(expected));
    }

    @Test
    public void Will_not_clean_a_null_request_body() {

        final StringStubRequest expected = mock(StringStubRequest.class);

        // Given
        given(bodyCleaner1.supports(expected)).willReturn(true);
        given(bodyCleaner2.supports(expected)).willReturn(true);

        // When
        final StringStubRequest actual = cleaner.cleanBody(expected);

        // Then
        then(bodyCleaner1).should(never()).cleanBody(any(StringStubRequest.class));
        then(bodyCleaner2).should(never()).cleanBody(any(StringStubRequest.class));
        assertThat(actual, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_convert_a_request_to_a_clean_string() throws JsonProcessingException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final String expected = someString();

        // Given
        given(mapper.writeValueAsString(request)).willReturn(expected);

        // When
        final String actual = cleaner.toCleanString(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_convert_a_request_to_a_clean_string() throws JsonProcessingException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final JsonProcessingException exception = mock(JsonProcessingException.class);

        // Given
        given(mapper.writeValueAsString(request)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> cleaner.toCleanString(request));

        // Then
        assertThat(actual, instanceOf(CleaningException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to convert the request into a clean string:\n%s", request)));
        assertThat(actual.getCause(), is(exception));
    }
}
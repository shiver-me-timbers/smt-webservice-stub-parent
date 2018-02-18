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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import java.io.IOException;
import java.util.Map;

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

public class JacksonBodyCleanerTest {

    private JacksonBodyCleaner cleaner;
    private ObjectMapper mapper;
    private IsSubtype isSubtype;
    private MapKeyFilter filter;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        mapper = mock(ObjectMapper.class);
        filter = mock(MapKeyFilter.class);
        isSubtype = mock(IsSubtype.class);
        cleaner = new JacksonBodyCleaner(mapper, filter) {
            @Override
            protected boolean isCorrectSubtype(StubContentType contentType) {
                return isSubtype.check(contentType);
            }
        };
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_setup_the_json_body_cleaner_object_mapper_correctly() {

        // Then
        then(mapper).should().configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        then(mapper).should().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        then(mapper).should().setSerializationInclusion(NON_NULL);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_support_a_the_correct_subtype_request() {

        final StringStubRequest request = mock(StringStubRequest.class);

        final StubContentType contentType = mock(StubContentType.class);

        // Given
        given(request.getContentType()).willReturn(contentType);
        given(isSubtype.check(contentType)).willReturn(true);

        // When
        final boolean actual = cleaner.supports(request);

        // Then
        assertThat(actual, is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Cannot_support_any_other_request() {

        final StringStubRequest request = mock(StringStubRequest.class);

        final StubContentType contentType = mock(StubContentType.class);

        // Given
        given(request.getContentType()).willReturn(contentType);
        given(isSubtype.check(contentType)).willReturn(false);

        // When
        final boolean actual = cleaner.supports(request);

        // Then
        assertThat(actual, is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_support_a_request_with_no_content_type() {

        final StringStubRequest request = mock(StringStubRequest.class);

        // Given
        given(request.getContentType()).willReturn(null);

        // When
        final boolean actual = cleaner.supports(request);

        // Then
        assertThat(actual, is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_clean_an_XML_request() throws IOException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final String body = someString();
        final Map<String, Object> map = mock(Map.class);
        final Map<String, Object> filteredMap = mock(Map.class);
        final String fieldCleanedBody = someString();
        final StringStubRequest requestCopy = mock(StringStubRequest.class);

        final StringStubRequest expected = mock(StringStubRequest.class);

        // Given
        given(request.getBody()).willReturn(body);
        given(mapper.readValue(body, Map.class)).willReturn(map);
        given(filter.filterKeys(map)).willReturn(filteredMap);
        given(mapper.writeValueAsString(filteredMap)).willReturn(fieldCleanedBody);
        given(request.copy()).willReturn(requestCopy);
        given(requestCopy.withBody(fieldCleanedBody)).willReturn(expected);

        // When
        final StringStubRequest actual = cleaner.cleanBody(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_clean_an_XML_request() throws IOException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final String body = someString();

        final IOException exception = mock(IOException.class);

        // Given
        given(request.getBody()).willReturn(body);
        given(mapper.readValue(body, Map.class)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> cleaner.cleanBody(request));

        // Then
        assertThat(actual, instanceOf(CleaningException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to clean JSON request:\n%s", request)));
        assertThat(actual.getCause(), is(exception));
    }

    private interface IsSubtype {
        boolean check(StubContentType contentType);
    }
}
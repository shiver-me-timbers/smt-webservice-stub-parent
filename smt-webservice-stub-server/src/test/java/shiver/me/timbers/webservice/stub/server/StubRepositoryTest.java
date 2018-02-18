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

package shiver.me.timbers.webservice.stub.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.server.StubRepository.FORMATTER;

public class StubRepositoryTest {

    private ObjectMapper mapper;
    private String directory;
    private Clock clock;
    private MockRepository mockRepository;
    private StubRepository repository;

    @Before
    public void setUp() {
        mapper = mock(ObjectMapper.class);
        directory = someString();
        clock = mock(Clock.class);
        mockRepository = mock(MockRepository.class);
        repository = new StubRepository(mapper, directory, clock) {
            @Override
            protected void saveWithPath(String path, String content) {
                mockRepository.save(path, content);
            }

            @Override
            protected InputStream findResponseByPath(String path) {
                return mockRepository.findResponse(path);
            }

            @Override
            protected List<String> findCallsByPath(String path) {
                return mockRepository.findCalls(path);
            }

            @Override
            protected void recordCallWithPath(String path, String content) {
                mockRepository.recordCall(path, content);
            }
        };
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_save_a_stub() throws IOException {

        final String hash = someString();
        final Stubbing<StringStubRequest, StringStubResponse> stubbing = mock(Stubbing.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final StringStubResponse response = mock(StringStubResponse.class);
        final String requestJson = someString();
        final String responseJson = someString();

        // Given
        given(stubbing.getRequest()).willReturn(request);
        given(stubbing.getResponse()).willReturn(response);
        given(mapper.writeValueAsString(request)).willReturn(requestJson);
        given(mapper.writeValueAsString(response)).willReturn(responseJson);

        // When
        repository.save(hash, stubbing);

        // Then
        then(mockRepository).should().save(format("%s%s-request.json", directory, hash), requestJson);
        then(mockRepository).should().save(format("%s%s-response.json", directory, hash), responseJson);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void Can_find_a_stubbed_response_by_a_hash() throws IOException {

        final String hash = someString();

        final InputStream stream = mock(InputStream.class);

        final StringStubResponse expected = mock(StringStubResponse.class);

        // Given
        given(mockRepository.findResponse(format("%s%s-response.json", directory, hash))).willReturn(stream);
        given(mapper.readValue(stream, StringStubResponse.class)).willReturn(expected);

        // When
        final StringStubResponse actual = repository.findResponseByHash(hash);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_find_a_call_to_a_stub() throws IOException {

        final String hash = someString();

        final String call1 = someString();
        final String call2 = someString();
        final String call3 = someString();

        // Given
        given(mockRepository.findCalls(format("%s%s-called-", directory, hash)))
            .willReturn(asList(call1, call2, call3));

        // When
        final List<String> actual = repository.findCallsByHash(hash);

        // Then
        assertThat(actual, contains(call1, call2, call3));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_record_a_call_to_the_stub() throws IOException {

        final String hash = someString();
        final StringStubRequest request = mock(StringStubRequest.class);

        final Instant now = Instant.now();
        final String json = someString();

        // Given
        given(clock.instant()).willReturn(now);
        given(mapper.writeValueAsString(request)).willReturn(json);

        // When
        repository.recordCall(hash, request);

        // Then
        then(mockRepository).should()
            .recordCall(format("%s%s-called-%s.json", directory, hash, FORMATTER.format(now)), json);
    }

    private interface MockRepository {

        void save(String name, String content);

        InputStream findResponse(String path);

        List<String> findCalls(String path);

        void recordCall(String path, String content);
    }
}
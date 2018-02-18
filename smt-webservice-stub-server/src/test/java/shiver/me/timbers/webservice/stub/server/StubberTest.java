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

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import java.io.IOException;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubberTest {

    private Digester digester;
    private StubRepository repository;
    private Stubber stubber;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubRepository.class);
        stubber = new Stubber(digester, repository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_stub_a_call() throws IOException {

        final Stubbing<StringStubRequest, StringStubResponse> stubbing = mock(Stubbing.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();

        // Given
        given(stubbing.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);

        // When
        stubber.stubCall(stubbing);

        // Then
        then(repository).should().save(hash, stubbing);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_stub_a_call() throws IOException {

        final Stubbing<StringStubRequest, StringStubResponse> stubbing = mock(Stubbing.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();

        final IOException exception = mock(IOException.class);

        // Given
        given(stubbing.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);
        willThrow(exception).given(repository).save(hash, stubbing);

        // When
        final Throwable actual = catchThrowable(() -> stubber.stubCall(stubbing));

        // Then
        assertThat(actual, instanceOf(StubbingException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to stub call with hash (%s).", hash)));
        assertThat(actual.getCause(), is(exception));
    }
}
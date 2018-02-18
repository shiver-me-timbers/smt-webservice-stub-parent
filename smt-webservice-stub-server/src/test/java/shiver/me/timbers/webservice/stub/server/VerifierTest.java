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
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import java.io.IOException;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class VerifierTest {

    private Digester digester;
    private StubRepository repository;
    private Verifier verifier;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubRepository.class);
        verifier = new Verifier(digester, repository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_that_a_stub_has_been_called() throws IOException {

        final Verifying verifying = mock(Verifying.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(singletonList(someString()));

        // When
        verifier.verify(verifying);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_that_a_stub_has_been_called_too_many_times() throws IOException {

        final Verifying verifying = mock(Verifying.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(asList(someString(), someString()));

        // When
        final Throwable actual = catchThrowable(() -> verifier.verify(verifying));

        // Given
        assertThat(actual, instanceOf(VerifyRequestException.class));
        assertThat(actual.getMessage(), equalTo(
            format("Verify Failure. Request with hash (%s) called more than once.", hash)
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_that_a_stub_has_not_been_called() throws IOException {

        final Verifying verifying = mock(Verifying.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(emptyList());

        // When
        final Throwable actual = catchThrowable(() -> verifier.verify(verifying));

        // Given
        assertThat(actual, instanceOf(VerifyRequestException.class));
        assertThat(actual.getMessage(), equalTo(
            format("Verify Failure. Request with hash (%s) was never called.", hash)
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_retrieve_the_calls() throws IOException {

        final Verifying verifying = mock(Verifying.class);

        final StringStubRequest request = mock(StringStubRequest.class);
        final String hash = someString();
        final IOException exception = mock(IOException.class);

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> verifier.verify(verifying));

        // Given
        assertThat(actual, instanceOf(VerifyRequestException.class));
        assertThat(actual.getMessage(), equalTo(
            format("Verify Failure. Request with hash (%s) could not be retrieved.", hash)
        ));
        assertThat(actual.getCause(), is(exception));
    }
}
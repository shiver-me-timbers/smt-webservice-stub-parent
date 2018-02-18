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

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import java.io.IOException;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubTest {

    private Digester digester;
    private StubRepository repository;
    private Stub stub;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubRepository.class);
        stub = new Stub(digester, repository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_call_the_stub_and_register_the_call() throws IOException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final String hash = someString();
        final StringStubResponse expected = mock(StringStubResponse.class);

        // Given
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findResponseByHash(hash)).willReturn(expected);

        // When
        final StringStubResponse actual = stub.call(request);

        // Then
        final InOrder order = inOrder(repository);
        order.verify(repository).findResponseByHash(hash);
        order.verify(repository).recordCall(hash, request);
        assertThat(actual, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_call_the_stub_and_register_the_call() throws IOException {

        final StringStubRequest request = mock(StringStubRequest.class);

        final String hash = someString();
        final IOException exception = mock(IOException.class);

        // Given
        given(digester.digestRequest(request)).willReturn(hash);
        given(repository.findResponseByHash(hash)).willThrow(exception);

        // When
        final Throwable actual = Assertions.catchThrowable(() -> stub.call(request));

        // Then
        assertThat(actual, instanceOf(StubException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to call the stub with hash (%s).", hash)));
        assertThat(actual.getCause(), is(exception));
    }
}
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

package shiver.me.timbers.webservice.stub.client;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubRequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class ThenStubTest {

    private String verifyingUrl;
    private Client client;
    private ThenStub then;

    @Before
    public void setUp() {
        verifyingUrl = someString();
        client = mock(Client.class);
        then = new ThenStub(verifyingUrl, client);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_request_should() {

        final StubRequest<Object, StubRequest> request = mock(StubRequest.class);
        final WebTarget target = mock(WebTarget.class);

        // Given
        given(client.target(verifyingUrl)).willReturn(target);

        // When
        final RequestShould actual = then.request(request);

        // Then
        assertThat(actual, hasField("request", request));
        assertThat(actual, hasField("target", target));
    }
}
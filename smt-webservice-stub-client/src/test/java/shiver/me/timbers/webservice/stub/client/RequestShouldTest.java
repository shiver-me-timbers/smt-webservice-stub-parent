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

import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubRequest;

import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class RequestShouldTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_request_verifier() {

        // Given
        final StubRequest<Object, StubRequest> request = mock(StubRequest.class);
        final WebTarget target = mock(WebTarget.class);

        // When
        final RequestVerifying actual = new RequestShould<>(request, target).should();

        // Then
        assertThat(actual, hasField("request", request));
        assertThat(actual, hasField("target", target));
    }
}
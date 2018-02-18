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
import shiver.me.timbers.webservice.stub.api.StubResponse;
import shiver.me.timbers.webservice.stub.api.Stubbing;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class RequestStubbingTest {

    private StubRequest<Object, StubRequest> request;
    private WebTarget target;
    private RequestStubbing stubbing;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        request = mock(StubRequest.class);
        target = mock(WebTarget.class);
        stubbing = new RequestStubbing<>(request, target);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_stub_a_response() {

        final StubResponse<Object, StubResponse> response = mock(StubResponse.class);

        final StubRequest<Object, StubRequest> requestCopy = mock(StubRequest.class);
        final StubResponse<Object, StubResponse> responseCopy = mock(StubResponse.class);
        final String requestBodyString = someString();
        final String responseBodyString = someString();
        final StubRequest<Object, StubRequest> requestWithBody = mock(StubRequest.class);
        final StubResponse<Object, StubResponse> responseWithBody = mock(StubResponse.class);
        final Builder builder = mock(Builder.class);

        // Given
        given(request.copy()).willReturn(requestCopy);
        given(response.copy()).willReturn(responseCopy);
        given(request.stringifyBody()).willReturn(requestBodyString);
        given(response.stringifyBody()).willReturn(responseBodyString);
        given(requestCopy.withBody(requestBodyString)).willReturn(requestWithBody);
        given(responseCopy.withBody(responseBodyString)).willReturn(responseWithBody);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);

        // When
        stubbing.willRespond(response);

        // Then
        then(builder).should()
            .put(Entity.entity(new Stubbing<>(requestWithBody, responseWithBody), APPLICATION_JSON_TYPE), String.class);
    }
}
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
import shiver.me.timbers.webservice.stub.api.Verifying;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerBetween;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class RequestVerifyingTest {

    private StubRequest<Object, StubRequest> request;
    private WebTarget target;
    private RequestVerifying verifying;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        request = mock(StubRequest.class);
        target = mock(WebTarget.class);
        verifying = new RequestVerifying(request, target);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_that_the_request_has_been_called() {

        final StubRequest<Object, StubRequest> requestCopy = mock(StubRequest.class);
        final String bodyString = someString();
        final StubRequest<Object, StubRequest> requestWithBody = mock(StubRequest.class);
        final Builder builder = mock(Builder.class);
        final Response response = mock(Response.class);

        // Given
        given(request.copy()).willReturn(requestCopy);
        given(request.stringifyBody()).willReturn(bodyString);
        given(requestCopy.withBody(bodyString)).willReturn(requestWithBody);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);
        given(builder.post(Entity.entity(new Verifying<>(requestWithBody), APPLICATION_JSON_TYPE))).willReturn(response);
        given(response.getStatus()).willReturn(OK.getStatusCode());

        // When
        verifying.beCalled();

        // Then
        then(response).should().getStatus();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_verify_that_the_request_has_been_called() {

        final StubRequest<Object, StubRequest> requestCopy = mock(StubRequest.class);
        final String bodyString = someString();
        final StubRequest<Object, StubRequest> requestWithBody = mock(StubRequest.class);
        final Builder builder = mock(Builder.class);
        final Response response = mock(Response.class);
        final String message = someString();

        // Given
        given(request.copy()).willReturn(requestCopy);
        given(request.stringifyBody()).willReturn(bodyString);
        given(requestCopy.withBody(bodyString)).willReturn(requestWithBody);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);
        given(builder.post(entity(new Verifying<>(requestWithBody), APPLICATION_JSON_TYPE))).willReturn(response);
        given(response.getStatus()).willReturn(someIntegerBetween(400, 600));
        given(response.readEntity(String.class)).willReturn(message);

        // When
        final Throwable actual = catchThrowable(() -> verifying.beCalled());

        // Then
        assertThat(actual, instanceOf(VerifyRequestError.class));
        assertThat(actual.getMessage(), is(message));
    }
}
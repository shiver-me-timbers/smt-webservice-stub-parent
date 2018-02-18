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

package shiver.me.timbers.webservice.stub.server.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.Stub;
import shiver.me.timbers.webservice.stub.server.Stubber;
import shiver.me.timbers.webservice.stub.server.Verifier;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;

public class StubControllerTest {

    private Stubber stubber;
    private Stub stub;
    private Verifier verifier;
    private StubController controller;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
        stub = mock(Stub.class);
        verifier = mock(Verifier.class);
        controller = new StubController(stubber, stub, verifier);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_stub_a_soap_request() {

        // Given
        final Stubbing<StringStubRequest, StringStubResponse> stubbing = mock(Stubbing.class);

        // When
        controller.stub(stubbing);

        // Then
        then(stubber).should().stubCall(stubbing);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_call_a_stubbed_soap_request() {

        final StringStubRequest request = mock(StringStubRequest.class);
        final StringStubResponse response = mock(StringStubResponse.class);
        final HttpStatus status = someEnum(HttpStatus.class);
        final StubHeaders headers = new StubHeaders(asList(
            h(someString(), singletonList(someString())),
            h(someString(), asList(someString(), someString())),
            h(someString(), asList(someString(), someString(), someString()))
        ));
        final String responseBody = someString();

        // Given
        given(stub.call(request)).willReturn(response);
        given(response.getStatus()).willReturn(status.value());
        given(response.getHeaders()).willReturn(headers);
        given(response.getBody()).willReturn(responseBody);

        // When
        final ResponseEntity<String> actual = controller.call(request);

        // Then
        assertThat(actual.getStatusCode().value(), equalTo(status.value()));
        assertThat(actual.getHeaders(), equalTo(toHttpHeaders(headers)));
        assertThat(actual.getBody(), equalTo(responseBody));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_that_a_soap_request_has_been_made() {

        // Given
        final Verifying<StringStubRequest> verifying = mock(Verifying.class);

        // When
        controller.verify(verifying);

        // Then
        then(verifier).should().verify(verifying);
    }

    private static HttpHeaders toHttpHeaders(StubHeaders headers) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        return httpHeaders;
    }
}
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

package shiver.me.timbers.webservice.stub.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;
import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;
import shiver.me.timbers.webservice.stub.server.Verifier;
import shiver.me.timbers.webservice.stub.server.VerifyRequestException;
import shiver.me.timbers.webservice.stub.server.lambda.api.StringVerifying;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class VerifierRequestHandlerTest {

    private Verifier verifier;
    private RequestHandler<ProxyRequest<StringVerifying>, ProxyResponse<String>> soapVerifying;

    @Before
    public void setUp() {
        verifier = mock(Verifier.class);
        soapVerifying = new VerifierRequestHandler(verifier);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_verify_a_soap_request() {

        final ProxyRequest<StringVerifying> request = mock(ProxyRequest.class);

        final StringVerifying verifying = mock(StringVerifying.class);

        // Given
        given(request.getBody()).willReturn(verifying);

        // When
        final ProxyResponse actual = soapVerifying.handleRequest(request, mock(Context.class));

        // Then
        then(verifier).should().verify(verifying);
        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getHeaders(), (Matcher) hasEntry("Content-Type", "application/json"));
        assertThat(actual.getBody(), equalTo("Verify Success."));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_verify_a_soap_request_because_it_is_called_more_than_once() {

        final ProxyRequest<StringVerifying> request = mock(ProxyRequest.class);

        final StringVerifying verifying = mock(StringVerifying.class);
        final VerifyRequestException exception = mock(VerifyRequestException.class);
        final String message = someString();

        // Given
        given(request.getBody()).willReturn(verifying);
        willThrow(exception).given(verifier).verify(verifying);
        given(exception.getMessage()).willReturn(message);

        // When
        final ProxyResponse<String> actual = soapVerifying.handleRequest(request, mock(Context.class));

        // Then
        assertThat(actual.getStatusCode(), is(400));
        assertThat(actual.getHeaders(), (Matcher) hasEntry("Content-Type", "application/json"));
        assertThat(actual.getBody(), equalTo(message));
    }
}
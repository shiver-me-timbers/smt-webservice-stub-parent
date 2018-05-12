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
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.server.Stubber;
import shiver.me.timbers.webservice.stub.server.lambda.api.StringStubbing;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class StubberRequestHandlerTest {

    private Stubber stubber;
    private RequestHandler<ProxyRequest<StringStubbing>, ProxyResponse<String>> soapStubbing;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
        soapStubbing = new StubberRequestHandler(stubber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_stub_a_request() {

        final ProxyRequest<StringStubbing> request = mock(ProxyRequest.class);

        final StringStubbing stubbing = mock(StringStubbing.class);

        // Given
        given(request.getBody()).willReturn(stubbing);

        // When
        final ProxyResponse<String> actual = soapStubbing.handleRequest(request, mock(Context.class));

        // Then
        then(stubber).should().stubCall(stubbing);
        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getHeaders(), (Matcher) hasEntry("Content-Type", "application/json"));
        assertThat(actual.getBody(), equalTo("SOAP stub saved."));
    }
}
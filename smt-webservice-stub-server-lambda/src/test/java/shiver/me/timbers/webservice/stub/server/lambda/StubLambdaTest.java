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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;
import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.StubQuery;
import shiver.me.timbers.webservice.stub.server.Stub;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;
import static shiver.me.timbers.webservice.stub.api.StubQuery.q;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AmazonS3ClientBuilder.class)
public class StubLambdaTest {

    private PathFinder pathFinder;
    private Stub stub;
    private RequestHandler<ProxyRequest<String>, ProxyResponse<String>> soapStub;

    @Before
    public void setUp() {
        pathFinder = mock(PathFinder.class);
        stub = mock(Stub.class);
        soapStub = new StubLambda(pathFinder, stub);
    }

    @Test
    public void Instantiation_for_coverage() {

        PowerMockito.mockStatic(AmazonS3ClientBuilder.class);

        // Given
        given(AmazonS3ClientBuilder.defaultClient()).willReturn(mock(AmazonS3.class));

        // When
        new StubLambda();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_soap_request() {

        final ProxyRequest<String> request = mock(ProxyRequest.class);

        final String method = someString();
        final String path = someString();
        final String qName = someString();
        final String qValue = someString();
        final String hName = someString();
        final String hValue = someString();
        final String body = someString();
        final ArgumentCaptor<StringStubRequest> captor = ArgumentCaptor.forClass(StringStubRequest.class);
        final StringStubResponse response = mock(StringStubResponse.class);

        // Given
        given(request.getHttpMethod()).willReturn(method);
        given(pathFinder.findPath(request)).willReturn(path);
        given(request.getQueryStringParameters()).willReturn(singletonMap(qName, qValue));
        given(request.getHeaders()).willReturn(singletonMap(hName, hValue));
        given(request.getBody()).willReturn(body);
        given(stub.call(captor.capture())).willReturn(response);
        given(response.getStatus()).willReturn(someInteger());
        given(response.getHeaders()).willReturn(new StubHeaders());
        given(response.getBody()).willReturn(someString());

        // When
        final ProxyResponse<String> actual = soapStub.handleRequest(request, mock(Context.class));

        // Then
        final StringStubRequest stubRequest = captor.getValue();
        assertThat(stubRequest.getMethod(), is(method));
        assertThat(stubRequest.getPath(), is(path));
        assertThat(stubRequest.getQuery(), equalTo(new StubQuery(q(qName, qValue))));
        assertThat(stubRequest.getHeaders(), equalTo(new StubHeaders(h(hName, hValue))));
        assertThat(stubRequest.getBody(), is(body));
        assertThat(actual, equalTo(new StubProxyResponse(response)));
    }
}
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
import org.apache.log4j.Logger;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequestHandler;
import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.StubQuery;
import shiver.me.timbers.webservice.stub.server.Stub;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import static java.lang.String.format;
import static shiver.me.timbers.webservice.stub.api.MultiValueTreeMap.toMultiTreeMap;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.digester;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.repository;

public class StubLambda implements ProxyRequestHandler {

    private final Logger log = Logger.getLogger(getClass());
    private final PathFinder pathFinder;
    private final Stub stub;

    public StubLambda() {
        this(new PathFinder(), new Stub(digester(), repository()));
    }

    StubLambda(PathFinder pathFinder, Stub stub) {
        this.pathFinder = pathFinder;
        this.stub = stub;
    }

    @Override
    public ProxyResponse<String> handleRequest(ProxyRequest<String> request, Context context) {
        log.info("START: Soap studded response.");
        final StringStubResponse response = stub.call(
            new StringStubRequest(request.getBody())
                .withMethod(request.getHttpMethod())
                .withPath(pathFinder.findPath(request))
                .withQuery(new StubQuery(toMultiTreeMap(request.getQueryStringParameters())))
                .withHeaders(new StubHeaders(toMultiTreeMap(request.getHeaders())))
        );
        log.info(format("RESPONSE:\n%s", response));
        return new StubProxyResponse(response);
    }
}

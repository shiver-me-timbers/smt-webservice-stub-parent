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
import shiver.me.timbers.aws.apigateway.proxy.DeserialisedProxyRequestHandler;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.StubRepository;
import shiver.me.timbers.webservice.stub.server.Verifier;
import shiver.me.timbers.webservice.stub.server.VerifyRequestException;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

class VerifierRequestHandler implements DeserialisedProxyRequestHandler<Verifying, String> {

    private final Logger log = Logger.getLogger(getClass());

    private final Verifier verifier;

    VerifierRequestHandler(Digester digester, StubRepository repository) {
        this(new Verifier(digester, repository));
    }

    VerifierRequestHandler(Verifier verifier) {
        this.verifier = verifier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StubberProxyResponse handleRequest(ProxyRequest<Verifying> request, Context context) {
        log.info("START: Starting verification.");
        try {
            verifier.verify(request.getBody());
        } catch (VerifyRequestException e) {
            return new StubberProxyResponse(400, e.getMessage());
        }
        return new StubberProxyResponse("Verify Success.");
    }
}

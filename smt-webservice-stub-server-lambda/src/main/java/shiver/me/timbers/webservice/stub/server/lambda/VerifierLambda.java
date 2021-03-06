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

import shiver.me.timbers.aws.apigateway.proxy.JsonProxyRequestHandler;
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.lambda.api.StringVerifying;

import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.digester;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.jsonMapper;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.repository;

public class VerifierLambda extends JsonProxyRequestHandler<StringVerifying, String> {

    public VerifierLambda() {
        super(
            StringVerifying.class,
            jsonMapper(),
            new VerifierRequestHandler(digester(), repository())
        );
    }
}

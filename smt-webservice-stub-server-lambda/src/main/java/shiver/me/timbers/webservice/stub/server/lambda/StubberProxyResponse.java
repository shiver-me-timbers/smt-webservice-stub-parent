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

import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;

import static java.util.Collections.singletonMap;

public class StubberProxyResponse extends ProxyResponse<String> {

    public StubberProxyResponse(String message) {
        this(200, message);
    }

    public StubberProxyResponse(int status, String message) {
        super(status);
        setHeaders(singletonMap("Content-Type", "application/json"));
        setBody(message);
    }
}

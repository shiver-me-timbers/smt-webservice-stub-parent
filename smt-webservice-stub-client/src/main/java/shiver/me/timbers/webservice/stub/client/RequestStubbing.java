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

import shiver.me.timbers.webservice.stub.api.StubRequest;
import shiver.me.timbers.webservice.stub.api.StubResponse;
import shiver.me.timbers.webservice.stub.api.Stubbing;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RequestStubbing<RQ extends StubRequest> {

    private final StubRequest request;
    private final WebTarget target;

    RequestStubbing(RQ request, WebTarget target) {
        this.request = request;
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public <RS extends StubResponse> void willRespond(RS response) {
        target.request(APPLICATION_JSON_TYPE).put(
            entity(
                new Stubbing(
                    (StubRequest) request.copy().withBody(request.stringifyBody()),
                    (StubResponse) response.copy().withBody(response.stringifyBody())
                ),
                APPLICATION_JSON_TYPE
            ),
            String.class
        );
    }
}

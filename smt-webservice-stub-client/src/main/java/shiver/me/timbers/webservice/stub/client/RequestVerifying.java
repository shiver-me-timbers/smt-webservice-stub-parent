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
import shiver.me.timbers.webservice.stub.api.Verifying;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RequestVerifying<RQ extends StubRequest> {

    private final RQ request;
    private final WebTarget target;

    RequestVerifying(RQ request, WebTarget target) {
        this.request = request;
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public void beCalled() {
        final Response response = target.request(APPLICATION_JSON_TYPE).post(
            entity(new Verifying((StubRequest) request.copy().withBody(request.stringifyBody())), APPLICATION_JSON_TYPE)
        );
        if (response.getStatus() >= 400) {
            throw new VerifyRequestError(response.readEntity(String.class));
        }
    }
}

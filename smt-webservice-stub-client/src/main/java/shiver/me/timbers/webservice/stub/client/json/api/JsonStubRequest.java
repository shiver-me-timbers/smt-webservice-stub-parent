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

package shiver.me.timbers.webservice.stub.client.json.api;

import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.api.StubRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static shiver.me.timbers.webservice.stub.client.json.api.JsonStringify.jsonStringify;

/**
 * @author Karl Bennett
 */
public class JsonStubRequest<T> extends StubRequest<T, JsonStubRequest> {

    public static JsonStubRequest<Void> jsonReq() {
        return new JsonStubRequest<>();
    }

    public static <T> JsonStubRequest<T> jsonReq(T body) {
        return new JsonStubRequest<>(body);
    }

    public JsonStubRequest() {
        super(jsonStringify());
        withJsonContentType();
    }

    public JsonStubRequest(T body) {
        super(jsonStringify(), body);
        withJsonContentType();
    }

    private void withJsonContentType() {
        withHeaders(new StubContentType(APPLICATION_JSON));
    }

    private JsonStubRequest(JsonStubRequest<T> request) {
        super(request);
    }

    @Override
    public JsonStubRequest copy() {
        return new JsonStubRequest<>(this);
    }
}

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
import shiver.me.timbers.webservice.stub.api.StubResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static shiver.me.timbers.webservice.stub.client.json.api.JsonStringify.jsonStringify;

/**
 * @author Karl Bennett
 */
public class JsonStubResponse<T> extends StubResponse<T, JsonStubResponse<T>> {

    public static JsonStubResponse<Void> jsonRes() {
        return new JsonStubResponse<>();
    }

    public static <T> JsonStubResponse<T> jsonRes(T body) {
        return new JsonStubResponse<>(body);
    }

    public JsonStubResponse() {
        super(jsonStringify());
        withJsonContentType();
    }

    public JsonStubResponse(T body) {
        super(jsonStringify(), body);
        withJsonContentType();
    }

    private JsonStubResponse(JsonStubResponse<T> request) {
        super(request);
    }

    private void withJsonContentType() {
        withHeaders(new StubContentType(APPLICATION_JSON));
    }

    @Override
    public JsonStubResponse<T> copy() {
        return new JsonStubResponse<>(this);
    }
}

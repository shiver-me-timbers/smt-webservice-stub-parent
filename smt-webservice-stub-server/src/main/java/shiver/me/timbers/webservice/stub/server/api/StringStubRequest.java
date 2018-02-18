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

package shiver.me.timbers.webservice.stub.server.api;

import shiver.me.timbers.webservice.stub.api.StubRequest;

import static shiver.me.timbers.webservice.stub.server.api.StringStringify.STRINGIFY;

/**
 * @author Karl Bennett
 */
public class StringStubRequest extends StubRequest<String, StringStubRequest> {

    public StringStubRequest() {
        super(STRINGIFY);
    }

    public StringStubRequest(String body) {
        super(STRINGIFY, body);
    }

    public StringStubRequest(StubRequest<String, StringStubRequest> request) {
        super(request);
    }

    @Override
    public StringStubRequest copy() {
        return new StringStubRequest(this);
    }
}

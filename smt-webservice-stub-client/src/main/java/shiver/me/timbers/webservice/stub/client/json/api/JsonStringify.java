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

import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.webservice.stub.client.jackson.api.JacksonStringify;

/**
 * @author Karl Bennett
 */
public class JsonStringify extends JacksonStringify {

    public static JsonStringify jsonStringify() {
        return new JsonStringify(new ObjectMapper());
    }

    public JsonStringify(ObjectMapper mapper) {
        super(mapper);
    }
}

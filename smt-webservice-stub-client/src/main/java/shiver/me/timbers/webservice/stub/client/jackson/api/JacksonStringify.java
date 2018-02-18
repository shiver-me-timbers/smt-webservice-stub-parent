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

package shiver.me.timbers.webservice.stub.client.jackson.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.webservice.stub.api.Stringify;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public class JacksonStringify implements Stringify {

    private final ObjectMapper mapper;

    public static JacksonStringify jsonStringify() {
        return new JacksonStringify(new ObjectMapper());
    }

    public JacksonStringify(ObjectMapper mapper) {
        this.mapper = mapper;
        mapper.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(NON_NULL);
    }

    @Override
    public String toString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JacksonException(format("Could not serialise the (%s) object to JSON.", object.getClass()), e);
        }
    }
}

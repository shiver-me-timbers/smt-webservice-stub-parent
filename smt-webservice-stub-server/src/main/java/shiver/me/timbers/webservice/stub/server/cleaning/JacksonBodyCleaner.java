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

package shiver.me.timbers.webservice.stub.server.cleaning;

import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public abstract class JacksonBodyCleaner implements BodyCleaner {

    private final ObjectMapper mapper;
    private final MapKeyFilter filter;

    public JacksonBodyCleaner(ObjectMapper mapper, MapKeyFilter filter) {
        mapper.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(NON_NULL);
        this.mapper = mapper;
        this.filter = filter;
    }

    @Override
    public boolean supports(StringStubRequest request) {
        final StubContentType contentType = request.getContentType();
        return contentType != null && isCorrectSubtype(contentType);
    }

    protected abstract boolean isCorrectSubtype(StubContentType contentType);

    @SuppressWarnings("unchecked")
    @Override
    public StringStubRequest cleanBody(StringStubRequest request) {
        try {
            return request.copy()
                .withBody(mapper.writeValueAsString(filter.filterKeys(mapper.readValue(request.getBody(), Map.class))));
        } catch (IOException e) {
            throw new CleaningException(format("Failed to clean JSON request:\n%s", request), e);
        }
    }
}

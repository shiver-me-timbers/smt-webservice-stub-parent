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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
public class Cleaner {

    private final Set<String> keepHeaders;
    private final List<BodyCleaner> bodyCleaners;
    private final ObjectMapper mapper;

    public Cleaner(Set<String> keepHeaders, List<BodyCleaner> bodyCleaners, ObjectMapper mapper) {
        this.keepHeaders = keepHeaders;
        this.bodyCleaners = bodyCleaners;
        this.mapper = mapper;
    }

    public StringStubRequest cleanHeaders(StringStubRequest request) {
        final Map<String, List<String>> filteredHeaders = request.getHeaders().entrySet().stream()
            .filter(entry -> keepHeaders.contains(entry.getKey()))
            .collect(toMap(Entry::getKey, Entry::getValue));
        final StringStubRequest requestCopy = request.copy();
        requestCopy.setHeaders(new StubHeaders(filteredHeaders));
        return requestCopy;
    }

    public StringStubRequest cleanBody(StringStubRequest request) {
        return bodyCleaners.stream().filter(cleaner -> cleaner.supports(request)).findFirst()
            .orElse(new NoOpBodyCleaner())
            .cleanBody(request);
    }

    public String toCleanString(StringStubRequest request) {
        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new CleaningException(format("Failed to convert the request into a clean string:\n%s", request), e);
        }
    }
}

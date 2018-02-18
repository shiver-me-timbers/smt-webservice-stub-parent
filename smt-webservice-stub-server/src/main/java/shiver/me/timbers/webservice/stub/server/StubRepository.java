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

package shiver.me.timbers.webservice.stub.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public abstract class StubRepository {

    static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd-HH-mm-ss-SSSS").withZone(ZoneId.systemDefault());

    private final Logger log = Logger.getLogger(getClass());

    private final ObjectMapper mapper;
    private final String directory;
    private final Clock clock;

    protected StubRepository(ObjectMapper mapper, String directory, Clock clock) {
        this.mapper = mapper;
        this.directory = directory;
        this.clock = clock;
    }

    public void save(String hash, Stubbing stubbing) throws IOException {
        final HashMap<String, String> map = new HashMap<>();
        map.put("request", mapper.writeValueAsString(stubbing.getRequest()));
        map.put("response", mapper.writeValueAsString(stubbing.getResponse()));
        // Run the put requests in parallel for speed.
        map.entrySet().parallelStream().forEach(entry -> {
            final String fileName = format("%s%s-%s.json", directory, hash, entry.getKey());
            log.info(format("Saving the stubbing to (%s).", fileName));
            saveWithPath(fileName, entry.getValue());
        });
    }

    @SuppressWarnings("unchecked")
    public StringStubResponse findResponseByHash(String hash) throws IOException {
        final String path = format("%s%s-response.json", directory, hash);
        log.info(format("Getting response with path (%s).", path));
        return mapper.readValue(findResponseByPath(path), StringStubResponse.class);
    }

    public List<String> findCallsByHash(String hash) throws IOException {
        return findCallsByPath(format("%s%s-called-", directory, hash));
    }

    public void recordCall(String hash, StringStubRequest request) throws IOException {
        recordCallWithPath(format("%s%s-called-%s.json", directory, hash, FORMATTER.format(clock.instant())),
            mapper.writeValueAsString(request)
        );
    }

    protected abstract void saveWithPath(String path, String content);

    protected abstract InputStream findResponseByPath(String path) throws IOException;

    protected abstract List<String> findCallsByPath(String path) throws IOException;

    protected abstract void recordCallWithPath(String path, String content) throws IOException;
}

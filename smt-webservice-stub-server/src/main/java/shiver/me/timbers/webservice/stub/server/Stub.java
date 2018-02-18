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

import org.apache.log4j.Logger;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import java.io.IOException;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public class Stub {

    private final Logger log = Logger.getLogger(getClass());

    private final Digester digester;
    private final StubRepository repository;

    public Stub(Digester digester, StubRepository repository) {
        this.digester = digester;
        this.repository = repository;
    }

    public StringStubResponse call(StringStubRequest request) {
        log.info(format("REQUEST:\n%s", request));
        final String hash = digester.digestRequest(request);
        log.info(format("Finding stubbed response for hash (%s).", hash));
        try {
            final StringStubResponse response = repository.findResponseByHash(hash);
            log.info(format("Saving stub call for hash (%s).", hash));
            repository.recordCall(hash, request);
            return response;
        } catch (IOException e) {
            throw new StubException(format("Failed to call the stub with hash (%s).", hash), e);
        }
    }
}

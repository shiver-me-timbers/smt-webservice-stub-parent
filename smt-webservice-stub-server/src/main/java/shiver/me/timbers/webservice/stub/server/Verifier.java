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
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public class Verifier {

    private final Logger log = Logger.getLogger(getClass());

    private final Digester digester;
    private final StubRepository repository;

    public Verifier(Digester digester, StubRepository repository) {
        this.digester = digester;
        this.repository = repository;
    }

    public void verify(Verifying<StringStubRequest> verifying) {
        log.info(format("REQUEST:\n%s", verifying.getRequest()));
        final String hash = digester.digestRequest(verifying.getRequest());
        try {
            final List<String> calls = repository.findCallsByHash(hash);
            if (calledMoreThanOnce(calls)) {
                log.info(format("Verify for (%s) has failed. Request called more than once.", hash));
                throw new VerifyRequestException(
                    format("Verify Failure. Request with hash (%s) called more than once.", hash)
                );
            }
            if (neverCalled(calls)) {
                log.info(format("Verify for (%s) has failed. Request never called.", hash));
                throw new VerifyRequestException(format("Verify Failure. Request with hash (%s) was never called.", hash));
            }
        } catch (IOException e) {
            throw new VerifyRequestException(
                format("Verify Failure. Request with hash (%s) could not be retrieved.", hash),
                e
            );
        }
        log.info(format("Verify for (%s) has succeeded.", hash));
    }

    private static boolean calledMoreThanOnce(List calls) {
        return calls.size() > 1;
    }

    private static boolean neverCalled(List calls) {
        return calls.size() < 1;
    }
}

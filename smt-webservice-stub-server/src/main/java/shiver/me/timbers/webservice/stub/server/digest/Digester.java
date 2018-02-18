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

package shiver.me.timbers.webservice.stub.server.digest;

import org.apache.log4j.Logger;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.cleaning.Cleaner;

import static java.lang.String.format;

public class Digester {

    private final Logger log = Logger.getLogger(getClass());

    private final Cleaner cleaner;
    private final MessageDigestFactory factory;
    private final Encoding encoding;

    public Digester(Cleaner cleaner, MessageDigestFactory factory, Encoding encoding) {
        this.cleaner = cleaner;
        this.factory = factory;
        this.encoding = encoding;
    }

    public String digestRequest(StringStubRequest request) {
        final String requestString = cleaner.toCleanString(cleaner.cleanHeaders(cleaner.cleanBody(request)));
        log.info(format("Digesting XML:\n%s", requestString));
        return encoding.toHex(factory.create("MD5").digest(requestString.getBytes()));
    }
}

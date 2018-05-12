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

package shiver.me.timbers.webservice.stub.server.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import shiver.me.timbers.webservice.stub.server.IOStreams;
import shiver.me.timbers.webservice.stub.server.Stub;
import shiver.me.timbers.webservice.stub.server.Stubber;
import shiver.me.timbers.webservice.stub.server.Verifier;
import shiver.me.timbers.webservice.stub.server.cleaning.Cleaner;
import shiver.me.timbers.webservice.stub.server.cleaning.MapKeyFilter;
import shiver.me.timbers.webservice.stub.server.digest.Digester;
import shiver.me.timbers.webservice.stub.server.digest.Encoding;
import shiver.me.timbers.webservice.stub.server.digest.MessageDigestFactory;
import shiver.me.timbers.webservice.stub.server.json.JsonBodyCleaner;
import shiver.me.timbers.webservice.stub.server.xml.XmlBodyCleaner;

import java.io.InputStream;
import java.time.Clock;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author Karl Bennett
 */
@Configuration
@ComponentScan
public class StubServerConfiguration {

    @Bean
    public Stubber stubber(Digester digester, FileStubRepository repository) {
        return new Stubber(digester, repository);
    }

    @Bean
    public Stub stub(Digester digester, FileStubRepository repository) {
        return new Stub(digester, repository);
    }

    @Bean
    public Verifier verifier(Digester digester, FileStubRepository repository) {
        return new Verifier(digester, repository);
    }

    @Bean
    public Digester digester(
        @Value("#{'${stub.headers.to.keep:}'.split(',')}") Set<String> headersToKeep,
        @Value("#{'${stub.xml.tags.to.ignore:}'.split(',')}") Set<String> tagsToIgnore,
        @Value("#{'${stub.json.fields.to.ignore:}'.split(',')}") Set<String> fieldsToIgnore
    ) {
        return new Digester(
            new Cleaner(
                headersToKeep,
                asList(
                    new XmlBodyCleaner(new XmlMapper(), new MapKeyFilter(tagsToIgnore)),
                    new JsonBodyCleaner(new ObjectMapper(), new MapKeyFilter(fieldsToIgnore))
                ),
                new ObjectMapper()
            ),
            new MessageDigestFactory(),
            new Encoding()
        );
    }

    @Bean
    public Clock systemDefaultZoneClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public IOStreams ioStreams() {
        return new IOStreams();
    }
}

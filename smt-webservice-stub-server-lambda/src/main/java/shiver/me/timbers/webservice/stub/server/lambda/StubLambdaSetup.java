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

package shiver.me.timbers.webservice.stub.server.lambda;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import shiver.me.timbers.webservice.stub.server.Env;
import shiver.me.timbers.webservice.stub.server.cleaning.Cleaner;
import shiver.me.timbers.webservice.stub.server.cleaning.MapKeyFilter;
import shiver.me.timbers.webservice.stub.server.digest.Digester;
import shiver.me.timbers.webservice.stub.server.digest.Encoding;
import shiver.me.timbers.webservice.stub.server.digest.MessageDigestFactory;
import shiver.me.timbers.webservice.stub.server.json.JsonBodyCleaner;
import shiver.me.timbers.webservice.stub.server.xml.XmlBodyCleaner;

import java.time.Clock;

import static java.util.Arrays.asList;

class StubLambdaSetup {

    private static ObjectMapper JSON_MAPPER;
    private static XmlMapper XML_MAPPER;
    private static Digester DIGESTER;
    private static S3StubRepository REPOSITORY;
    private static Env ENV;

    static ObjectMapper jsonMapper() {
        if (JSON_MAPPER == null) {
            JSON_MAPPER = new ObjectMapper();
        }
        return JSON_MAPPER;
    }

    static XmlMapper xmlMapper() {
        if (XML_MAPPER == null) {
            XML_MAPPER = new XmlMapper();
        }
        return XML_MAPPER;
    }

    static Digester digester() {
        if (DIGESTER == null) {
            DIGESTER = new Digester(
                new Cleaner(
                    env().getAsSet("STUB_HEADERS_TO_KEEP"),
                    asList(
                        new XmlBodyCleaner(xmlMapper(), new MapKeyFilter(env().getAsSet("STUB_XML_TAGS_TO_IGNORE"))),
                        new JsonBodyCleaner(jsonMapper(), new MapKeyFilter(env().getAsSet("STUB_JSON_FIELDS_TO_IGNORE")))
                    ),
                    jsonMapper()
                ),
                new MessageDigestFactory(),
                new Encoding()
            );
        }
        return DIGESTER;
    }

    static S3StubRepository repository() {
        if (REPOSITORY == null) {
            REPOSITORY = new S3StubRepository(
                jsonMapper(),
                env().get("S3_DIRECTORY_NAME"),
                Clock.systemDefaultZone(),
                env().get("S3_BUCKET_NAME"),
                AmazonS3ClientBuilder.defaultClient()
            );
        }
        return REPOSITORY;
    }

    static Env env() {
        if (ENV == null) {
            ENV = new Env();
        }
        return ENV;
    }
}

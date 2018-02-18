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

import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.StubResponse;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Map.Entry;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;
import static shiver.me.timbers.webservice.stub.client.json.api.JsonStubResponse.jsonRes;

public class JsonStubResponseTest {

    private static final StubContentType CONTENT_TYPE = new StubContentType(APPLICATION_JSON);

    @Test
    public void Instantiation_for_coverage() {
        jsonRes();
        jsonRes(someThing());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_copy_a_JSON_response() {

        final int status = someInteger();
        final Entry<String, List<String>> header1 = h(someString(3), someString(5));
        final Entry<String, List<String>> header2 = h(someString(8), someString(13));
        final StubHeaders headers = new StubHeaders(asList(header1, header2));
        final Object body = someThing();

        // Given
        final StubResponse<Object, JsonStubResponse<Object>> response = new JsonStubResponse<>(body)
            .withStatus(status)
            .withHeaders(headers);

        // When
        final JsonStubResponse actual = response.copy();

        // Then
        assertThat(actual, not(sameInstance(response)));
        assertThat(actual, hasField("status", status));
        assertThat(actual, hasField("headers", new StubHeaders(CONTENT_TYPE, header1, header2)));
        assertThat(actual, hasField("body", body));
    }
}
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

package shiver.me.timbers.webservice.stub.api;

import org.junit.Test;

import java.util.HashMap;

import static java.util.Arrays.asList;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;

public class StubHeadersTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_some_headers() {
        new StubHeaders();
        new StubHeaders(
            h(someString(1), someString(2)),
            h(someString(3), someString(5), someString(8)),
            h(someString(13), asList(someString(21), someString(34), someString(55)))
        );
        new StubHeaders(asList(
            h(someString(1), someString(2)),
            h(someString(3), someString(5)),
            h(someString(8), someString(13))
        ));
        new StubHeaders(new HashMap<>());
    }
}
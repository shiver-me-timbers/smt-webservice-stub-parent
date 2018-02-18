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

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static shiver.me.timbers.data.random.RandomStrings.someString;

class Equals {

    static final StubQuery RED_QUERY = new StubQuery();
    static final StubQuery BLACK_QUERY = new StubQuery(singletonMap(someString(3), singletonList(someString(5))));
    static final StubHeaders RED_HEADERS = new StubHeaders();
    static final StubHeaders BLACK_HEADERS = new StubHeaders(singletonMap(someString(3), singletonList(someString(5))));
}

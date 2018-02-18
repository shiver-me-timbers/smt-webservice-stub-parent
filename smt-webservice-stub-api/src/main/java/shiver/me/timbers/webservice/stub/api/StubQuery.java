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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry;

/**
 * @author Karl Bennett
 */
public class StubQuery extends MultiValueTreeMap<String, String> {

    public static StubQuery parse(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return new StubQuery();
        }
        final StubQuery query = new StubQuery();
        Arrays.stream(queryString.split("&"))
            .map(param -> param.split("="))
            .forEach(array -> query.add(array[0], array[1]));
        return query;
    }

    public static Entry<String, List<String>> q(String name, String... values) {
        return e(name, values);
    }

    public static Entry<String, List<String>> q(String name, List<String> values) {
        return e(name, values);
    }

    public StubQuery() {
    }

    public StubQuery(Map<String, List<String>> query) {
        super(query);
    }

    @SafeVarargs
    public StubQuery(Entry<String, List<String>>... query) {
        super(query);
    }

    public StubQuery(Collection<Entry<String, List<String>>> values) {
        super(values);
    }
}

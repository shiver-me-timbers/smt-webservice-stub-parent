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

package shiver.me.timbers.webservice.stub.server.cleaning;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;

public class MapKeyFilterTest {

    @Test
    public void Can_filter_keys_out_of_a_map() {

        final Map<String, Object> map = new HashMap<>();

        final String key1 = someAlphanumericString(2);
        final String key2 = someAlphanumericString(3);
        final String key3 = someAlphanumericString(5);
        final String key4 = someAlphanumericString(8);
        final String key5 = someAlphanumericString(13);
        final String key6 = someAlphanumericString(21);
        final String value1 = someAlphanumericString(2);
        final String value2 = someAlphanumericString(2);
        final String value3 = someAlphanumericString(2);
        final String value4 = someAlphanumericString(2);

        final Map<String, Object> expected = new HashMap<>();

        // Given
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, singletonMap(key4, value3));
        map.put(key5, singletonMap(key6, value4));
        expected.put(key1, value1);
        expected.put(key5, emptyMap());

        // When
        final Map<String, Object> actual = new MapKeyFilter(new HashSet<>(asList(key2, key3, key6))).filterKeys(map);

        // Then
        assertThat(actual, equalTo(expected));
    }
}
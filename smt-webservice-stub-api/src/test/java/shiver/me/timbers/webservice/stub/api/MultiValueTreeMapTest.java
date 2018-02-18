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

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.webservice.stub.api.MultiValueTreeMap.e;

public class MultiValueTreeMapTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_multi_value_tree_map() {

        // Given
        final String name1 = someString(3);
        final String name2 = someString(5);
        final String name3 = someString(8);
        final Object value1 = someThing();
        final Object value2 = someThing();
        final Object value3 = someThing();
        final List<Object> value4 = asList(someThing(), someThing(), someThing());

        // When
        final MultiValueTreeMap<String, Object> actual = new MultiValueTreeMap<>(
            e(name1, value1), e(name2, value2), e(name2, value3), e(name3, value4)
        );

        // Then
        assertThat(actual, allOf(
            (Matcher) hasEntry(name1, singletonList(value1)),
            (Matcher) hasEntry(name2, asList(value2, value3)),
            (Matcher) hasEntry(name3, value4)
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_multi_value_tree_map_from_a_normal_map() {

        // Given
        final String name1 = someString(3);
        final String name2 = someString(5);
        final String name3 = someString(8);
        final Object value1 = someThing();
        final Object value2 = someThing();
        final Object value3 = someThing();
        final Map<String, Object> map = of(
            new SimpleEntry<>(name1, value1),
            new SimpleEntry<>(name2, value2),
            new SimpleEntry<>(name3, value3)
        ).collect(toMap(Entry::getKey, Entry::getValue));

        // When
        final MultiValueTreeMap<String, Object> actual = MultiValueTreeMap.toMultiTreeMap(map);

        // Then
        assertThat(actual, allOf(
            (Matcher) hasEntry(name1, singletonList(value1)),
            (Matcher) hasEntry(name2, singletonList(value2)),
            (Matcher) hasEntry(name3, singletonList(value3))
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_set_an_entry() {

        // Given
        final String name1 = someString(3);
        final String name2 = someString(5);
        final String name3 = someString(8);
        final Object value1 = someThing();
        final Object value2 = someThing();
        final Object value3 = someThing();
        final List<Object> value4 = asList(someThing(), someThing(), someThing());

        // When
        final MultiValueTreeMap<String, Object> actual = new MultiValueTreeMap<>();
        actual.put(name1, value1);
        actual.put(name2, value2, value3);
        actual.put(name3, value4);

        // Then
        assertThat(actual, allOf(
            (Matcher) hasEntry(name1, singletonList(value1)),
            (Matcher) hasEntry(name2, asList(value2, value3)),
            (Matcher) hasEntry(name3, value4)
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_add_values_to_a_header() {

        // Given
        final String name = someString(3);
        final Object value1 = someThing();
        final Object value2 = someThing();
        final Object value3 = someThing();
        final List<Object> value4 = asList(someThing(), someThing(), someThing());

        // When
        final MultiValueTreeMap<String, Object> actual = new MultiValueTreeMap<>();
        actual.add(name, value1);
        actual.add(name, value2, value3);
        actual.add(name, value4);

        // Then
        assertThat(actual, hasEntry(name, concat(of(value1, value2, value3), value4.stream()).collect(toList())));
    }
}
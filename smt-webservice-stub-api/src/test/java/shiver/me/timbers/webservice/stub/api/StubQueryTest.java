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

import java.util.HashMap;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.api.StubQuery.q;

public class StubQueryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_some_queries() {
        new StubQuery();
        new StubQuery(
            q(someString(1), someString(2)),
            q(someString(3), someString(5), someString(8)),
            q(someString(13), asList(someString(21), someString(34), someString(55)))
        );
        new StubQuery(asList(
            q(someString(1), someString(2)),
            q(someString(3), someString(5)),
            q(someString(8), someString(13))
        ));
        new StubQuery(new HashMap<>());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_parse_a_query_string() {

        // Given
        final String name1 = someAlphanumericString(1);
        final String name2 = someAlphanumericString(2);
        final String name3 = someAlphanumericString(3);
        final String value1 = someAlphanumericString(5);
        final String value2 = someAlphanumericString(8);
        final String value3 = someAlphanumericString(13);
        final String value4 = someAlphanumericString(21);
        final String value5 = someAlphanumericString(34);
        final String query = format(
            "%s=%s&%s=%s&%s=%s&%s=%s&%s=%s",
            name1, value1, name2, value2, name3, value3, name3, value4, name3, value5
        );

        // When
        final StubQuery actual = StubQuery.parse(query);

        // Then
        assertThat(actual, allOf(
            (Matcher) hasEntry(name1, singletonList(value1)),
            (Matcher) hasEntry(name2, singletonList(value2)),
            (Matcher) hasEntry(name3, asList(value3, value4, value5))
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_parse_a_null_query_string() {

        // When
        final StubQuery actual = StubQuery.parse(null);

        // Then
        assertThat(actual, equalTo(new StubQuery()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_parse_an_empty_query_string() {

        // When
        final StubQuery actual = StubQuery.parse("");

        // Then
        assertThat(actual, equalTo(new StubQuery()));
    }
}
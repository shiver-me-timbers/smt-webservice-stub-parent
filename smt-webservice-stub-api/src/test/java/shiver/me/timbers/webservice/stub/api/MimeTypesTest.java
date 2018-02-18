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

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class MimeTypesTest {

    @Test
    public void Can_check_if_the_content_type_is_whats_expected() {

        // Given
        final String subType = someAlphanumericString(3);

        // When
        final boolean actual = new MimeTypes().containsSubType(
            format("%s/%s %s", someAlphanumericString(13), subType, someString()),
            subType
        );

        // Then
        assertThat(actual, is(true));
    }

    @Test
    public void Can_check_if_the_content_type_is_not_whats_expected() {

        // When
        final boolean actual = new MimeTypes().containsSubType(
            format("%s/%s %s", someAlphanumericString(13), someAlphanumericString(3), someString()),
            someAlphanumericString(5)
        );

        // Then
        assertThat(actual, is(false));
    }

    @Test
    public void Can_check_if_the_content_type_of_a_malformed_mime_type() {

        // When
        final boolean actual = new MimeTypes().containsSubType(someAlphanumericString(), someAlphanumericString(3));

        // Then
        assertThat(actual, is(false));
    }
}
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

package shiver.me.timbers.webservice.stub.client.soap.example;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class Argument3Test {

    @Test
    public void Instantiation_for_coverage() {
        new Argument3();
    }

    @Test
    public void Can_get_the_arguments_value() {

        // Given
        final String expected = someString();

        // When
        final String actual = new Argument3(expected).getValue();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void The_argument_has_equality() {
        EqualsVerifier.forClass(Argument3.class).usingGetClass().verify();
    }
}
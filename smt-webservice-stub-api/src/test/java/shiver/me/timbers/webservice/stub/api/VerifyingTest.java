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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.webservice.stub.api.Equals.BLACK_HEADERS;
import static shiver.me.timbers.webservice.stub.api.Equals.BLACK_QUERY;
import static shiver.me.timbers.webservice.stub.api.Equals.RED_HEADERS;
import static shiver.me.timbers.webservice.stub.api.Equals.RED_QUERY;

public class VerifyingTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_verifying() {
        new Verifying();
        new Verifying(mock(StubRequest.class));
    }

    @Test
    public void Is_a_valid_bean() {
        assertThat(Verifying.class, hasValidGettersAndSetters());
    }

    @Test
    public void Has_equality() {
        EqualsVerifier.forClass(Verifying.class)
            .usingGetClass()
            .withPrefabValues(StubQuery.class, RED_QUERY, BLACK_QUERY)
            .withPrefabValues(StubHeaders.class, RED_HEADERS, BLACK_HEADERS)
            .suppress(NONFINAL_FIELDS)
            .verify();
    }
}
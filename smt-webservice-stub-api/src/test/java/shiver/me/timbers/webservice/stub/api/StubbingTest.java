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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.webservice.stub.api.Equals.BLACK_HEADERS;
import static shiver.me.timbers.webservice.stub.api.Equals.BLACK_QUERY;
import static shiver.me.timbers.webservice.stub.api.Equals.RED_HEADERS;
import static shiver.me.timbers.webservice.stub.api.Equals.RED_QUERY;

public class StubbingTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_stubbing() {
        new Stubbing();
        new Stubbing(mock(StubRequest.class), mock(StubResponse.class));
    }

    @Test
    public void Is_a_valid_bean() {
        assertThat(Stubbing.class, hasValidGettersAndSetters());
    }

    @Test
    public void Has_equality() {
        EqualsVerifier.forClass(Stubbing.class)
            .usingGetClass()
            .withPrefabValues(StubQuery.class, RED_QUERY, BLACK_QUERY)
            .withPrefabValues(StubHeaders.class, RED_HEADERS, BLACK_HEADERS)
            .suppress(NONFINAL_FIELDS)
            .verify();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Has_a_to_string() {

        // Given
        final StubRequest<Object, StubRequest> request = mock(StubRequest.class);
        final StubResponse<Object, StubResponse> response = mock(StubResponse.class);

        // When
        final String actual = new Stubbing<>(request, response).toString();

        // Then
        assertThat(actual, allOf(containsString(request.toString()), containsString(response.toString())));
    }
}
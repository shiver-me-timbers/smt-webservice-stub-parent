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

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NoOpBodyCleanerTest {

    private NoOpBodyCleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new NoOpBodyCleaner();
    }

    @Test
    public void Cannot_support_anything() {

        // When
        final Throwable actual = catchThrowable(() -> cleaner.supports(mock(StringStubRequest.class)));

        // Then
        assertThat(actual, instanceOf(UnsupportedOperationException.class));
    }

    @Test
    public void Will_not_change_the_request() {

        // Given
        final StringStubRequest expected = mock(StringStubRequest.class);

        // When
        final StringStubRequest actual = cleaner.cleanBody(expected);

        // Then
        verifyZeroInteractions(expected);
        assertThat(actual, is(expected));
    }
}
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

import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubContentTypeTest {

    private MimeTypes mimeTypes;
    private String mimeType;
    private StubContentType contentType;

    @Before
    public void setUp() {
        mimeTypes = mock(MimeTypes.class);
        mimeType = someString();
        contentType = new StubContentType(mimeTypes, singletonList(mimeType));
    }

    @Test
    public void Can_check_if_the_content_type_is_for_XML() {

        final Boolean expected = someBoolean();

        // Given
        given(mimeTypes.containsSubType(mimeType, "xml")).willReturn(expected);

        // When
        final boolean actual = contentType.isXml();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_check_if_the_content_type_is_for_JSON() {

        final Boolean expected = someBoolean();

        // Given
        given(mimeTypes.containsSubType(mimeType, "json")).willReturn(expected);

        // When
        final boolean actual = contentType.isJson();

        // Then
        assertThat(actual, is(expected));
    }
}
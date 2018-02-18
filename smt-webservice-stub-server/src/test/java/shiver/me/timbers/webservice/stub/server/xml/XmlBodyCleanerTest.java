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

package shiver.me.timbers.webservice.stub.server.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.server.cleaning.MapKeyFilter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;

public class XmlBodyCleanerTest {

    private XmlBodyCleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new XmlBodyCleaner(mock(XmlMapper.class), mock(MapKeyFilter.class));
    }

    @Test
    public void Can_support_JSON_requests() {

        final StubContentType contentType = mock(StubContentType.class);

        final boolean expected = someBoolean();

        // Given
        given(contentType.isXml()).willReturn(expected);

        // When
        final boolean actual = cleaner.isCorrectSubtype(contentType);

        // Then
        assertThat(actual, is(expected));
    }
}
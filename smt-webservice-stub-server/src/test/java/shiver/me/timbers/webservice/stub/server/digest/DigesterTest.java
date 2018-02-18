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

package shiver.me.timbers.webservice.stub.server.digest;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.cleaning.Cleaner;

import java.security.MessageDigest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DigesterTest {

    private Cleaner cleaner;
    private MessageDigestFactory factory;
    private Digester digester;
    private Encoding encoding;

    @Before
    public void setUp() {
        cleaner = mock(Cleaner.class);
        factory = mock(MessageDigestFactory.class);
        encoding = mock(Encoding.class);
        digester = new Digester(cleaner, factory, encoding);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_hash_from_a_string() {

        final StringStubRequest request = mock(StringStubRequest.class);

        final StringStubRequest cleanHeadersRequest = mock(StringStubRequest.class);
        final StringStubRequest cleanBodyRequest = mock(StringStubRequest.class);
        final String requestString = someString();
        final MessageDigest digest = mock(MessageDigest.class);

        final byte[] bytes = someString().getBytes();
        final String expected = someString();

        // Given
        given(cleaner.cleanBody(request)).willReturn(cleanBodyRequest);
        given(cleaner.cleanHeaders(cleanBodyRequest)).willReturn(cleanHeadersRequest);
        given(cleaner.toCleanString(cleanHeadersRequest)).willReturn(requestString);
        given(factory.create("MD5")).willReturn(digest);
        given(digest.digest(requestString.getBytes())).willReturn(bytes);
        given(encoding.toHex(bytes)).willReturn(expected);

        // When
        final String actual = digester.digestRequest(request);

        // Then
        assertThat(actual, is(expected));
    }
}
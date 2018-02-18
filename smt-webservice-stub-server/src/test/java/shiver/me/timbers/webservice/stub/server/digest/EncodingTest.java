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

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomBytes.someBytes;

public class EncodingTest {

    @Test
    public void Instantiation_for_coverage() {
        new Encoding();
    }

    @Test
    public void Can_encode_bytes_to_a_hex_string() {

        // Given
        final byte[] bytes = someBytes();

        // When
        final String actual = new Encoding().toHex(bytes);

        // Then
        assertThat(actual, equalTo(Hex.encodeHexString(bytes)));
    }
}
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

package shiver.me.timbers.webservice.stub.server;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerBetween;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class IOStreamsTest {

    private IOStreams ioStreams;

    @Before
    public void setUp() {
        ioStreams = new IOStreams();
    }

    @Test
    public void Can_buffer_an_input_stream() throws IOException {

        // Given
        final String expected = someString();

        // When
        final BufferedInputStream actual = ioStreams.buffer(new ByteArrayInputStream(expected.getBytes()));

        // Then
        assertThat(actual, instanceOf(BufferedInputStream.class));
        assertThat(IOUtils.toString(actual, "UTF-8"), equalTo(expected));
    }

    @Test
    public void Will_not_buffer_a_buffered_input_stream() {

        // Given
        final BufferedInputStream expected = mock(BufferedInputStream.class);

        // When
        final BufferedInputStream actual = ioStreams.buffer(expected);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_read_some_bytes_from_a_stream() throws IOException {

        final InputStream stream = mock(InputStream.class);
        final int byteNum = someIntegerBetween(1, 1024);
        final String expected = someString(byteNum);

        // Given
        given(stream.read(new byte[byteNum])).will((Answer<Integer>) invocationOnMock -> {
            final byte[] buffer = invocationOnMock.getArgument(0);
            final byte[] bytes = expected.getBytes();
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            return bytes.length;
        });

        // When
        final String actual = ioStreams.readBytesToString(stream, byteNum);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_read_a_stream_into_a_string() throws IOException {

        // Given
        final String expected = someString();

        // When
        final String actual = ioStreams.toString(new ByteArrayInputStream(expected.getBytes()));

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_read_a_stream_into_a_string() throws IOException {

        final InputStream stream = spy(new ByteArrayInputStream(someString().getBytes()));

        final IOException exception = mock(IOException.class);

        // Given
        willThrow(exception).given(stream).close();

        // When
        final Throwable actual = catchThrowable(() -> ioStreams.toString(stream));

        // Then
        assertThat(actual, is(exception));
    }

    @Test
    public void Can_convert_a_string_into_a_stream() throws IOException {

        // Given
        final String expected = someString();

        // When
        final InputStream actual = ioStreams.toStream(expected);

        // Then
        assertThat(IOUtils.toString(actual, "UTF-8"), is(expected));
    }
}
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

package shiver.me.timbers.webservice.stub.server.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.webservice.stub.server.RepositoryException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class FileStubRepositoryTest {

    private String directory;
    private FileRepository fileRepository;
    private FileStubRepository repository;

    @Before
    public void setUp() throws IOException {
        directory = someString();
        fileRepository = mock(FileRepository.class);
        repository = new FileStubRepository(mock(ObjectMapper.class), directory, mock(Clock.class), fileRepository);
    }

    @Test
    public void Can_create_the_repository_directory() throws IOException {

        // Then
        then(fileRepository).should().mkdirs(directory);
    }

    @Test
    public void Can_save_a_stub_with_a_path() throws IOException {

        // Given
        final String path = someString();
        final String content = someString();

        // When
        repository.saveWithPath(path, content);

        // Then
        then(fileRepository).should().save(path, content);
    }

    @Test
    public void Can_fail_to_save_a_stub_with_a_path() throws IOException {

        final String path = someString();
        final String content = someString();

        final IOException exception = mock(IOException.class);

        // Given
        willThrow(exception).given(fileRepository).save(path, content);

        // When
        final Throwable actual = catchThrowable(() -> repository.saveWithPath(path, content));

        // Then
        assertThat(actual, instanceOf(RepositoryException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to save stub request with path (%s).", path)));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_find_a_stubbed_response_by_a_path() throws IOException {

        final String path = someString();

        final InputStream expected = mock(InputStream.class);

        // Given
        given(fileRepository.openFile(path)).willReturn(expected);

        // When
        final InputStream actual = repository.findResponseByPath(path);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_find_a_call_to_a_stub_by_a_path() throws IOException {

        final String path = someString();

        final Path path1 = mock(Path.class);
        final Path path2 = mock(Path.class);
        final Path path3 = mock(Path.class);
        final Path absolutePath1 = mock(Path.class);
        final Path absolutePath2 = mock(Path.class);
        final Path absolutePath3 = mock(Path.class);
        final String call1 = someString();
        final String call2 = someString();
        final String call3 = someString();

        // Given
        given(fileRepository.list(path)).willReturn(asList(path1, path2, path3));
        given(path1.toAbsolutePath()).willReturn(absolutePath1);
        given(path2.toAbsolutePath()).willReturn(absolutePath2);
        given(path3.toAbsolutePath()).willReturn(absolutePath3);
        given(absolutePath1.toString()).willReturn(call1);
        given(absolutePath2.toString()).willReturn(call2);
        given(absolutePath3.toString()).willReturn(call3);

        // When
        final List<String> actual = repository.findCallsByPath(path);

        // Then
        assertThat(actual, contains(call1, call2, call3));
    }

    @Test
    public void Can_record_a_call_to_the_stub_with_a_path() throws IOException {

        // Given
        final String path = someString();
        final String content = someString();

        // When
        repository.recordCallWithPath(path, content);

        // Then
        then(fileRepository).should().save(path, content);
    }
}
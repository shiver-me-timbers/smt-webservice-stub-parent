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

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.lang.String.format;
import static java.nio.file.FileVisitResult.CONTINUE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphaString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class FileRepositoryTest {

    private String base;
    private String directory;
    private FileRepository repository;

    @Before
    public void setUp() {
        base = format("target%1$s%2$s%1$s", File.separator, someAlphaString(5));
        directory = format("%s%s%s", base, someAlphaString(8), File.separator);
        repository = new FileRepository();
    }

    @After
    public void tearDown() throws IOException {
        Files.walkFileTree(Paths.get(base), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return CONTINUE;
            }
        });
    }

    @Test
    public void Can_make_some_directories() throws IOException {

        // When
        repository.mkdirs(directory);

        // Then
        assertThat(Files.exists(Paths.get(directory)), is(true));
    }

    @Test
    public void Can_save_a_file() throws IOException {

        final String filePath = directory + someAlphaString(13);
        final String content = someString();

        // Given
        repository.mkdirs(directory);

        // When
        repository.save(filePath, content);

        // Then
        assertThat(Files.exists(Paths.get(filePath)), is(true));
        assertThat(new String(Files.readAllBytes(Paths.get(filePath))), equalTo(content));
    }

    @Test
    public void Can_open_a_file() throws IOException {

        final String filePath = directory + someAlphaString(13);
        final String content = someString();

        // Given
        repository.mkdirs(directory);
        repository.save(filePath, content);

        // When
        final InputStream actual = repository.openFile(filePath);

        // Then
        assertThat(IOUtils.toString(actual, "UTF-8"), equalTo(content));
    }

    @Test
    public void Can_list_all_the_files_in_a_directory() throws IOException {

        final String prefix = someAlphaString(3);
        final String filePath1 = directory + prefix + someAlphaString(5);
        final String filePath2 = directory + "do_not_list" + someAlphaString(8);
        final String filePath3 = directory + prefix + someAlphaString(13);
        final String filePath4 = directory + prefix + someAlphaString(21);

        // Given
        repository.mkdirs(directory);
        repository.save(filePath1, someString());
        repository.save(filePath2, someString());
        repository.save(filePath3, someString());
        repository.save(filePath4, someString());

        // When
        final List<Path> actual = repository.list(directory + prefix);

        // Then
        assertThat(actual, containsInAnyOrder(path(filePath1), path(filePath3), path(filePath4)));
    }

    private static Path path(String path) {
        return Paths.get(path);
    }
}
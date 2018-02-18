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

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author Karl Bennett
 */
@Component
public class FileRepository {

    public void mkdirs(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }

    public void save(String filePath, String content) throws IOException {
        Files.write(Files.createFile(Paths.get(filePath)), content.getBytes());
    }

    public InputStream openFile(String path) throws IOException {
        return Files.newInputStream(Paths.get(path));
    }

    public List<Path> list(String path) throws IOException {
        final Path query = Paths.get(path);
        final Path directory = query.getParent();
        final Path prefix = query.getFileName();
        return Files.list(directory).filter(hasPrefix(prefix)).collect(toList());
    }

    private static Predicate<Path> hasPrefix(Path prefix) {
        return path -> path.getFileName().toString().startsWith(prefix.toString());
    }
}

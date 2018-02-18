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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shiver.me.timbers.webservice.stub.server.RepositoryException;
import shiver.me.timbers.webservice.stub.server.StubRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Karl Bennett
 */
@Component
public class FileStubRepository extends StubRepository {

    private final FileRepository fileRepository;

    public FileStubRepository(
        ObjectMapper mapper,
        @Value("${soap.stubber.directory:target/SoapStub/}") String directory,
        Clock clock,
        FileRepository fileRepository
    ) throws IOException {
        super(mapper, directory, clock);
        this.fileRepository = fileRepository;
        fileRepository.mkdirs(directory);
    }

    @Override
    protected void saveWithPath(String path, String content) {
        try {
            fileRepository.save(path, content);
        } catch (IOException e) {
            throw new RepositoryException(format("Failed to save stub request with path (%s).", path), e);
        }
    }

    @Override
    protected InputStream findResponseByPath(String path) throws IOException {
        return fileRepository.openFile(path);
    }

    @Override
    protected List<String> findCallsByPath(String path) throws IOException {
        return fileRepository.list(path).stream().map(Path::toAbsolutePath).map(Path::toString).collect(toList());
    }

    @Override
    protected void recordCallWithPath(String path, String content) throws IOException {
        fileRepository.save(path, content);
    }
}

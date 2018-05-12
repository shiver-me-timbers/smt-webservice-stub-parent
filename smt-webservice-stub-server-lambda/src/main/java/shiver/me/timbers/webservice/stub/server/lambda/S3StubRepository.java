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

package shiver.me.timbers.webservice.stub.server.lambda;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import shiver.me.timbers.webservice.stub.server.StubRepository;

import java.io.InputStream;
import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

class S3StubRepository extends StubRepository {

    private final Logger log = Logger.getLogger(getClass());

    private final String bucketName;
    private final AmazonS3 s3;

    S3StubRepository(ObjectMapper mapper, String directory, Clock clock, String bucketName, AmazonS3 s3) {
        super(mapper, directory, clock);
        this.bucketName = bucketName;
        this.s3 = s3;
    }

    @Override
    protected void saveWithPath(String path, String content) {
        log.info(format("Saving the stubbing to the (%s) S3 bucket with the key (%s).", bucketName, path));
        s3.putObject(bucketName, path, content);
    }

    @Override
    protected InputStream findResponseByPath(String path) {
        log.info(format("Getting object from bucket (%s) with key (%s).", bucketName, path));
        return s3.getObject(bucketName, path).getObjectContent();
    }

    @Override
    protected List<String> findCallsByPath(String path) {
        log.info(format("Getting calls from bucket (%s) that start with (%s).", bucketName, path));
        return s3.listObjects(bucketName, path).getObjectSummaries().stream().map(S3ObjectSummary::getKey)
            .collect(toList());
    }

    @Override
    protected void recordCallWithPath(String path, String content) {
        log.info(format("Recording a call in bucket (%s) with key (%s).", bucketName, path));
        s3.putObject(bucketName, path, content);
    }
}

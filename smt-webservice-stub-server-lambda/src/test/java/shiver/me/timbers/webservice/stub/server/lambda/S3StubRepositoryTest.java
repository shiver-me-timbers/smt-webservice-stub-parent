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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.time.Clock;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class S3StubRepositoryTest {

    private String bucketName;
    private AmazonS3 s3;
    private S3StubRepository repository;

    @Before
    public void setUp() {
        bucketName = someString();
        s3 = mock(AmazonS3.class);
        repository = new S3StubRepository(mock(ObjectMapper.class), someString(), mock(Clock.class), bucketName, s3);
    }

    @Test
    public void Can_save_a_stub_with_a_path() {

        // Given
        final String name = someString();
        final String content = someString();

        // When
        repository.saveWithPath(name, content);

        // Then
        then(s3).should().putObject(bucketName, name, content);
    }

    @Test
    public void Can_find_a_stubbed_response_by_a_path() {

        final String path = someString();

        final S3Object s3Object = mock(S3Object.class);
        final S3ObjectInputStream expected = mock(S3ObjectInputStream.class);

        // Given
        given(s3.getObject(bucketName, path)).willReturn(s3Object);
        given(s3Object.getObjectContent()).willReturn(expected);

        // When
        final InputStream actual = repository.findResponseByPath(path);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_find_a_call_to_a_stub_by_a_path() {

        final String path = someString();

        final ObjectListing objects = mock(ObjectListing.class);
        final S3ObjectSummary summary1 = mock(S3ObjectSummary.class);
        final S3ObjectSummary summary2 = mock(S3ObjectSummary.class);
        final S3ObjectSummary summary3 = mock(S3ObjectSummary.class);
        final String call1 = someString();
        final String call2 = someString();
        final String call3 = someString();

        // Given
        given(s3.listObjects(bucketName, path)).willReturn(objects);
        given(objects.getObjectSummaries()).willReturn(asList(summary1, summary2, summary3));
        given(summary1.getKey()).willReturn(call1);
        given(summary2.getKey()).willReturn(call2);
        given(summary3.getKey()).willReturn(call3);

        // When
        final List<String> actual = repository.findCallsByPath(path);

        // Then
        assertThat(actual, contains(call1, call2, call3));
    }

    @Test
    public void Can_record_a_call_to_the_stub_with_a_path() {

        // Given
        final String path = someString();
        final String content = someString();

        // When
        repository.recordCallWithPath(path, content);

        // Then
        then(s3).should().putObject(bucketName, path, content);
    }
}
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
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import shiver.me.timbers.webservice.stub.server.Env;
import shiver.me.timbers.webservice.stub.server.StubRepository;
import shiver.me.timbers.webservice.stub.server.digest.Digester;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.digester;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.env;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.jsonMapper;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.repository;
import static shiver.me.timbers.webservice.stub.server.lambda.StubLambdaSetup.xmlMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AmazonS3ClientBuilder.class)
public class StubLambdaSetupTest {

    @Test
    public void Instantiation_for_coverage() {
        new StubLambdaSetup();
    }

    @Test
    public void Can_create_a_singleton_json_mapper() {

        // When
        final ObjectMapper actual = jsonMapper();
        final ObjectMapper expected = jsonMapper();

        // Then
        assertThat(actual, not(nullValue()));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_singleton_xml_mapper() {

        // When
        final XmlMapper actual = xmlMapper();
        final XmlMapper expected = xmlMapper();

        // Then
        assertThat(actual, not(nullValue()));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_singleton_env() {

        // When
        final Env actual = env();
        final Env expected = env();

        // Then
        assertThat(actual, not(nullValue()));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_singleton_repository() {

        PowerMockito.mockStatic(AmazonS3ClientBuilder.class);

        // Given
        given(AmazonS3ClientBuilder.defaultClient()).willReturn(mock(AmazonS3.class));

        // When
        final StubRepository actual = repository();
        final StubRepository expected = repository();

        // Then
        assertThat(actual, not(nullValue()));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_singleton_digester() {

        // When
        final Digester actual = digester();
        final Digester expected = digester();

        // Then
        assertThat(actual, not(nullValue()));
        assertThat(actual, is(expected));
    }
}
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

package shiver.me.timbers.webservice.stub.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shiver.me.timbers.webservice.stub.client.GivenStub;
import shiver.me.timbers.webservice.stub.client.ThenStub;
import shiver.me.timbers.webservice.stub.client.spring.SpringBootTestUrl;

import javax.ws.rs.client.Client;

import static javax.ws.rs.client.Entity.xml;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.client.xml.api.XmlStubRequest.xmlReq;
import static shiver.me.timbers.webservice.stub.client.xml.api.XmlStubResponse.xmlRes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestStubApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("ignorefields")
public class ITXmlStubIgnoreFields {

    @Autowired
    @Qualifier("stubUrl")
    private SpringBootTestUrl stubUrl;

    @Autowired
    private GivenStub given;

    @Autowired
    private ThenStub then;

    @Autowired
    private Client client;

    @Test
    public void Can_ignore_fields_in_a_stubbed_XML_request() {

        final String one = someAlphanumericString(8);

        final TestObject request = new TestObject(one, new TestChild(someInteger()));

        final TestResponse expected = new TestResponse(someString());

        // Given
        given.request(xmlReq(request)).willRespond(xmlRes(expected));

        // When
        final TestResponse actual = client.target(stubUrl.toString()).request(APPLICATION_XML_TYPE)
            .post(xml(new TestObject(one, new TestChild(someInteger()))), TestResponse.class);

        // Then
        assertThat(actual, equalTo(expected));
        then.request(xmlReq(request)).should().beCalled();
    }
}

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

import org.junit.After;
import org.junit.Before;
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
import shiver.me.timbers.webservice.stub.client.soap.example.Argument1;
import shiver.me.timbers.webservice.stub.client.soap.example.Argument2;
import shiver.me.timbers.webservice.stub.client.soap.example.Argument3;
import shiver.me.timbers.webservice.stub.client.soap.example.ExampleClient;
import shiver.me.timbers.webservice.stub.client.soap.example.ExampleResponse;
import shiver.me.timbers.webservice.stub.client.soap.example.jaxws.ExampleMethod;
import shiver.me.timbers.webservice.stub.client.soap.example.jaxws.ExampleMethodResponse;
import shiver.me.timbers.webservice.stub.client.spring.SpringBootTestUrl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapStubRequest.soapReq;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapStubResponse.soapRes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestStubApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("ignorefields")
public class ITSoapStubIgnoreTags {

    @Autowired
    @Qualifier("stubUrl")
    private SpringBootTestUrl stubUrl;

    @Autowired
    private GivenStub given;

    @Autowired
    private ThenStub then;

    private ExampleClient service;

    @Before
    public void setUp() {
        service = new ExampleClient(stubUrl.toString());
    }

    @After
    public void tearDown() {
        service.clearHeaders();
    }

    @Test
    public void Can_ignore_tags_in_a_stubbed_SOAP_request() {

        final ExampleMethod request = new ExampleMethod();
        final ExampleMethodResponse response = new ExampleMethodResponse();

        final Argument1 argument1 = new Argument1(someString());

        final ExampleResponse expected = new ExampleResponse(someString());

        // Given
        request.setArgument1(argument1);
        request.setArgument2(someArgument2());
        response.setExampleResponse(expected);
        given.request(soapReq(request)).willRespond(soapRes(response));

        // When
        final ExampleResponse actual = service.exampleMethod(argument1, someArgument2());

        // Then
        assertThat(actual, equalTo(expected));
        then.request(soapReq(request)).should().beCalled();
    }

    private static Argument2 someArgument2() {
        return new Argument2(someString(), new Argument3(someString()));
    }
}

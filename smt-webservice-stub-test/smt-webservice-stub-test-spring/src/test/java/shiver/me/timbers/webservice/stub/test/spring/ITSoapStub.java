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

import javax.xml.ws.WebServiceException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static shiver.me.timbers.data.random.RandomStrings.someAlphaString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapHeader.sH;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapStubRequest.soapReq;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapStubResponse.soapRes;
import static shiver.me.timbers.webservice.stub.client.soap.example.SoapHeader.soapH;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestStubApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ITSoapStub {

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
    public void Can_stub_a_SOAP_request() {

        final ExampleMethod request = new ExampleMethod();
        final ExampleMethodResponse response = new ExampleMethodResponse();

        final Argument1 argument1 = new Argument1(someString());
        final Argument2 argument2 = new Argument2(someString(), new Argument3(someString()));
        final ExampleResponse expected = new ExampleResponse(someString());

        // Given
        request.setArgument1(argument1);
        request.setArgument2(argument2);
        response.setExampleResponse(expected);
        given.request(soapReq(request)).willRespond(soapRes(response));

        // When
        final ExampleResponse actual = service.exampleMethod(argument1, argument2);

        // Then
        assertThat(actual, equalTo(expected));
        then.request(soapReq(request)).should().beCalled();
    }

    @Test
    public void Can_fail_to_match_a_stubbed_a_SOAP_request() {

        final ExampleMethod request = new ExampleMethod();
        final ExampleMethodResponse response = new ExampleMethodResponse();

        final Argument2 argument2 = new Argument2(someString(), new Argument3(someString()));
        final ExampleResponse expected = new ExampleResponse(someString());

        // Given
        request.setArgument1(new Argument1(someString()));
        request.setArgument2(argument2);
        response.setExampleResponse(expected);
        given.request(soapReq(request)).willRespond(soapRes(response));

        // When
        final Throwable actual = catchThrowable(() -> service.exampleMethod(new Argument1(someString()), argument2));

        // Then
        assertThat(actual, instanceOf(WebServiceException.class));
    }

    @Test
    public void Can_stub_a_soap_request_with_SOAP_headers() {

        final ExampleMethod request = new ExampleMethod();
        final ExampleMethodResponse response = new ExampleMethodResponse();

        final Argument1 argument1 = new Argument1(someString());
        final Argument2 argument2 = new Argument2(someString(), new Argument3(someString()));
        final ExampleResponse expected = new ExampleResponse(someString());
        final String hName1 = someAlphaString(3);
        final String hName2 = someAlphaString(5);
        final String hName3 = someAlphaString(8);
        final String hValue1 = someString();
        final String hValue2 = someString();
        final String hValue3 = someString();

        // Given
        request.setArgument1(argument1);
        request.setArgument2(argument2);
        response.setExampleResponse(expected);
        given.request(soapReq(request).withSoapHeaders(sH(hName1, hValue1), sH(hName2, hValue2), sH(hName3, hValue3)))
            .willRespond(soapRes(response));

        // When
        service.addHeaders(soapH(hName1, hValue1), soapH(hName2, hValue2), soapH(hName3, hValue3));
        final ExampleResponse actual = service.exampleMethod(argument1, argument2);

        // Then
        assertThat(actual, equalTo(expected));
        then.request(soapReq(request).withSoapHeaders(sH(hName1, hValue1), sH(hName2, hValue2), sH(hName3, hValue3)))
            .should().beCalled();
    }
}

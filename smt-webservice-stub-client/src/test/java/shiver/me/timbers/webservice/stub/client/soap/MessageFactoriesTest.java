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

package shiver.me.timbers.webservice.stub.client.soap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.webservice.stub.client.soap.MessageFactories.messageFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageFactory.class)
public class MessageFactoriesTest {

    @Test
    public void Instantiation_for_coverage() {
        new MessageFactories();
    }

    @Test
    public void Can_create_a_message_factory() throws SOAPException {

        PowerMockito.mockStatic(MessageFactory.class);

        final MessageFactory expected = mock(MessageFactory.class);

        // Given
        given(MessageFactory.newInstance()).willReturn(expected);

        // When
        final MessageFactory actual = messageFactory();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_create_a_message_factory() throws SOAPException {

        PowerMockito.mockStatic(MessageFactory.class);

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(MessageFactory.newInstance()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(MessageFactories::messageFactory);

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create the SOAP MessageFactory."));
        assertThat(actual.getCause(), is(exception));
    }
}
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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.String.format;
import static shiver.me.timbers.webservice.stub.client.soap.MessageFactories.messageFactory;

public class SoapMessageFactory {

    private final MessageFactory factory;

    public SoapMessageFactory() {
        this(messageFactory());
    }

    public SoapMessageFactory(MessageFactory factory) {
        this.factory = factory;
    }

    public SOAPMessage createMessage() throws SOAPException {
        return factory.createMessage();
    }

    public SOAPMessage createMessage(String soapRequestXml) throws SOAPException {
        try {
            return factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(soapRequestXml.getBytes()));
        } catch (IOException e) {
            throw new SOAPException(format("Failed to create a SOAPMessage from: %s", soapRequestXml), e);
        }
    }
}

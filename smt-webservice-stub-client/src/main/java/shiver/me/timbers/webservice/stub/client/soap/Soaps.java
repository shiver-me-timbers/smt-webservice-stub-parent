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

import shiver.me.timbers.webservice.stub.client.soap.api.SoapHeader;

import javax.xml.soap.SOAPMessage;
import java.util.Set;

public class Soaps {

    private final Marshals marshals;
    private final SoapMessages messages;

    public Soaps(Marshals marshals, SoapMessages messages) {
        this.marshals = marshals;
        this.messages = messages;
    }

    public SOAPMessage marshalToSOAPMessage(Object object, Set<SoapHeader> headers) {
        return messages.wrapInSoapEnvelope(marshals.marshalToDocument(object), headers);
    }

    public String toXmlString(SOAPMessage requestMessage) {
        return messages.toXmlString(requestMessage);
    }
}

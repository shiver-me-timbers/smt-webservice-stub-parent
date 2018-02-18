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

import org.w3c.dom.Document;
import shiver.me.timbers.webservice.stub.client.soap.api.SoapHeader;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class SoapMessages {

    private final SoapMessageFactory messageFactory;
    private final SoapHeaders soapHeaders;

    public SoapMessages(SoapMessageFactory messageFactory, SoapHeaders soapHeaders) {
        this.messageFactory = messageFactory;
        this.soapHeaders = soapHeaders;
    }

    public SOAPMessage wrapInSoapEnvelope(Document document, Set<SoapHeader> headers) {
        try {
            final SOAPMessage message = messageFactory.createMessage();
            addHeaders(document, message.getSOAPHeader(), headers);
            message.getSOAPBody().addDocument(document);
            return message;
        } catch (SOAPException e) {
            throw new SoapException("Failed to wrap the document in a SOAP envelope.", e);
        }
    }

    public String toXmlString(SOAPMessage message) {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            message.writeTo(stream);
            return stream.toString();
        } catch (SOAPException | IOException e) {
            throw new SoapException("Failed to convert the SOAP message to a string.", e);
        }
    }

    private void addHeaders(Document document, SOAPHeader soapHeader, Set<SoapHeader> headers) {
        if (headers.isEmpty()) {
            soapHeader.detachNode(); // Clean up the empty header tag.
            return;
        }
        final String namespace = soapHeaders.extractNamespace(document);
        headers.forEach(header -> soapHeaders.addHeader(soapHeader, namespace, header));
    }
}

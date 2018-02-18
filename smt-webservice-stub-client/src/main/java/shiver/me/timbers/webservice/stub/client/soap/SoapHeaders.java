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

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public class SoapHeaders {

    public String extractNamespace(Document document) {
        return document.getDocumentElement().getNamespaceURI();
    }

    public void addHeader(SOAPHeader soapHeader, String namespace, SoapHeader header) {
        try {
            soapHeader.addHeaderElement(new QName(namespace, header.getKey())).addTextNode(header.getValue());
        } catch (SOAPException e) {
            throw new SoapException(format("Failed to add SOAP header (%s:%s).", header.getKey(), header.getValue()), e);
        }
    }
}

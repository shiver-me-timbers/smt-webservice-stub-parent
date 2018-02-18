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

package shiver.me.timbers.webservice.stub.client.soap.example;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

/**
 * @author Karl Bennett
 */
public class SoapHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private final String namespace;
    private final Set<SoapHeader> headers;

    public SoapHeaderHandler(String namespace, Set<SoapHeader> headers) {
        this.namespace = namespace;
        this.headers = headers;
    }

    public void addSoapHeaders(SoapHeader... headers) {
        Arrays.stream(headers).collect(toCollection(() -> this.headers));
    }

    public void clearSoapHeaders() {
        headers.clear();
    }

    @Override
    public Set<QName> getHeaders() {
        return new HashSet<>();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        if (headers.isEmpty()) {
            return true;
        }
        try {
            final SOAPHeader soapHeader = context.getMessage().getSOAPHeader();
            for (SoapHeader header : headers) {
                soapHeader.addHeaderElement(new QName(namespace, header.getKey())).addTextNode(header.getValue());
            }
        } catch (SOAPException e) {
            throw new SoapHeaderException("Failed to add SOAP headers.", e);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {
        // No need to close adding headers.
    }
}

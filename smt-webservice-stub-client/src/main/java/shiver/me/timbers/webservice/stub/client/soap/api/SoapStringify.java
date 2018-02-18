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

package shiver.me.timbers.webservice.stub.client.soap.api;

import shiver.me.timbers.webservice.stub.api.Stringify;
import shiver.me.timbers.webservice.stub.client.soap.DocumentFactory;
import shiver.me.timbers.webservice.stub.client.soap.JaxbContextFactory;
import shiver.me.timbers.webservice.stub.client.soap.Marshals;
import shiver.me.timbers.webservice.stub.client.soap.SoapHeaders;
import shiver.me.timbers.webservice.stub.client.soap.SoapMessageFactory;
import shiver.me.timbers.webservice.stub.client.soap.SoapMessages;
import shiver.me.timbers.webservice.stub.client.soap.Soaps;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author Karl Bennett
 */
public class SoapStringify implements Stringify {

    public static SoapStringify soapStringify() {
        return new SoapStringify(
            new Soaps(
                new Marshals(new DocumentFactory(DocumentBuilderFactory.newInstance()), new JaxbContextFactory()),
                new SoapMessages(new SoapMessageFactory(), new SoapHeaders())
            )
        );
    }

    private final Soaps soaps;
    private final Set<SoapHeader> headers;

    SoapStringify(Soaps soaps) {
        this.soaps = soaps;
        this.headers = new HashSet<>();
    }

    @Override
    public String toString(Object object) {
        return soaps.toXmlString(soaps.marshalToSOAPMessage(object, headers));
    }

    public void addSoapHeaders(SoapHeader... headers) {
        this.headers.addAll(asList(headers));
    }
}

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

package shiver.me.timbers.webservice.stub.client.xml.api;

import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.api.StubResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static shiver.me.timbers.webservice.stub.client.xml.api.XmlStringify.xmlStringify;

/**
 * @author Karl Bennett
 */
public class XmlStubResponse<T> extends StubResponse<T, XmlStubResponse<T>> {

    public static XmlStubResponse<Void> xmlRes() {
        return new XmlStubResponse<>();
    }

    public static <T> XmlStubResponse<T> xmlRes(T body) {
        return new XmlStubResponse<>(body);
    }

    public XmlStubResponse() {
        super(xmlStringify());
        withXmlContentType();
    }

    public XmlStubResponse(T body) {
        super(xmlStringify(), body);
        withXmlContentType();
    }

    private XmlStubResponse(XmlStubResponse<T> request) {
        super(request);
    }

    private void withXmlContentType() {
        withHeaders(new StubContentType(APPLICATION_XML));
    }

    @Override
    public XmlStubResponse<T> copy() {
        return new XmlStubResponse<>(this);
    }
}

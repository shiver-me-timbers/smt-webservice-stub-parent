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
import shiver.me.timbers.webservice.stub.api.StubRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static shiver.me.timbers.webservice.stub.client.xml.api.XmlStringify.xmlStringify;

/**
 * @author Karl Bennett
 */
public class XmlStubRequest<T> extends StubRequest<T, XmlStubRequest> {

    public static XmlStubRequest<Void> xmlReq() {
        return new XmlStubRequest<>();
    }

    public static <T> XmlStubRequest<T> xmlReq(T body) {
        return new XmlStubRequest<>(body);
    }

    public XmlStubRequest() {
        super(xmlStringify());
        withXmlContentType();
    }

    public XmlStubRequest(T body) {
        super(xmlStringify(), body);
        withXmlContentType();
    }

    private void withXmlContentType() {
        withHeaders(new StubContentType(APPLICATION_XML));
    }

    private XmlStubRequest(XmlStubRequest<T> request) {
        super(request);
    }

    @Override
    public XmlStubRequest copy() {
        return new XmlStubRequest<>(this);
    }
}

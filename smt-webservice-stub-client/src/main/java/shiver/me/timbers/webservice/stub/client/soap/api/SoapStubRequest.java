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

import shiver.me.timbers.webservice.stub.api.StubContentType;
import shiver.me.timbers.webservice.stub.api.StubRequest;

import static javax.ws.rs.core.MediaType.TEXT_XML;
import static shiver.me.timbers.webservice.stub.client.soap.api.SoapStringify.soapStringify;

/**
 * @author Karl Bennett
 */
public class SoapStubRequest<T> extends StubRequest<T, SoapStubRequest<T>> {

    private final SoapStringify stringify;

    public static <T> SoapStubRequest<T> soapReq(T request) {
        return new SoapStubRequest<>(request);
    }

    public SoapStubRequest(T request) {
        this(soapStringify(), request);
    }

    SoapStubRequest(SoapStringify stringify, T request) {
        super(stringify, request);
        this.stringify = stringify;
        withHeaders(new StubContentType(TEXT_XML));
    }

    private SoapStubRequest(SoapStubRequest<T> request) {
        super(request);
        this.stringify = request.stringify;
    }

    @Override
    public SoapStubRequest<T> copy() {
        return new SoapStubRequest<>(this);
    }

    public SoapStubRequest<T> withSoapHeaders(SoapHeader... headers) {
        stringify.addSoapHeaders(headers);
        return this;
    }
}

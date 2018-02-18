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

import javax.jws.WebService;

@WebService(
    serviceName = "ExampleService",
    endpointInterface = "shiver.me.timbers.webservice.stub.client.soap.example.ExampleService"
)
public class DelegateExampleService implements ExampleService {

    private final SoapHeaderHandler handler;
    private final ExampleService service;

    public DelegateExampleService() {
        this(null, null);
    }

    public DelegateExampleService(SoapHeaderHandler handler, ExampleService service) {
        this.handler = handler;
        this.service = service;
    }

    public void addHeaders(SoapHeader... headers) {
        handler.addSoapHeaders(headers);
    }

    public void clearHeaders() {
        handler.clearSoapHeaders();
    }

    @Override
    public ExampleResponse exampleMethod(Argument1 argument1, Argument2 argument2) {
        return service.exampleMethod(argument1, argument2);
    }
}

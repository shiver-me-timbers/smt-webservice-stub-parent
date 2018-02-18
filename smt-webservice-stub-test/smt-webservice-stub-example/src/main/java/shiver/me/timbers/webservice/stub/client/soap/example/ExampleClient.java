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

import java.util.HashSet;

public class ExampleClient extends DelegateExampleService {

    public ExampleClient(String endpointUrl) {
        this("http://example.soap.client.stub.webservice.timbers.me.shiver/", endpointUrl);
    }

    public ExampleClient(String namespace, String endpointUrl) {
        this(namespace, new SoapHeaderHandler(namespace, new HashSet<>()), endpointUrl);
    }

    private ExampleClient(String namespace, SoapHeaderHandler handler, String endpointUrl) {
        super(handler, new ExampleServiceFactory(namespace, handler, new ServiceFactory()).create(endpointUrl));
    }
}

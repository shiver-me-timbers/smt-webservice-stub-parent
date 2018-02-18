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

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.List;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

class ExampleServiceFactory {

    private final String namespace;
    private final SoapHeaderHandler handler;
    private final ServiceFactory serviceFactory;

    public ExampleServiceFactory(String namespace, SoapHeaderHandler handler, ServiceFactory serviceFactory) {
        this.namespace = namespace;
        this.handler = handler;
        this.serviceFactory = serviceFactory;
    }

    ExampleService create(String endpointUrl) {
        final ExampleService example = serviceFactory
            .create("META-INF/wsdl/ExampleService.wsdl", namespace, ExampleService.class)
            .getPort(ExampleService.class);
        final BindingProvider bindingProvider = (BindingProvider) example;
        bindingProvider.getRequestContext().put(ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

        final Binding binding = bindingProvider.getBinding();
        final List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(handler);
        binding.setHandlerChain(handlerChain);

        return example;
    }
}

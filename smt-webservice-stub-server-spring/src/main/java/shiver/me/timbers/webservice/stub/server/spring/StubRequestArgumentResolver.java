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

package shiver.me.timbers.webservice.stub.server.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shiver.me.timbers.webservice.stub.server.IOStreams;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Arrays.asList;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toSet;

/**
 * @author Karl Bennett
 */
class StubRequestArgumentResolver implements HandlerMethodArgumentResolver {

    private final IOStreams ioStreams;
    private final String stubContext;

    StubRequestArgumentResolver(IOStreams ioStreams, String stubContext) {
        this.ioStreams = ioStreams;
        this.stubContext = stubContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(StringStubRequest.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer container,
        NativeWebRequest request,
        WebDataBinderFactory factory
    ) throws Exception {
        final HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
        return new StringStubRequest(ioStreams.toString(servletRequest.getInputStream()))
            .withMethod(servletRequest.getMethod())
            .withPath(servletRequest.getServletPath().replaceFirst(stubContext, ""))
            .withQuery(servletRequest.getQueryString())
            .withHeaders(headers(request));
    }

    private static Set<Entry<String, List<String>>> headers(NativeWebRequest request) {
        return StreamSupport.stream(iterable(request.getHeaderNames()).spliterator(), true).map(header(request))
            .collect(toSet());
    }

    private static Function<String, Entry<String, List<String>>> header(NativeWebRequest request) {
        return name -> new SimpleEntry<>(name, asList(request.getHeaderValues(name)));
    }

    private static <T> Iterable<T> iterable(Iterator<T> iterator) {
        return () -> iterator;
    }
}

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

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import shiver.me.timbers.webservice.stub.api.StubHeaders;
import shiver.me.timbers.webservice.stub.api.StubQuery;
import shiver.me.timbers.webservice.stub.server.IOStreams;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.buildSomeString;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.webservice.stub.api.StubHeaders.h;
import static shiver.me.timbers.webservice.stub.api.StubQuery.q;

public class StubRequestArgumentResolverTest {

    private IOStreams ioStreams;
    private String stubContext;
    private StubRequestArgumentResolver resolver;

    @Before
    public void setUp() {
        ioStreams = mock(IOStreams.class);
        stubContext = buildSomeString().thatContainsAlphanumericCharacters().thatContains("/").build();
        resolver = new StubRequestArgumentResolver(ioStreams, stubContext);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_support_a_string_stub_requests() {

        final MethodParameter parameter = mock(MethodParameter.class);

        // Given
        given(parameter.getParameterType()).willReturn((Class) StringStubRequest.class);

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Cannot_support_any_other_types() {

        final MethodParameter parameter = mock(MethodParameter.class);

        // Given
        given(parameter.getParameterType()).willReturn((Class) someThing().getClass());

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_resolve_a_stub_requests() throws Exception {

        final MethodParameter parameter = mock(MethodParameter.class);
        final ModelAndViewContainer container = mock(ModelAndViewContainer.class);
        final NativeWebRequest request = mock(NativeWebRequest.class);
        final WebDataBinderFactory factory = mock(WebDataBinderFactory.class);

        final String hName1 = someString();
        final String hName2 = someString();
        final String hName3 = someString();
        final String hValue1 = someString();
        final String hValue2 = someString();
        final String hValue3 = someString();
        final String hValue4 = someString();
        final String hValue5 = someString();
        final String hValue6 = someString();
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        final String method = someString();
        final String qName1 = someAlphanumericString(3);
        final String qName2 = someAlphanumericString(5);
        final String qValue1 = someAlphanumericString(8);
        final String qValue2 = someAlphanumericString(13);
        final String path = stubContext + someString();
        final String queryString = format("%s=%s&%s=%s", qName1, qValue1, qName2, qValue2);
        final ServletInputStream stream = mock(ServletInputStream.class);
        final String body = someString();

        // Given
        given(request.getHeaderNames()).willReturn(asList(hName1, hName2, hName3).iterator());
        given(request.getHeaderValues(hName1)).willReturn(new String[]{hValue1});
        given(request.getHeaderValues(hName2)).willReturn(new String[]{hValue2, hValue3});
        given(request.getHeaderValues(hName3)).willReturn(new String[]{hValue4, hValue5, hValue6});
        given(request.getNativeRequest(HttpServletRequest.class)).willReturn(servletRequest);
        given(servletRequest.getMethod()).willReturn(method);
        given(servletRequest.getServletPath()).willReturn(path);
        given(servletRequest.getQueryString()).willReturn(queryString);
        given(servletRequest.getInputStream()).willReturn(stream);
        given(ioStreams.toString(stream)).willReturn(body);

        // When
        final StringStubRequest actual = (StringStubRequest) resolver
            .resolveArgument(parameter, container, request, factory);

        // Then
        assertThat(actual.getMethod(), equalTo(method));
        assertThat(actual.getPath(), equalTo(path.replaceFirst(stubContext, "")));
        assertThat(actual.getQuery(), equalTo(new StubQuery(q(qName1, qValue1), q(qName2, qValue2))));
        assertThat(actual.getHeaders(), equalTo(
            new StubHeaders(h(hName1, hValue1), h(hName2, hValue2, hValue3), h(hName3, hValue4, hValue5, hValue6))
        ));
        assertThat(actual.getBody(), equalTo(body));
    }
}
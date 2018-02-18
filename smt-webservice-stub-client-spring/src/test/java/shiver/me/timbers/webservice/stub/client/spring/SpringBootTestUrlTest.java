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

package shiver.me.timbers.webservice.stub.client.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerBetween;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class SpringBootTestUrlTest {

    private String path;
    private ApplicationContext context;
    private SpringBootTestUrl url;

    @Before
    public void setUp() {
        path = "/" + someString();
        context = mock(ApplicationContext.class);
        url = new SpringBootTestUrl(path, "");
    }

    @Test
    public void Can_get_the_spring_boot_test_url() {

        final Environment environment = mock(Environment.class);
        final String port = someString();

        // Given
        url.setApplicationContext(context);
        given(context.getEnvironment()).willReturn(environment);
        given(environment.getProperty("local.server.port")).willReturn(port);

        // When
        final String actual = url.toString();

        // Then
        assertThat(actual, equalTo(url(port)));
    }

    @Test
    public void Can_get_the_overridden_spring_boot_test_url() {

        final Environment environment = mock(Environment.class);

        final String expected = someString();
        final SpringBootTestUrl url = new SpringBootTestUrl(path, expected);

        // Given
        url.setApplicationContext(context);
        given(context.getEnvironment()).willReturn(environment);
        given(environment.getProperty("local.server.port")).willReturn(someString());

        // When
        final String actual = url.toString();

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_get_the_spring_boot_test_url_length() {

        final Environment environment = mock(Environment.class);
        final String port = someString();

        // Given
        url.setApplicationContext(context);
        given(context.getEnvironment()).willReturn(environment);
        given(environment.getProperty("local.server.port")).willReturn(port);

        // When
        final int actual = url.length();

        // Then
        assertThat(actual, equalTo(url(port).length()));
    }

    @Test
    public void Can_get_a_sub_sequence_from_the_spring_boot_test_url() {

        final Environment environment = mock(Environment.class);
        final String port = someString();

        // Given
        url.setApplicationContext(context);
        given(context.getEnvironment()).willReturn(environment);
        given(environment.getProperty("local.server.port")).willReturn(port);
        final int start = someIntegerBetween(0, url.length() - 1);
        final int end = someIntegerBetween(start, url.length() - 1);

        // When
        final CharSequence actual = url.subSequence(start, end);

        // Then
        assertThat(actual, equalTo(url(port).subSequence(start, end)));
    }

    @Test
    public void Can_get_a_char_at_an_index_in_the_spring_boot_test_url() {

        final Environment environment = mock(Environment.class);
        final String port = someString();

        // Given
        url.setApplicationContext(context);
        given(context.getEnvironment()).willReturn(environment);
        given(environment.getProperty("local.server.port")).willReturn(port);
        final int index = someIntegerBetween(0, url.length() - 1);

        // When
        final char actual = url.charAt(index);

        // Then
        assertThat(actual, equalTo(url(port).charAt(index)));
    }

    private String url(String port) {
        return format("http://localhost:%s/%s", port, path);
    }
}
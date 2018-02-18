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

package shiver.me.timbers.webservice.stub.client.soap;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

class Matchers {

    static Matcher<InputStream> hasContent(String expected) {

        return new TypeSafeMatcher<InputStream>() {
            @Override
            protected boolean matchesSafely(InputStream stream) {
                try {
                    final String actual = IOUtils.toString(stream, Charset.forName("UTF-8"));
                    return expected.equals(actual);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void describeTo(Description description) {
                throw new UnsupportedOperationException();
            }
        };
    }
}

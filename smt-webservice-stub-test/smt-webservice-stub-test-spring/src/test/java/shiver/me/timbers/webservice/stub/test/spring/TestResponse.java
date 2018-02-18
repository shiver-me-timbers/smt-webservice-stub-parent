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

package shiver.me.timbers.webservice.stub.test.spring;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author Karl Bennett
 */
@XmlRootElement(name = "TestResponse")
public class TestResponse {

    private String four;

    public TestResponse() {
    }

    public TestResponse(String four) {
        this.four = four;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResponse that = (TestResponse) o;
        return Objects.equals(four, that.four);
    }

    @Override
    public int hashCode() {

        return Objects.hash(four);
    }
}

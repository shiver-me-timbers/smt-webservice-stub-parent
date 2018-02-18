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
@XmlRootElement(name = "TestObject")
public class TestObject {

    private String one;
    private TestChild two;

    public TestObject() {
    }

    public TestObject(String one, TestChild two) {
        this.one = one;
        this.two = two;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public TestChild getTwo() {
        return two;
    }

    public void setTwo(TestChild two) {
        this.two = two;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject that = (TestObject) o;
        return Objects.equals(one, that.one) &&
            Objects.equals(two, that.two);
    }

    @Override
    public int hashCode() {

        return Objects.hash(one, two);
    }
}

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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@XmlRootElement(name = "argument2")
@XmlAccessorType(FIELD)
public class Argument2 {

    private final String value;
    private final Argument3 argument3;

    Argument2() {
        this(null, null);
    }

    public Argument2(String value, Argument3 argument3) {
        this.value = value;
        this.argument3 = argument3;
    }

    public String getValue() {
        return value;
    }

    public Argument3 getArgument3() {
        return argument3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument2 argument2 = (Argument2) o;

        if (value != null ? !value.equals(argument2.value) : argument2.value != null) return false;
        return argument3 != null ? argument3.equals(argument2.argument3) : argument2.argument3 == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (argument3 != null ? argument3.hashCode() : 0);
        return result;
    }
}

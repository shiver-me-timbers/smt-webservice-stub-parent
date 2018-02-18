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

package shiver.me.timbers.webservice.stub.server;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Env {

    private final Logger log = Logger.getLogger(getClass());

    public String get(String name) {
        final String variable = System.getenv(name);
        log.info(format("Retrieved: %s=%s", name, variable));
        return variable;
    }

    public Set<String> getAsSet(String name) {
        return new HashSet<>(getAsList(name));
    }

    public List<String> getAsList(String name) {
        final String variable = System.getenv(name);
        log.info(format("Retrieved: %s=%s", name, variable));
        if (variable == null) {
            return emptyList();
        }
        return asList(variable.split(","));
    }
}

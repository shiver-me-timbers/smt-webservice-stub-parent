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

package shiver.me.timbers.webservice.stub.server.cleaning;

import java.util.Map;
import java.util.Set;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
public class MapKeyFilter {

    private final Set<String> fieldsToIgnore;

    public MapKeyFilter(Set<String> fieldsToIgnore) {
        this.fieldsToIgnore = fieldsToIgnore;
    }

    public Map<String, Object> filterKeys(Map<String, Object> map) {
        return map.entrySet().stream().filter(this::isNotIgnored)
            .map(this::filterValue).collect(toMap(Entry::getKey, Entry::getValue));
    }

    private boolean isNotIgnored(Entry<String, Object> entry) {
        return !fieldsToIgnore.contains(entry.getKey());
    }

    @SuppressWarnings("unchecked")
    private Entry<String, Object> filterValue(Entry<String, Object> entry) {
        final Object value = entry.getValue();
        if (value instanceof Map) {
            return new SimpleEntry<>(entry.getKey(), filterKeys((Map) value));
        }
        return entry;
    }
}

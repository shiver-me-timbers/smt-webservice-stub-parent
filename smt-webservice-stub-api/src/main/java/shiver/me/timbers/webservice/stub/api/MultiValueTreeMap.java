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

package shiver.me.timbers.webservice.stub.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
public class MultiValueTreeMap<K extends Comparable, V> extends TreeMap<K, List<V>> {

    @SafeVarargs
    public static <K extends Comparable, V> Entry<K, List<V>> e(K key, V... values) {
        return e(key, asList(values));
    }

    public static <K extends Comparable, V> Entry<K, List<V>> e(K name, List<V> values) {
        return new SimpleEntry<>(name, values);
    }

    public static <K extends Comparable, V> MultiValueTreeMap<K, V> toMultiTreeMap(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        return new MultiValueTreeMap<>(
            map.entrySet().stream().collect(toMap(Entry::getKey, entry -> singletonList(entry.getValue())))
        );
    }

    public MultiValueTreeMap() {
    }

    @SafeVarargs
    public MultiValueTreeMap(Entry<K, List<V>>... entries) {
        this(entries == null ? null : asList(entries));
    }

    public MultiValueTreeMap(Collection<Entry<K, List<V>>> entries) {
        this(toMultiTreeMap(entries));
    }

    public MultiValueTreeMap(Map<K, List<V>> map) {
        super(map == null ? emptyMap() : map);
    }

    @SuppressWarnings("unchecked")
    public List<V> put(K key, V... values) {
        return super.put(key, asList(values));
    }

    @SuppressWarnings("unchecked")
    public void add(K key, V... values) {
        add(key, asList(values));
    }

    public void add(K key, List<V> values) {
        final List<V> list = get(key);
        if (list == null) {
            put(key, new ArrayList<>(values));
            return;
        }
        list.addAll(values);
    }

    private static <K extends Comparable, V> Map<K, List<V>> toMultiTreeMap(Collection<Entry<K, List<V>>> values) {
        if (values == null) {
            return null;
        }
        final MultiValueTreeMap<K, V> map = new MultiValueTreeMap<>();
        values.forEach(entry -> map.add(entry.getKey(), entry.getValue()));
        return map;
    }
}

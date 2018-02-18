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

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;
import static shiver.me.timbers.webservice.stub.api.StubContentType.CONTENT_TYPE;

/**
 * @author Karl Bennett
 */
public abstract class StubMessage<T, R extends StubMessage> {

    private final Stringify stringify;
    private StubHeaders headers;
    private T body;

    protected StubMessage(StubMessage<T, R> request) {
        this(request.stringify, request.getHeaders(), request.getBody());
    }

    public StubMessage(Stringify stringify, StubHeaders headers, T body) {
        this.stringify = stringify;
        this.headers = headers;
        this.body = body;
    }

    public StubHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(StubHeaders headers) {
        this.headers = headers;
    }

    @SuppressWarnings("unchecked")
    public R withHeaders(Entry<String, List<String>>... headers) {
        return withHeaders(new StubHeaders(headers));
    }

    public R withHeaders(Set<Entry<String, List<String>>> headers) {
        return withHeaders(new StubHeaders(headers));
    }

    public R withHeaders(Map<String, List<String>> headers) {
        return withHeaders(new StubHeaders(headers));
    }

    @SuppressWarnings("unchecked")
    public R withHeaders(StubHeaders headers) {
        this.headers.putAll(headers);
        return (R) this;
    }

    public StubContentType getContentType() {
        final List<String> contentType = headers.entrySet().stream().filter(this::isContentType).findFirst()
            .orElse(new SimpleEntry<>(null, null)).getValue();
        return contentType != null ? new StubContentType(contentType) : null;
    }

    private boolean isContentType(Entry<String, List<String>> entry) {
        return CONTENT_TYPE.equals(entry.getKey().toLowerCase());
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @SuppressWarnings("unchecked")
    public R withBody(T body) {
        this.body = body;
        return (R) this;
    }

    public String stringifyBody() {
        return stringify.toString(body);
    }

    public abstract R copy();
}

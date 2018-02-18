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

import static java.util.Map.Entry;
import static shiver.me.timbers.webservice.stub.api.StubQuery.parse;

/**
 * @author Karl Bennett
 */
public abstract class StubRequest<T, R extends StubRequest> extends StubMessage<T, R> {

    private String method;
    private String path;
    private StubQuery query;

    protected StubRequest(Stringify stringify) {
        super(stringify, new StubHeaders(), null);
        this.method = "GET";
        this.path = "";
        this.query = new StubQuery();
    }

    protected StubRequest(Stringify stringify, T body) {
        this(stringify);
        this.method = "POST";
        setBody(body);
    }

    protected StubRequest(StubRequest<T, R> request) {
        super(request);
        this.method = request.getMethod();
        this.path = request.getPath();
        this.query = request.getQuery();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public R withMethod(String method) {
        this.method = method;
        return (R) this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    public R withPath(String path) {
        this.path = path;
        return (R) this;
    }

    public StubQuery getQuery() {
        return query;
    }

    public void setQuery(StubQuery query) {
        this.query = query;
    }

    public R withQuery(String query) {
        return withQuery(parse(query));
    }

    @SuppressWarnings("unchecked")
    public R withQuery(Entry<String, List<String>>... params) {
        return withQuery(new StubQuery(params));
    }

    @SuppressWarnings("unchecked")
    public R withQuery(StubQuery query) {
        this.query = query;
        return (R) this;
    }

    @Override
    public String toString() {
        return "StubRequest{" +
            ", method='" + method + '\'' +
            ", path='" + path + '\'' +
            ", query=" + query +
            ", headers=" + getHeaders() +
            ", body=" + getBody() +
            '}';
    }
}

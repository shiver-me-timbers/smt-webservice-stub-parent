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

/**
 * @author Karl Bennett
 */
public abstract class StubResponse<T, R extends StubResponse> extends StubMessage<T, R> {

    private int status;

    protected StubResponse(Stringify stringify) {
        this(stringify, null);
    }

    protected StubResponse(Stringify stringify, T body) {
        super(stringify, new StubHeaders(), body);
        this.status = 200;
    }

    protected StubResponse(StubResponse<T, R> response) {
        super(response);
        this.status = response.getStatus();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @SuppressWarnings("unchecked")
    public R withStatus(int status) {
        this.status = status;
        return (R) this;
    }

    @Override
    public String toString() {
        return "StubResponse{" +
            ", status=" + status +
            ", headers=" + getHeaders() +
            ", body=" + getBody() +
            '}';
    }
}

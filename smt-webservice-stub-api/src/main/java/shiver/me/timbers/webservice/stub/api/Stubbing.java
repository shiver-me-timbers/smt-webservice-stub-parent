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


import java.util.Objects;

public class Stubbing<RQ extends StubRequest, RS extends StubResponse> {

    private RQ request;
    private RS response;

    public Stubbing() {
    }

    public Stubbing(RQ request, RS response) {
        this.request = request;
        this.response = response;
    }

    public RQ getRequest() {
        return request;
    }

    public void setRequest(RQ request) {
        this.request = request;
    }

    public RS getResponse() {
        return response;
    }

    public void setResponse(RS response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stubbing stubbing = (Stubbing) o;
        return Objects.equals(request, stubbing.request) &&
            Objects.equals(response, stubbing.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, response);
    }

    @Override
    public String toString() {
        return "Stubbing{" +
            "request=" + request +
            ", response=" + response +
            '}';
    }
}

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

package shiver.me.timbers.webservice.stub.server.spring;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.Stub;
import shiver.me.timbers.webservice.stub.server.Stubber;
import shiver.me.timbers.webservice.stub.server.Verifier;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Karl Bennett
 */
@RestController
public class StubController {

    private final Stubber stubber;
    private final Stub stub;
    private final Verifier verifier;

    public StubController(Stubber stubber, Stub stub, Verifier verifier) {
        this.stubber = stubber;
        this.stub = stub;
        this.verifier = verifier;
    }

    @RequestMapping(path = "/stubber", method = PUT, consumes = APPLICATION_JSON_VALUE)
    public void stub(@RequestBody Stubbing<StringStubRequest, StringStubResponse> stubbing) {
        stubber.stubCall(stubbing);
    }

    @RequestMapping(path = "/stub*", consumes = ALL_VALUE)
    public ResponseEntity<String> call(StringStubRequest request) {
        final StringStubResponse response = stub.call(request);
        final HttpHeaders headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        return ResponseEntity.status(response.getStatus()).headers(headers).body(response.getBody());
    }

    @RequestMapping(path = "/verifier", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void verify(@RequestBody Verifying<StringStubRequest> verifying) {
        verifier.verify(verifying);
    }
}

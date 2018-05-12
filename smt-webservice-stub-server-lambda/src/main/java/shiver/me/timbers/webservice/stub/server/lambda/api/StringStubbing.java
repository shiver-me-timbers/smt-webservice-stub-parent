package shiver.me.timbers.webservice.stub.server.lambda.api;

import shiver.me.timbers.webservice.stub.api.Stubbing;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

public class StringStubbing extends Stubbing<StringStubRequest, StringStubResponse> {

    public StringStubbing() {
    }

    public StringStubbing(StringStubRequest request, StringStubResponse response) {
        super(request, response);
    }
}

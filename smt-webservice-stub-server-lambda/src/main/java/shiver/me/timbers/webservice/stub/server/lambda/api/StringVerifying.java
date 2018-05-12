package shiver.me.timbers.webservice.stub.server.lambda.api;

import shiver.me.timbers.webservice.stub.api.Verifying;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

public class StringVerifying extends Verifying<StringStubRequest> {

    public StringVerifying() {
    }

    public StringVerifying(StringStubRequest request) {
        super(request);
    }
}

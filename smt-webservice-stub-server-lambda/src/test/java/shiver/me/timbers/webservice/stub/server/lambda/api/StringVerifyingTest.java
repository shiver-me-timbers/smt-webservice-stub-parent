package shiver.me.timbers.webservice.stub.server.lambda.api;

import org.junit.Test;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;

import static org.mockito.Mockito.mock;

public class StringVerifyingTest {

    @Test
    public void Instantiation_for_coverage() {
        new StringVerifying();
        new StringVerifying(mock(StringStubRequest.class));
    }
}
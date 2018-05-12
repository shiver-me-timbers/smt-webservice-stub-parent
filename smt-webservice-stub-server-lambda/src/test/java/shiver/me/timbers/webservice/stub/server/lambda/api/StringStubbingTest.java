package shiver.me.timbers.webservice.stub.server.lambda.api;

import org.junit.Test;
import shiver.me.timbers.webservice.stub.server.api.StringStubRequest;
import shiver.me.timbers.webservice.stub.server.api.StringStubResponse;

import static org.mockito.Mockito.mock;

public class StringStubbingTest {

    @Test
    public void Instantiation_for_coverage() {
        new StringStubbing();
        new StringStubbing(mock(StringStubRequest.class), mock(StringStubResponse.class));
    }
}
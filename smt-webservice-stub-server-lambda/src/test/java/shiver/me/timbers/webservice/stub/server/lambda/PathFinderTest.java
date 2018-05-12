package shiver.me.timbers.webservice.stub.server.lambda;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class PathFinderTest {

    private PathFinder finder;

    @Before
    public void setUp() {
        finder = new PathFinder();
    }

    @Test
    public void Can_find_the_path_for_a_normal_request() {

        final ProxyRequest request = mock(ProxyRequest.class);

        // Given
        given(request.getPathParameters()).willReturn(null);

        // When
        final String actual = finder.findPath(request);

        // Then
        assertThat(actual, isEmptyString());
    }

    @Test
    public void Can_find_the_path_for_a_proxy_string() {

        final ProxyRequest request = mock(ProxyRequest.class);

        final String expected = someString();

        // Given
        given(request.getPathParameters()).willReturn(singletonMap("proxy", expected));

        // When
        final String actual = finder.findPath(request);

        // Then
        assertThat(actual, is(expected));
    }
}
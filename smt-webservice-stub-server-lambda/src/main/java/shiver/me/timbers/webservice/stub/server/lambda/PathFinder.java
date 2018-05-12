package shiver.me.timbers.webservice.stub.server.lambda;

import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;

import java.util.Map;

class PathFinder {

    String findPath(ProxyRequest<?> request) {
        final Map<String, String> pathParameters = request.getPathParameters();
        if (pathParameters != null) {
            return pathParameters.get("proxy");
        }
        return "";
    }
}

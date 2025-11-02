package com.example.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // Literal from the request
    private HttpVersion bestCompatibleVersion;

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = HttpMethod.valueOf(methodName);
                return;
            }
            else {
                throw new HttpParsingException(
                        HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED
                );
            }
        }
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isEmpty()){
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if (this.bestCompatibleVersion == null){
            throw new HttpParsingException(
                    HttpStatusCodes.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }
}

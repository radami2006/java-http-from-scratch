package com.example.http;

import java.util.HashMap;
import java.util.Set;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // Literal from the request
    private HttpVersion bestCompatibleVersion;
    private HashMap<String, String> headers = new HashMap<>();

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
    public Set<String> getHeadersNames() {
        return headers.keySet();
    }
    public String getHeader(String headerName) {
        return headers.get(headerName.toLowerCase());
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
    void addHeader(String headerName, String headerField){
        headers.put(headerName.toLowerCase(), headerField);
    }
}

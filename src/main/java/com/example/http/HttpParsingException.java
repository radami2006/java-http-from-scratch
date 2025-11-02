package com.example.http;

public class HttpParsingException extends Exception {

    private HttpStatusCodes errorCode;

    public HttpParsingException(HttpStatusCodes errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCodes getErrorCode() {
        return errorCode;
    }
}

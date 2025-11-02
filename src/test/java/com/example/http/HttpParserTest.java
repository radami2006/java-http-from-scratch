package com.example.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        this.httpParser = new HttpParser();
    }


    /* --- TESTS --- */
    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail();
        }

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(request.getRequestTarget(), "/");
        assertEquals("HTTP/1.1", request.getOriginalHttpVersion());
        assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleVersion());
    }

    @Test
    void parseHttpRequestBadMethod1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethodName1());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethodName2());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestInvNumItems1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseRequestLineInvNumItems1());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestLineCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestBadHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseBadVersion()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseUnsupportedVersion()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestSupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateSupportedHttpVersion1()
            );
            assertNotNull(request);
            assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleVersion());
            assertEquals("HTTP/1.2", request.getOriginalHttpVersion());
        } catch (HttpParsingException e) {
            fail();
        }
    }

    /* --- GENERATORS --- */
    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                         "Host: localhost:8080\r\n" +
                         "Connection: keep-alive\r\n" +
                         "Cache-Control: max-age=0\r\n" +
                         "sec-ch-ua: \"Chromium\";v=\"142\", \"Brave\";v=\"142\", \"Not_A Brand\";v=\"99\"\r\n" +
                         "sec-ch-ua-mobile: ?0\r\n" +
                         "sec-ch-ua-platform: \"Windows\"\r\n" +
                         "Upgrade-Insecure-Requests: 1\r\n" +
                         "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36\r\n" +
                         "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8\r\n" +
                         "Sec-GPC: 1\r\n" +
                         "Accept-Language: es-ES,es;q=0.9\r\n" +
                         "Sec-Fetch-Site: none\r\n" +
                         "Sec-Fetch-Mode: navigate\r\n" +
                         "Sec-Fetch-User: ?1\r\n" +
                         "Sec-Fetch-Dest: document\r\n" +
                         "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                StandardCharsets.US_ASCII
        ));

        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName1() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName2() {
        String rawData = "WAYTOOLONGMETHOD / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateBadTestCaseRequestLineInvNumItems1() {
        String rawData = "GET / AAAAAAAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateBadTestCaseEmptyRequestLine() {
            String rawData = "\r\n" +
                    "Host: localhost:8080\r\n" +
                    "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

            InputStream inputStream = new ByteArrayInputStream(
                    rawData.getBytes(
                            StandardCharsets.US_ASCII
                    ));

            return inputStream;
        }

    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF() {
        String rawData = "GET / HTTP/1.1\r" + // <----------- no LF
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateBadTestCaseBadVersion() {
        String rawData = "GET / HTP/1.1\r\n" + // <----------- bad format version
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateBadTestCaseUnsupportedVersion() {
        String rawData = "GET / HTTP/2.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }

    private InputStream generateSupportedHttpVersion1() {
        String rawData = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                ));

        return inputStream;
    }
    }

package com.example.httpserver.core.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {

    WebRootHandler webRootHandler;
    private Method checkIfEndsWithSlashMethod;
    private Method checkIfProvidedPathExists;

    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
        webRootHandler = new WebRootHandler("WebRoot");
        Class<WebRootHandler> cls = WebRootHandler.class;
        checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checkIfEndsWithSlash", String.class);
        checkIfEndsWithSlashMethod.setAccessible(true);

        checkIfProvidedPathExists = cls.getDeclaredMethod("checkIfProvidedRelativePathExists", String.class);
        checkIfProvidedPathExists.setAccessible(true);
    }

    @Test
    void constructorGoodPath1() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("C:\\Users\\danie\\Downloads\\simple-http-server\\WebRoot");
        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath1() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("C:\\fakepath");
            fail();
        } catch (WebRootNotFoundException e) {
        }
    }


    @Test
    void constructorGoodPath2() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot");
        } catch (WebRootNotFoundException e) {
            fail();
        }
    }

    @Test
    void constructorBadPath2() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot2");
            fail();
        } catch (WebRootNotFoundException e) {
        }
    }

    @Test
    void CheckIfEndsWithSlashMethodFalse() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void CheckIfEndsWithSlashMethodTrue() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "WebRoot/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExists() {
        try {
            boolean result = (boolean) checkIfProvidedPathExists.invoke(webRootHandler, "index.html");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    @Test
    void testWebRootFilePathDoesNotExist() {
        try {
            boolean result = (boolean) checkIfProvidedPathExists.invoke(webRootHandler, "indexNotHere.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    @Test
    void testGetFileMimeType() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/");
            assertEquals("text/html", mimeType);
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Test
    void testGetFileMimeTypePng() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/logo.png");
            assertEquals("image/png", mimeType);
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Test
    void testGetFileMimeTypeDefault() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/favicon.ico");
            assertEquals("application/octet-stream", mimeType);
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Test
    void testGetFilesByteArrayData() {
        try {
            assertTrue(webRootHandler.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException e) {
            fail();
        } catch (ReadFileException e) {
            fail();
        }

    }

    @Test
    void testGetFilesByteArrayDataFileNotFound() {
        try {
            webRootHandler.getFileByteArrayData("/there-is-no-file-at-this-address.html");
            fail();
        } catch (FileNotFoundException e) {
            // Pass
        } catch (ReadFileException e) {
            fail();
        }

    }
}

package com.example.httpserver;

import com.example.httpserver.config.Configuration;
import com.example.httpserver.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * Driver class for the HTTP server
 *
 */
public class HttpServer {

    public static void main(String[] args){

        System.out.println("Server starting...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Using Port: " + conf.getPort());
        System.out.println("Using webRoot: " + conf.getWebroot());

        try {
            ServerSocket serverSocket = new ServerSocket(conf.getPort());
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // TODO we would do the writing

            String html = "<html><head><title>Simple Java HTTP server</title></head><body><h1>This page was served using my Simple Java HTTP Sever</h1></body></html>";

            final String CRLF = "\n\r"; // 13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + // STATUS line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                    "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());
            inputStream.close();
            outputStream.close();
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


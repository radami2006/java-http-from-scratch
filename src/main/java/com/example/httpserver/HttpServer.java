package com.example.httpserver;

import com.example.httpserver.config.Configuration;
import com.example.httpserver.config.ConfigurationManager;

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

        System.out.println(conf.getPort());
        System.out.println(conf.getWebroot());
    }
}


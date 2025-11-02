# Documentation for simple HTTP server in Java
This document describes the implementation steps followed to write this HTTP server in Java. These steps are taken from this [tutorial](https://youtu.be/FNUdLeGfShU) by [CoderFromScratch](https://www.youtube.com/@SuperCoderFromScratch).
## 1. JSON configuration loader
The following classes make up the JSON configuration loading system:
* Configuration
* ConfigurationManager
* HttpConfigurationException
* JSON  
These classes allow for the loading of settings for the HTTP server object from a json file.

## 2. HTTP SeverSocket
The server listens on the configured port and serves a hardcoded response
## 3. HTTP Server multithreading
The server creates a new thread for each connection and each processing.
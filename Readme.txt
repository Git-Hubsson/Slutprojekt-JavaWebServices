# Java Networking Project: Design a Web Service in Java
## Overview
This project is a Java-based application demonstrating fundamental concepts in network programming and API development. It features a simple client-server architecture where the server handles requests for storing and retrieving person data. The client, connecting through a specified port and URL, can send or retrieve this data.

## Features
Client-Server Communication: Utilizing Java sockets for establishing a connection between the client and server.

Data Management: Handling JSON objects to store and manage person data.

Dynamic Data Handling: Ability to add, retrieve, and send person information through the client-server model.

Basic API Simulation: Although not a full-fledged API, the project simulates some aspects of an API by handling different types of requests (GET, POST, QUIT).
## How to Use
Starting the Server:
Run the Server.java file.
The server will start on localhost at port 1337 (default setting).

Connecting the Client:
Run the Client.java file.
Enter the URL (localhost) and port (1337) when prompted.

Interacting with the Server:
Through the client interface, you can choose to GET or POST data, or QUIT the session.
GET: Retrieves the information of a person or all stored data.
POST: Allows you to add a new person's data to the server.
QUIT: Closes the client connection.
## Technology
Java: Core programming language.

JSON.simple: Used for handling JSON in Java.

Java Sockets: For client-server communication.
## Future Enhancements to be made
Database Integration: Implementing a real database system (like MySQL or MongoDB) instead of using in-memory JSON objects.
RESTful API Development: Transitioning to a RESTful architecture using frameworks like Spring Boot or Jersey for more standardized API functionality.
Error Handling and Logging: Improving error responses and implementing a logging mechanism.
Frontend Development: Creating a simple UI for easier interaction with the server.

http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm

Länken går till den nödvändiga JAR filen för att kunna köra applikationen.
JAR filen innehåller följande klassfiler eller Java-källfiler.
META-INF/MANIFEST.MF
org.json.simple.ItemList.class
org.json.simple.JSONArray.class
org.json.simple.JSONAware.class
org.json.simple.JSONObject.class
org.json.simple.JSONStreamAware.class
org.json.simple.JSONValue.class
org.json.simple.parser.ContainerFactory.class
org.json.simple.parser.ContentHandler.class
org.json.simple.parser.JSONParser.class
org.json.simple.parser.ParseException.class
org.json.simple.parser.Yylex.class
org.json.simple.parser.Yytoken.class

För att kunna ta dig vidare på klientsidan så måste du ange localhost som URL och port 1337.

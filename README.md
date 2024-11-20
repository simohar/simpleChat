# Assignment 2: Client-Server Architecture
This repository contains the source code for Assignment 2 for the course Introduction to Software Engineering (SEG2505) at the University of Ottawa.

## Project Description
The objective of this assignment is to practice concepts related to client-server architecture in Java, using the OCSF (Object Client-Server Framework). The project is a simple chat application that allows multiple clients to connect to a server and exchange messages.

## Implemented Features
The following extensions have been added to the initial application:

## Client Side

Command Handling: The client recognizes the following commands, which start with the # symbol:

#quit: Properly terminates the client.
#logoff: Disconnects the client from the server without quitting the application.
#sethost <host>: Sets the hostname (only if the client is disconnected).
#setport <port>: Sets the port number (only if the client is disconnected).
#login: Connects the client to the server (only if the client is disconnected).
#gethost: Displays the current hostname.
#getport: Displays the current port number.

- Server Disconnection Handling: If the server stops while a client is connected, the client displays a message indicating that the server has been stopped and exits.
- Login with Identifier: The client must now provide a mandatory login identifier (loginID) at startup. This identifier is used to identify the client to the server and other clients.

## Server Side
- Client Connection/Disconnection Handling: The server displays a message each time a client connects or disconnects.
- Server User Interface: A ServerConsole class has been added to allow the server user to enter commands or messages to send to clients.

Command Handling: The server recognizes the following commands, which start with the # symbol:

#quit: Properly terminates the server.
#stop: Stops listening for new clients.
#close: Stops the server and disconnects all existing clients.
#setport <port>: Sets the port number (only if the server is closed).
#start: Starts the server to listen for new clients (only if the server is stopped).
#getport: Displays the current port number.
- Login Identifier Management: The server manages the clients' login identifiers to identify who is sending messages.

## Project Structure
The project is organized according to the following package structure:

edu.seg2105.client.common: Contains the ChatIF interface.
edu.seg2105.client.ui: Contains the ClientConsole class.
edu.seg2105.client.backend: Contains the ChatClient class.
edu.seg2105.server.ui: Contains the ServerConsole class.
edu.seg2105.server.backend: Contains the EchoServer class.

## Prerequisites
Java Development Kit (JDK) 8 or higher.
An Integrated Development Environment (IDE) such as Visual Studio Code with Java extensions, or any other Java-compatible IDE.

## Compilation and Execution
### Server
#### Compilation:

From the root of the project, compile the server classes:
javac edu/seg2105/server/backend/EchoServer.java edu/seg2105/server/ui/ServerConsole.java edu/seg2105/client/common/ChatIF.java

#### Execution:

To start the server with the default port (5555):
java edu.seg2105.server.ui.ServerConsole
To specify a different port:

java edu.seg2105.server.ui.ServerConsole <port>
### Client
#### Compilation:

From the root of the project, compile the client classes:
javac edu/seg2105/client/backend/ChatClient.java edu/seg2105/client/ui/ClientConsole.java edu/seg2105/client/common/ChatIF.java

#### Execution:

To start the client, you must provide a mandatory loginID. The host and port parameters are optional (default: localhost and 5555).
java edu.seg2105.client.ui.ClientConsole <loginID> [host [port]]

Example:
java edu.seg2105.client.ui.ClientConsole user1
Or with a specific host and port:
java edu.seg2105.client.ui.ClientConsole user1 127.0.0.1 1234

## Usage
Client Commands
- Send a message: Type the message and press Enter.
- Use a command: Start the line with # followed by the command.
Server Commands
- Send a message to all clients: Type the message and press Enter.
- Use a command: Start the line with # followed by the command.

## Test Cases
The features have been tested according to the test cases specified in the provided document. The tests include:

- Starting the server with default or specific arguments.
- Connecting clients with or without a login identifier.
- Sending and receiving messages between clients and server.
- Command handling on both client and server sides.
- Handling server disconnections and shutdowns.

## Notes
This project was completed as part of Assignment 2 for the course SEG2505 - Introduction to Software Engineering.
Any resemblance to existing code is purely coincidental, and modifications have been made in accordance with the assignment instructions.

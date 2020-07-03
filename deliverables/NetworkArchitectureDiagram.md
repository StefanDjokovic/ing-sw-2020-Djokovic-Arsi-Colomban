# Network architecture

* __Server__: the server is composed of __Model__ and __Controller__, and a __network layer__, consisting of:
  + __ServerHandler__: the class that initializes the server. It has methods that init all the game components, described more in-depth in the "initialization" paragraph of this document
  + __SocketConnection__:  they're the threads that handle the connections with each client
  + __VirtualView__: Model and Controller see it as they would see the clientCLI on a not-networked solution, it gets messages from and delivers messages to the SocketConnection instances, and then notifies its observers. There's one for each player, and it's linked to exactly one SocketConnection.
---
* __Client__: the client is composed of __View__ and a __network layer__, consisting of a single class, called
  + __Client__: it handles all networking elements, the View sees it as it would see Model and Controller, so when a message arrives the Client class updates its observers (the clientCLI), delivering the message, and when the View updates its observers (the Client class) with a message, the client sends it to the server.

---

* __Messages__: server and client communicate through messages. All message classes are _serializable_, and they get sent from client to server and from server to client through `InputObjectStream` and `OutputObjectStream` streams.

 The two kinds of messages are:
  + __Requests__: messages sent from the server to the client, once they arrive they ask the View to do something
  + __Answers__: messages sent from the client to the server, once they arrive they ask the Controller to do something

# Initialization

```
+--------+                +--------+
| Server |                | Client |
+--------+                +--------+        
     |                         |
     |RequestGameInformation   |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-|user gets lobby info | 
     |                         | |   displayed on CLI  |
     |                         | |        or GUI       |
     |                         | |---------------------|
     |       AnswerLobbyAndName|
     |<------------------------|
     |                         |
     |                         |
     |RequestWaitOpponentMove  |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-|user gets a waiting  | 
     |                         | |   message until the |
     |                         | |other players connect|
     |                         | |---------------------|                       |   
     |RequestPlayerGod         |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-|  if user is the 1st |
     |                         | | player, they pick n |
     |                         | |  gods (n=lobby size)|
     |                         | | else they pick a god|
     |                         | |    from the list    |
     |                         | |---------------------|
     |          AnswerPlayerGod|
     |<------------------------|
     |                         |
     |                         |
     |RequestWorkerPlacement   |
     |------------------------>|
     |                         |
     |                         | /----------------------\
     |                         |-|user picks coordinates|
     |                         | |    for the workers   |
     |                         | |----------------------|
     |    AnswerWorkerPlacement|
     |<------------------------|
     |                         |
     |                         |
     |RequestUpdateBoardView   |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-| View updates board  |
     |                         | |---------------------|
     |                         |
     |                         |
     |RequestDisplayBoard      |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-|  View prints board  |
     |                         | |---------------------|

```

# Main loop

After the initialization phase is completed, the game enters the "main loop", asking players for their moves until one of them wins the game, according to the logic described in the graph below.

```
+--------+                +--------+
| Server |                | Client |
+--------+                +--------+        
     |                         |
     |RequestPowerCoordinates  |
     |------------------------>|
     |                         |
     |                         | /----------------------\
     |                         |-|user picks coordinates|
     |                         | |  where they want to  |
     |                         | |     use the power    |
     |                         | |----------------------|
     |                         |
     |   AnswerPowerCoordinates|
     |<------------------------|
     |                         |
     |                         |
     |RequestUpdateBoardView   |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-| View updates board  |
     |                         | |---------------------|
     |                         |
     |                         |
     |RequestDisplayBoard      |
     |------------------------>|
     |                         |
     |                         | /---------------------\
     |                         |-|  View prints board  |
     |                         | |---------------------|

```

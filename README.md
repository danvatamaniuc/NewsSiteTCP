# NewsSiteTCP
An example of a Java client-server app that uses the TCP protocol

The project is divided into 3 main components: the client, the server, and the bits they share in common.
Both the client and the server are dependant of the common module.

The client and server communicate between them using generic messages, that can transport all kinds of objects.
Important to note that if you want to send a class via such a message, said class needs to be serialized.

Messages also contain a "what" clause, that specifies the method from the server's manager to be invoked.
In case the message is being sent by the server, the "what" clause specifies the status of the request: was it succsesful or not.

The method requests received from the client are handled by the server using method handlers (basic lambda functions that call the desired method from the Manager).
In case the method is not recognized, a bad request response is being issued.

The Manager-type class is responsible for processing the requests from the UI - client scenari - or from the client - server scenario-.
Has an interface to ensure all the methods declared will be available.

Rest of the app is basic CRUD repos, some functional bits, and a console interface.

In case any question arise, feel free to ask me.

These are clients and a server that allow you to transfer txt files.


The server takes no arguments

The server will automatically find open ports for UDP and TCP connections.
The server will print out it's name/ip aswell as the aforementioned ports. This information is needed for the clients.
When a connection is made to the server, the server will send a file containing all the files in the server, and then handle the command given to it.

The (Get|Put)Client take 5 arguments
localFile, remoteFile, hostName, TCPport, UDPport

GetClient retrieves txt files from the server. 
remoteFile is the name of the txt-file to be retrieved. 
The file content will be placed in a new txt-file named localFile

PutClients send txt files to the server.
localFile is the name of the txt-file whose content will be sent to the server.
The Server will place that in a new txt-file named remoteFile.

Clients close after the file transfer is complete

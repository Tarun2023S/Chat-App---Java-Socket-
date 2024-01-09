package com.zoho_Inc.ChatApp;
import java.util.*;
import java.io.*;
import java.net.*;

public class Server 
{
	private ServerSocket serverSocket;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("S: A new client has connected");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}
		catch(IOException e) {
			
		}
	}
	
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Hello World!" );
        int portNumber = 1234;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Server server = new Server(serverSocket);
        server.startServer();
    }
    
    public void closeServerSocket() {
    	try {
    		if(serverSocket != null) {
    			serverSocket.close();
    		}
    	}
		catch(IOException e) {
					
		}
    }
}

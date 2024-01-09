package com.zoho_Inc.ChatApp;
import java.util.*;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		System.out.println("Calling CH Constr..");
		try {
			System.out.println("Inside CHConstr try block..");
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			System.out.println("Before bcMsg");
			broadCastMessage("SEREVR: "+clientUsername+" has entered the chat!");
//			System.out.println("After bcMsg");
			
		}
		catch(IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	public void run() {
		String msgFromClient;
		while(socket.isConnected()) {
			System.out.println("Conneted..");
			try {
				System.out.println("Read this");
				msgFromClient = bufferedReader.readLine();
				broadCastMessage(msgFromClient);
			} catch(IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	public void broadCastMessage(String messageToSend) {
	    for (ClientHandler clientHandler : clientHandlers) {
	        try {
	            if (!clientHandler.clientUsername.equals(clientUsername)) {
	                System.out.println("Writing to: " + clientHandler.clientUsername);
	                clientHandler.bufferedWriter.write(messageToSend);
	                clientHandler.bufferedWriter.newLine(); // Add this line to send a newline character
	                clientHandler.bufferedWriter.flush();
	                System.out.println("Written to: " + clientHandler.clientUsername);
	            }
	        } catch (IOException e) {
	            closeEverything(socket, bufferedReader, bufferedWriter);
	        }
	    }
	}

	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadCastMessage("SEREVR: "+clientUsername+" has left the chat!");
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if(socket != null) {
				socket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

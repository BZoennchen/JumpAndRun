package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
	private ServerSocket server;
	private List<Socket> clientList;
	private final int PORT = 5413; 
	
	public SocketServer(){
		clientList = new ArrayList<Socket>();
	}
	
	public void open() throws IOException{
		server = new ServerSocket(PORT);
		
		while(!server.isClosed())
		{
			clientList.add(server.accept());
		}
	}
	
	public void close() throws IOException{
		server.close();
	}
}

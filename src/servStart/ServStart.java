package servStart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServStart {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServStart s = new ServStart();
		s.initConnection(444);

	}
	public Socket clientSocket;
	
	
	public PrintWriter outputWriter;
	public BufferedReader networkInputStream;
	
	
	public void initConnection(int port ) throws IOException{
		System.out.println("starting");
		ServerSocket serverSocket = null;
		clientSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		try {
			System.out.println("Waiting for connection...");
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.out.println("Accept failed: 4444");
			System.exit(-1);
		}

		System.out.println("ConnectionReceived,");
		
		outputWriter = new PrintWriter(clientSocket.getOutputStream());
		networkInputStream = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

	}

}

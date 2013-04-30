package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import network.inputHandler.NetworkHandler;

import panel.TextLogWindow;

public class ObjectServer {
	ObjectOutputStream objectOutputStream = null;
	ObjectInputStream objectInputStream = null; 
	TextLogWindow log;

	public ObjectServer(TextLogWindow _log) {
		super();
		this.log = _log;
	}

	/**
	 * 
	 * @param port
	 * @return
	 */
	public int initConnection(int port){
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			log.println("Waiting for connection on port "+port);
			clientSocket = serverSocket.accept();
			log.println("Incomming connection");
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			log.println("OutputStream OK");
			objectInputStream =  new ObjectInputStream(clientSocket.getInputStream());
			log.println("InputStream OK");
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

	private void sendToClient(Object obj){
		try {
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(NetworkHandler netHandler) {
		log.println("Waiting for object from client");
		boolean continue_ = true;
		while(continue_){
			try {
				Object input = objectInputStream.readObject();
				netHandler.handle(input);
				System.err.println("end of read");
			} catch (IOException e) {
				log.println("ERROR GETTING OBJECT");
				e.printStackTrace();
				continue_ = false;
			} catch (ClassNotFoundException e) {
				log.println("ClassNotFoundException");
				e.printStackTrace();
			}
			//send to the client answer;
			Object answer = netHandler.buildResponsePacket();
			sendToClient(answer);
		}
	}
}

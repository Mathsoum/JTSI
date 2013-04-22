package servStart.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import panel.TextLogWindow;


public class ObjectServer {
	Socket clientSocket = null;

	OutputStream outputStream = null;
	ObjectOutputStream objectOutputStream = null;


	InputStream inputstream = null;
	ObjectInputStream objInput = null; 


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
	public int initConnection(int port ){
		ServerSocket serverSocket = null;
		clientSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			log.println("Could not listen on port: " + port);
			return -1;
		}

		try {
			log.println("Waiting for connection on port "+port);
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			log.println("Accept failed: 4444");
			return -1;
		}

		log.println("ConnectionReceived");

		try {
			outputStream = clientSocket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		log.println("outputWriter OK");

		try {
			inputstream = clientSocket.getInputStream();
			objInput =  new ObjectInputStream(inputstream);

		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		log.println("InputStream OK");

		return 1;
	}

	public void sendToClient(Object obj){
		//socketPrintWriter.println(text);

		try {
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitForIncomingObject(ObjectHandler  objhandler) {
		log.println("Waiting for object from client");

		boolean continue_ = true;

		while(continue_){

			try {
				Object  o= objInput.readObject();

				objhandler.handle(o);
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
			Object answer = objhandler.buildResponsePacket();
			sendToClient(answer);


		}

	}

}

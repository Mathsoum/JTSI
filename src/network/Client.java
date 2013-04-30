package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private Socket echoSocket = null;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;

	/**
	 * 
	 * @param host
	 * @param port
	 * @return negative number in case of error, 0 or positive number in case of success
	 */
	public int initNetwork(String host,int port) {
		try {
			echoSocket = new Socket(host, port);
			objectOutputStream = new ObjectOutputStream(echoSocket.getOutputStream());
			objectInputStream =  new ObjectInputStream(echoSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			return -1;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "+ host);
			return -2;
		}
		return 1;
	}
	/**
	 * 
	 * @return >= 0 means there is a object to read
	 * < 0 means there is an Error 
	 * 
	 * 	 */
	public Object waitInput(){
		try {
			System.err.println("Waiting object...");
			Object input = objectInputStream.readObject();
			System.err.println("Object is "+input);
			System.err.println("End of read");
			if(input != null){
				System.err.println("Correct object received");
			}else{
				System.err.println("Server stopped connection ?");
			}
			return input;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException");
			e.printStackTrace();
			return null;
		}
		return null;
	}



	public void sendToServer(Object obj){
		try {
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO send same info as the jtsi component
	public static void main(String[] args) throws IOException {
		Client c = new Client();

		if(c.initNetwork("192.168.56.1", 4444) >=0){

			Double[] in = new Double[2];
			c.sendToServer(in);

			c.waitInput();

		}

	}



}

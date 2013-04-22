package servStart;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private Socket echoSocket = null;



	private InputStream inputstream = null;
	ObjectInputStream objInput = null; //new ObjectInputStream(


	private OutputStream outputStream = null;
	private ObjectOutputStream objectOutputStream = null;


	/**
	 * 
	 * @param host
	 * @param port
	 * @return negative number in case of error, 0 or positive number in case of success
	 */
	public int initNetwork(String host,int port) {
		// Network related

		try {

			echoSocket = new Socket(host, port);

			outputStream = echoSocket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

			inputstream = echoSocket.getInputStream();
			objInput =  new ObjectInputStream(inputstream);

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			return -1;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ host);
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
	public Object waitObject(){

		try {
			System.err.println("waitingObject");

			Object  o= objInput.readObject();
			System.err.println("object is "+o);

			System.err.println("end of read");
			if(o != null){
				System.err.println("correctObject received");
			}else{
				System.err.println("server stoppedConnection ?");
			}
			return o;



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
		//socketPrintWriter.println(text);

		try {
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws IOException {
		Client c = new Client();

		if(c.initNetwork("192.168.56.1", 4444) >=0){

			Double[] in = new Double[2];
			c.sendToServer(in);

			c.waitObject();

		}

	}



}

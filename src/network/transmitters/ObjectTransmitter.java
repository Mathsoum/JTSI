package network.transmitters;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectTransmitter extends AbstractTransmitter {
	public ObjectTransmitter(Socket sock) {
		super(sock);
	}

	@Override
	public void send(Object objectToSend) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(objectToSend);
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
	}

	@Override
	public Object receive() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(sock.getInputStream());
			return ois.readObject();
		} catch (EOFException e) {
			System.err.println("EOF exception : "+e.getMessage());
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found exception : "+e.getMessage());
		}
		return null;
	}
}

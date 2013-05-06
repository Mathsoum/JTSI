package network.transmitters;

import java.net.Socket;

public abstract class AbstractTransmitter {
	protected Socket sock;
	
	public AbstractTransmitter(Socket sock) {
		this.sock = sock;
	}
	
	public abstract void send(Object content); 
	public abstract Object receive();
}

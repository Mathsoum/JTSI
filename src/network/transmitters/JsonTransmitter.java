package network.transmitters;

import java.net.Socket;

public class JsonTransmitter extends AbstractTransmitter {
	public JsonTransmitter(Socket sock) {
		super(sock);
	}
	
	@Override
	public void send(Object jsonContent) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object receive() {
		// TODO Auto-generated method stub
		return null;
	}
}

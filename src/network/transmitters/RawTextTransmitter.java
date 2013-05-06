package network.transmitters;

import java.net.Socket;


public class RawTextTransmitter extends AbstractTransmitter {
	public RawTextTransmitter(Socket sock) {
		super(sock);
	}

	@Override
	public void send(Object rawTextToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object receive() {
		// TODO Auto-generated method stub
		return null;
	}

}

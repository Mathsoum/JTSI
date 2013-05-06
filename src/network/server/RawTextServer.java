package network.server;

import network.builders.RawTextBuilder;
import network.transmitters.RawTextTransmitter;
import panel.TextLogWindow;

public class RawTextServer extends Server {

	public RawTextServer(TextLogWindow _log, int port) {
		super(_log);
		initConnection(port);
		transmitter = new RawTextTransmitter(clientSocket);
		builder = new RawTextBuilder();
	}
}

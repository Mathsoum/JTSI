package network.server;

import network.builders.JsonBuilder;
import network.transmitters.JsonTransmitter;
import panel.TextLogWindow;

public class JsonServer extends Server {

	public JsonServer(TextLogWindow _log, int port) {
		super(_log);
		initConnection(port);
		transmitter = new JsonTransmitter(clientSocket);
		builder = new JsonBuilder();
	}
}

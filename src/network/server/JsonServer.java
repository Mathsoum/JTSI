package network.server;

import network.DataType;
import panel.TextLogWindow;

public class JsonServer extends Server {
	public JsonServer(TextLogWindow _log) {
		super(_log);
		dataType = DataType.JSON;
	}
}

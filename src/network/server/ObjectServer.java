package network.server;

import network.DataType;
import panel.TextLogWindow;

public class ObjectServer extends Server {
	public ObjectServer(TextLogWindow _log) {
		super(_log);
		dataType = DataType.SERIALIZED_OBJECT;
	}
}

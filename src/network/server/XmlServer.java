package network.server;

import network.DataType;
import panel.TextLogWindow;

public class XmlServer extends Server {
	public XmlServer(TextLogWindow _log) {
		super(_log);
		dataType = DataType.XML;
	}
}

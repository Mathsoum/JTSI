package network.server;

import network.inputHandler.XmlHandler;
import panel.TextLogWindow;

public class Main_TestObjectServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TextLogWindow tlw = new TextLogWindow();
		ObjectServer os = new ObjectServer(tlw);
		
		os.initConnection(4444);
		os.run(new XmlHandler());
	}

}

package network.run;

import network.server.*;
import panel.TextLogWindow;

public class RunServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TextLogWindow tlw = new TextLogWindow();
		Server os = new XmlServer(tlw);
		os.run(4444);
	}
}

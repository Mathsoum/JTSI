package jtsi;

import jtsi.util.LogWindow;
import network.Client;

public abstract class Subtype_2 extends Subtype {

	protected static final String JTSISERVERHOST = "jtsiserverhost";
	protected static final String JTSISERVERPORT = "jtsiserverport";
	
	protected Client client = null;
	protected LogWindow logwindow = null;
	protected long stepCount = 0;

	public Subtype_2(TrnsysInterface _interface) {
		super(_interface);
	}

	protected Client getNetwork() {
		if(client == null){
			client = new Client();
			logwindow.addText("Client created \n");
			//reading environment variable for configuration
			String serverhost = System.getenv(JTSISERVERHOST);
			if (serverhost == null) {
				logwindow.addText(String.format("%s is not assigned.%n", JTSISERVERHOST));
				client = null;
				return null;
			}
			
			logwindow.addText("Server host is "+serverhost+" \n");
	
			String serverportS = System.getenv(JTSISERVERPORT);
			if (serverportS == null) {
				logwindow.addText(String.format("%s is not assigned.%n", JTSISERVERPORT));
				client = null;
				return null;
			} 
			
			logwindow.addText("serverport is "+serverportS+" \n");
			
			int serverport = new Integer(serverportS);
			logwindow.addText(String.format("Connecting to %s:%s ... %n", serverhost, serverport));
			int res = client.initNetwork(serverhost, serverport);
			if(res>=0){
				logwindow.addText(String.format("OK! %n"));
				return client;
			} else {
				logwindow.addText(String.format("ERROR : %s %n", new Integer(res).toString()));
				client = null;
				return null;
			}
		} else {
			return client;
		}
	}

	protected abstract Object buildPacket();

	@Override
	public abstract void step();

}
package jtsi.util.subtype22;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class IvyServerLink {
	private Ivy ivyCom;
	
	public IvyServerLink() {
		ivyCom = new Ivy("serverApp", "Go hit me !", null);
		try {
			ivyCom.bindMsg("^Client : (.*)", new IvyMessageListener() {
				
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					try {
						ivyCom.sendMsg("Server : Step received!");
						for(String str : arg1) {
							System.out.println("Msg : ["+str+"]");
						}
					} catch (IvyException e) {
						e.printStackTrace();
					}
				}
			});
			ivyCom.bindMsg("^Stop$", new IvyMessageListener() {
				
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					ivyCom.stop();
				}
			});
			ivyCom.start("192.168.1:2010");
		} catch (IvyException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new IvyServerLink();
	}
}

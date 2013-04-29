package jtsi;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class Subtype_22 extends Subtype {
	private Ivy ivyCom;
	private int stepCount;

	public Subtype_22(TrnsysInterface _interface) {
		super(_interface);
		ivyCom = new Ivy("clientApp", "Client ready to send", null);
		try {
			ivyCom.bindMsg("^Server : (.*)", new IvyMessageListener() {
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					for(String str : arg1) {
						System.out.println("Msg : ["+str+"]");
						try {
							ivyCom.sendMsg("Msg : ["+str+"]");
						} catch (IvyException e) {
							e.printStackTrace();
						}
					}
				}
			});
			ivyCom.start("192.168.1:2010");
			System.out.println("Waiting for server...");
			ivyCom.waitForClient("serverApp", 0);
			System.out.println("Let's go!...");
		} catch (IvyException e) {
			e.printStackTrace();
		}
		stepCount = 0;
	}

	@Override
	public void step() {
		try {
			++stepCount;
			System.out.println("Step "+stepCount);
			ivyCom.sendMsg("Client : Step "+stepCount);
		} catch (IvyException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			ivyCom.sendMsg(msg);
		} catch (IvyException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		try {
			ivyCom.sendMsg("Stop");
		} catch (IvyException e) {
			e.printStackTrace();
		}
		
		ivyCom.stop();
	}
	
	public static void main(String[] args) {
		Subtype_22 sub = new Subtype_22(null);
		
//		for(int i=0; i<100; ++i) {
			sub.step();
//		}
		sub.stop();
	}
}

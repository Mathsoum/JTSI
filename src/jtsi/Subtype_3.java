package jtsi;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyException;

public class Subtype_3 extends Subtype {
	private Ivy ivyCom;
	private int stepCount;

	public Subtype_3(TrnsysInterface _interface) {
		super(_interface);
		ivyCom = new Ivy("clientApp", "Client ready to send", null);
		try {
			ivyCom.start("192.168.1:2010");
			ivyCom.waitForClient("serverApp", 0); // Waiting for a running server to send a request
		} catch (IvyException e) {
			System.err.println("Ivy exception : "+e.getMessage());
		}
		stepCount = 0;
	}

	@Override
	public void step() {
		try {
			++stepCount;
			ivyCom.sendMsg("Client : Step "+stepCount);
		} catch (IvyException e) {
			System.err.println("Ivy exception : "+e.getMessage());
		}
	}
	
	public void stop() {
		try {
			ivyCom.sendMsg("Stop");
		} catch (IvyException e) {
			System.err.println("Ivy exception : "+e.getMessage());
		}
		
		ivyCom.stop();
	}
	
	public static void main(String[] args) {
		Subtype_3 sub = new Subtype_3(null);

		long begin = System.currentTimeMillis();
		for(int i=0; i<100; ++i) {
			sub.step();
		}
		long end = System.currentTimeMillis();
		System.out.println("Time : "+(end-begin));
		sub.stop();
	}
}

package jtsi;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class Subtype_3 extends Subtype {
	private Ivy ivyCom;
	private int stepCount;
	private int outputsBindId;

	public Subtype_3(TrnsysInterface _interface) {
		super(_interface);
		ivyCom = new Ivy("Client app", "Client ready to send", null);
		try {
			ivyCom.bindMsgOnce("listening", new IvyMessageListener() {
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					try {
						outputsBindId = ivyCom.bindMsg("^Output (.*)", new IvyMessageListener() {
							@Override
							public void receive(IvyClient arg0, String[] arg1) {
								if(arg1 != null) {
									for(String str : arg1) {
										if(str.equals("EOT")) {
											try {
												ivyCom.unBindMsg(outputsBindId);
											} catch (IvyException e) {
												System.err.println("Ivy excpetion : "+e.getMessage());
											}
										} else {
											System.out.println("Output : "+str);
										}	
									}
								}
							}
						});
						sendInputs();
					} catch (IvyException e) {
						System.err.println("Ivy exception : "+e.getMessage());
					}
				}
				
			});
			ivyCom.start("192.168.1:2010");
			ivyCom.waitForClient("Server app", 0);
			ivyCom.sendMsg("Hey listen to me!");
		} catch (IvyException e) {
			System.err.println("Ivy exception : "+e.getMessage());
		}
		stepCount = 0;
	}

	private void sendInputs() {
		try {
			ivyCom.sendMsg("Input 13");
			ivyCom.sendMsg("Input 37");
			ivyCom.sendMsg("Input 16");
			ivyCom.sendMsg("Input 64");
			ivyCom.sendMsg("Input EOT");
		} catch (IvyException e) {
			System.err.println("Ivy exception : "+e.getMessage());
		}
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
		new Subtype_3(null);
	}
}

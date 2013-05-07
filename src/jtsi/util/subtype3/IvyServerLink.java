package jtsi.util.subtype3;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class IvyServerLink {
	private Ivy ivyCom;
	private int inputsBindId;
	
	private void sendOutputs() {
		try {
			ivyCom.sendMsg("Output 42");
			ivyCom.sendMsg("Output EOT");
		} catch (IvyException e) {
			System.err.println("Ivy excpetion : "+e.getMessage());
		}
	}
	
	public IvyServerLink() {
		try {
			ivyCom = new Ivy("Server app", "Go hit me!", null);
			ivyCom.bindMsgOnce("listen", new IvyMessageListener() {
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					try {
						inputsBindId = ivyCom.bindMsg("^Input (.*)", new IvyMessageListener() {
							@Override
							public void receive(IvyClient arg0, String[] arg1) {
								if(arg1 != null) {
									for(String str : arg1) {
										if(str.equals("EOT")) {
											try {
												ivyCom.unBindMsg(inputsBindId);
												sendOutputs();
											} catch (IvyException e) {
												System.err.println("Ivy excpetion : "+e.getMessage());
											}
										} else {
											System.out.println("Input : "+str);
										}
									}
								}
							}
						});
						ivyCom.sendMsg("I'm listening");
					} catch (IvyException e) {
						System.err.println("Ivy excpetion : "+e.getMessage());
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
			System.err.println("Ivy excpetion : "+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new IvyServerLink();
	}
}

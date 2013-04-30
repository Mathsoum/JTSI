package jtsi.util.subtype3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
				public void receive(IvyClient client, String[] message) {
					String[] out = buildResponse(message[0]);
					try {
						FileWriter fw = new FileWriter("/tmp/out", true);
						for(String str : out) {
							fw.write(str);
							fw.append("\n");
							fw.flush();
						}
						fw.close();
						ivyCom.sendMsg("Server : EOT");
					} catch (IvyException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			ivyCom.bindMsg("^Stop$", new IvyMessageListener() {
				@Override
				public void receive(IvyClient arg0, String[] arg1) {
					try {
						ivyCom.sendMsg("Server : I'm shutting down...");
					} catch (IvyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ivyCom.stop();
				}
			});
			ivyCom.start("192.168.1:2010");
		} catch (IvyException e) {
			e.printStackTrace();
		}
	}
	
	private String[] buildResponse(String input) {
		ArrayList<String> output = new ArrayList<>();
		for(int i=0; i<5; ++i) {
			output.add("Process #"+i+" ["+input+"]... ");
		}
		return output.toArray(new String[output.size()]);
	}
	
	public static void main(String[] args) {
		new IvyServerLink();
	}
}

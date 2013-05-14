package jtsi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jtsi.util.LogWindow;
import network.Client;
import network.DataType;

public abstract class Subtype_2 extends Subtype {

	protected static final String JTSISERVERHOST = "jtsiserverhost";
	protected static final String JTSISERVERPORT = "jtsiserverport";
	
	protected Client client = null;
	protected LogWindow logwindow = null;
	protected long stepCount = 0;

	public Subtype_2(TrnsysInterface _interface) {
		super(_interface);
		logwindow = new LogWindow();
	}

	protected void initClient(DataType dataType) {
		if(client == null){
			logwindow.addText("Client created \n");
			//reading environment variable for configuration
			String serverhost = System.getenv(JTSISERVERHOST);
			if (serverhost == null) {
				logwindow.addText(String.format("%s is not assigned.%n", JTSISERVERHOST));
				client = null;
				return;
			}
			
			logwindow.addText("Server host is "+serverhost+" \n");
	
			String serverportS = System.getenv(JTSISERVERPORT);
			if (serverportS == null) {
				logwindow.addText(String.format("%s is not assigned.%n", JTSISERVERPORT));
				client = null;
				return;
			} 
			
			logwindow.addText("serverport is "+serverportS+" \n");
			
			int serverport = new Integer(serverportS);
			logwindow.addText(String.format("Connecting to %s:%s ... ", serverhost, serverport));
//			client = new Client("192.168.1.25", 4444, dataType);
			client = new Client(serverhost, serverport, dataType);
			if(client != null){
				logwindow.addText(String.format("OK! %n"));
			} else {
				logwindow.addText(String.format("ERROR %n"));
				client = null;
			}
		}
	}
	
	protected Map<String, Object> formatInputs() {
		int inputcount = trnsysInterface.getNbInputs();
	
		Integer timeStep = trnsysInterface.getTimestep();
		ArrayList<Double> inArray = new ArrayList<Double>();
		for(int i = 0 ; i<inputcount ;i++){
			inArray.add(trnsysInterface.getXin(i));
		}

		HashMap<String, Object> packet = new HashMap<String, Object>();	
		packet.put("stepCount", stepCount);
		packet.put("timestep", timeStep);
		packet.put("inarray", inArray);
		packet.put("outputCount", trnsysInterface.getNbOutputs());
		packet.put("intputCount", trnsysInterface.getNbInputs());
	
		return packet;
	}
	
	/**
	 * One simulation step
	 *   * Send inputs to server
	 *   * Wait for response (outputs)
	 *   * Parse XML response
	 *   * Set TRNSys output array
	 */
	@Override
	public void step() {
		long begin = System.nanoTime();
		logwindow.addText(String.format("\nStep %s \n",stepCount));
		this.stepCount ++;
		logwindow.setTitle("step: "+stepCount);
	
		//send over network
		if(client != null){
			//for each input collect values
			//build a packet and send it
			client.sendToServer(formatInputs());
		}
	
		long afterSend = System.nanoTime();
		logwindow.addText("Waiting for server answer... ");
		//receive packet from network
		Map<String, Object> ouputs = client.waitOutputs();
		long afterWaitObj = System.nanoTime();
		@SuppressWarnings("unchecked")
		List<Double> outarray = (List<Double>) ouputs.get("outarray");
		for(int i=0; i< outarray.size() ; i++){
			Double d = outarray.get(i);
			if(d != null) {
				trnsysInterface.setXout(i, d.doubleValue());
			}
		}
		logwindow.addText("OK\n");
		long end = System.nanoTime();
		
		logwindow.addText("Total: "+(end-begin)+"ns\n");
		logwindow.addText("Sending: "+(afterSend-begin)+"ns | ");
		logwindow.addText("Waiting: "+(afterWaitObj-afterSend)+"ns | ");
		logwindow.addText("Setting Output: "+(end-afterWaitObj)+"ns");
	}

}
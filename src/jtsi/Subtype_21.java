package jtsi;

import java.util.HashMap;

import jtsi.util.LogWindow;
import network.Client;

public class Subtype_21 extends Subtype_2 {
	public Subtype_21(TrnsysInterface _interface) {
		super(_interface);
		logwindow = new LogWindow();

	}

	@Override
	protected HashMap<String, Object> buildPacket() {
		int inputcount = trnsysInterface.getNbInputs();
	
		Integer timeStep = trnsysInterface.getTimestep();
		Double[] inArray = new Double[inputcount];
		for(int i = 0 ; i<inputcount ;i++){
			double inputValue = trnsysInterface.getXin(i);
			inArray[i] = inputValue;
		}

		HashMap<String, Object> packet = new HashMap<String, Object>();	
		packet.put("stepCount", stepCount);
		packet.put("timestep", timeStep);
		packet.put("inarray", inArray);
		packet.put("outputCount", trnsysInterface.getNbOutputs());
		packet.put("intputCount", trnsysInterface.getNbInputs());
	
		return packet;
	}

	@Override
	public void step() {
		long begin = System.currentTimeMillis();
		logwindow.addText(String.format("Step %s \n",stepCount));
		//increaseStepCount
		this.stepCount ++;
		//logwindow.setTitle("step: "+stepCount);
		//trnsys interface, getinput,
		//int ouputcount = trnsysInterface.getNbOutputs();
	
		//send over network
		Client client = getNetwork();
		if(client != null){
			//for each input collect values
			//build a packet
			client.sendToServer(buildPacket());
		}
	
		long afterSend = System.currentTimeMillis();
		logwindow.addText("Waiting for server answer...\n");
		//receive packet from network
		Object input = client.waitInput();
		long afterWaitObj = System.currentTimeMillis();
		if(input != null && input instanceof HashMap<?, ?>){
			HashMap<?, ?> packet = (HashMap<?, ?>)input;
			Double[] arrayDouble = (Double[])packet.get("outarray");
			for( int i=0; i< arrayDouble.length ; i++){
				Double d = arrayDouble[i];
				if(d != null){
					trnsysInterface.setXout(i, d.doubleValue());
				}
				//System.err.println(d);
			}
		}
		long end = System.currentTimeMillis();
		
		logwindow.addText("Total: "+(end-begin)+"ms\n");
		logwindow.addText("Sending: "+(afterSend-begin)+"ms | ");
		logwindow.addText("Waiting: "+(afterWaitObj-afterSend)+"ms | ");
		logwindow.addText("Setting Output: "+(end-afterWaitObj)+"ms\n");
		//end
	}
}

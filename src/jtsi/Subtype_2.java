package jtsi;

import java.util.Hashtable;

import jtsi.util.LogWindow;

import servStart.Client;

public class Subtype_2 extends Subtype {

	Client client =null;
	LogWindow logwindow = null;
	
	protected long stepCount = 0;
	/*
	 * 
	 */
	public Subtype_2(TrnsysInterface _interface) {
		super(_interface);
		logwindow = new LogWindow();

	}

	public Client getNetwork(){
		if(client == null){
			client = new Client();
			logwindow.addText("client Created \n");
			//reading environment variable for configuration
			String env = "jtsiserverhost";
			String serverhost = System.getenv(env);
			if (serverhost == null) {
				//addText(String.format("%s=%s%n",env, value));
				logwindow.addText(String.format("%s is not assigned.%n", env));
				client = null;
				return null;
			}
			
			logwindow.addText("server host is "+serverhost+" \n");

			env = "jtsiserverport";
			String serverportS = System.getenv(env);
			if (serverportS == null) {
				//addText(String.format("%s=%s%n",env, value));
				logwindow.addText(String.format("%s is not assigned.%n", env));
				client = null;
				return null;
			} 
			
			logwindow.addText("serverport is "+serverportS+" \n");
			
			int serverport = new Integer(serverportS);
			logwindow.addText(String.format("Connecting to %s:%s ... %n", serverhost,serverport));
			int res = client.initNetwork(serverhost, serverport);
			if(res>=0){
				logwindow.addText(String.format("OK! %n"));
				return client;
			}else{
				logwindow.addText(String.format("ERROR : %s %n", new Integer(res).toString()));
				client = null;
				return null;
			}
		}else{
			return client;
		}
	}

	public Hashtable<String, Object> buildPacket() {
		int inputcount = trnsysInterface.getNbInputs();

		Integer timeStep = trnsysInterface.getTimestep();
		Double[] inArray = new Double[inputcount];
		for(int i = 0 ; i<inputcount ;i++){
			double inputValue = trnsysInterface.getXin(i);
			inArray[i] = inputValue;
		}


		Hashtable<String, Object> packet = new Hashtable<String, Object>();

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
		logwindow.addText(String.format("step %s \n",stepCount));
		//increaseStepCount
		this.stepCount ++;
		//logwindow.setTitle("step: "+stepCount);
		//trnsys interface, getinput,
		//int ouputcount = trnsysInterface.getNbOutputs();


		//send over network
		if(getNetwork() != null){
			//for each input collect values
			//build a packet
			Hashtable<String, Object> packlet = buildPacket();
			getNetwork().sendToServer(packlet);


		}

		long afterSend = System.currentTimeMillis();
		logwindow.addText("waiting for server answer ");
		//receive packet from network
		Object o = getNetwork().waitObject();
		
		long afterWaitObj = System.currentTimeMillis();
		
		if( o != null ){
			//decode packet
			if(o instanceof Hashtable<?, ?> ){
				Hashtable<?, ?> packet = (Hashtable<?, ?>)o;

				Double[] arrayDouble = (Double[])packet.get("outarray");

				for( int i=0; i< arrayDouble.length ; i++){
					Double d = arrayDouble[i];
					if(d != null){
						trnsysInterface.setXout(i, d.doubleValue());
					}
					//System.err.println(d);
				}
			}
			//set ouputs
		}else{
			//no correctObject To get;
		}

		long end = System.currentTimeMillis();
		
		logwindow.addText("total: "+(end-begin)+"ms| ");
		logwindow.addText("Sending: "+(afterSend-begin)+"ms| ");
		logwindow.addText("waiting: "+(afterWaitObj-afterSend)+"ms| ");
		logwindow.addText("settingoutput: "+(end-afterWaitObj)+"ms\n");

		//end
	}



}

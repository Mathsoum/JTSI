package jtsi.test;

import java.util.ArrayList;
import java.util.HashMap;

import network.Client;
import network.DataType;

public class TestNetworkXML {
	public TestNetworkXML() {
		Client client = new Client("192.168.1.14", 4444, DataType.XML);
		System.out.println("Sending :");
		HashMap<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("a", new Integer(13));
		inputs.put("b", new Integer(37));
		inputs.put("c", new Double(23));
		ArrayList<Double> array = new ArrayList<Double>();
		array.add(new Double(1));
		array.add(new Double(2));
		array.add(new Double(3));
		inputs.put("darray", array);
		inputs.put("e", new Double(42));
		for(String key : inputs.keySet()) {
			System.out.println(key+" => "+inputs.get(key));
		}
		client.sendToServer(inputs);
		System.out.print("Waiting for outputs...");
		HashMap<String, Object> outputs = client.waitOutputs();
		System.out.println(" OK");
		for(String key : outputs.keySet()) {
			System.out.println(key+" => "+outputs.get(key));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestNetworkXML();
	}
}

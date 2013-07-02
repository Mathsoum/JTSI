package jtsi.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import network.Client;
import network.DataType;

public class TestJsonCommunication {
	
	public TestJsonCommunication() {
		super();
		Client client = new Client("192.168.1.10", 4444, DataType.JSON);
		System.out.print("Sending inputs... ");
		Map<String, Object> mapToSend = new HashMap<String, Object>();
		mapToSend.put("a", new Double(13));
		mapToSend.put("b", new Double(37));
		mapToSend.put("c", new Double(42));
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(new Double(1));
		list.add(new Double(2));
		list.add(new Double(3));
		list.add(new Double(4));
		mapToSend.put("d", list);
		System.out.println("Input map : "+mapToSend);
		client.sendToServer(mapToSend);
		System.out.println("done!");

		System.out.print("Waiting outputs... ");
		Map<String, Object> response = client.waitOutputs();
		System.out.println("JSONObject : "+JSONObject.toJSONString(response));
		System.out.println("Array class : "+response.get("outarray").getClass());
		System.out.println("done!");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestJsonCommunication();
	}

}

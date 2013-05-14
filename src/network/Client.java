package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import network.builders.AbstractBuilder;
import network.builders.JsonBuilder;
import network.builders.ObjectBuilder;
import network.builders.RawTextBuilder;
import network.builders.XmlBuilder;
import network.transmitters.AbstractTransmitter;
import network.transmitters.JsonTransmitter;
import network.transmitters.ObjectTransmitter;
import network.transmitters.RawTextTransmitter;
import network.transmitters.XmlTransmitter;

public class Client {

	private Socket socket;
	private AbstractTransmitter transmitter;
	private AbstractBuilder builder;

	public Client(String host, int port, DataType dataType) {
		initNetwork(host, port);
		switch (dataType) {
		case JSON:
			transmitter = new RawTextTransmitter(socket);
			builder = new JsonBuilder();
			break;
		case RAW_STRING:
			transmitter = new RawTextTransmitter(socket);
			builder = new RawTextBuilder();
			break;
		case SERIALIZED_OBJECT:
			transmitter = new ObjectTransmitter(socket);
			builder = new ObjectBuilder();
			break;
		case XML:
			transmitter = new RawTextTransmitter(socket);
			builder = new XmlBuilder();
			break;
		default:
			transmitter = null;
			builder = null;
			break;
		}
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @return negative number in case of error, 0 or positive number in case of success
	 */
	private int initNetwork(String host,int port) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			return -1;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "+ host);
			return -2;
		}
		return 1;
	}
	/**
	 * 
	 * @return >= 0 means there is a object to read
	 * < 0 means there is an Error 
	 * 
	 * 	 */
	public Map<String, Object> waitOutputs(){
		return builder.handle(transmitter.receive());
	}

	public void sendToServer(Map<String, Object> formattedInputs){
		transmitter.send(builder.build(formattedInputs));
	}
}

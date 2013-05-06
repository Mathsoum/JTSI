package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import network.DataType;
import network.builders.JsonBuilder;
import network.builders.ObjectBuilder;
import network.builders.AbstractBuilder;
import network.builders.RawTextBuilder;
import network.builders.XmlBuilder;
import network.transmitters.AbstractTransmitter;
import network.transmitters.JsonTransmitter;
import network.transmitters.ObjectTransmitter;
import network.transmitters.RawTextTransmitter;
import network.transmitters.XmlTransmitter;
import panel.TextLogWindow;

public abstract class Server {
	private TextLogWindow log;

	protected DataType dataType;
	protected Socket clientSocket;
	protected AbstractTransmitter transmitter;
	protected AbstractBuilder builder;

	private void initBuilderAndTransmitter() {
		switch (dataType) {
		case JSON:
			transmitter = new JsonTransmitter(clientSocket);
			builder = new JsonBuilder();
			break;
		case RAW_STRING:
			transmitter = new RawTextTransmitter(clientSocket);
			builder = new RawTextBuilder();
			break;
		case SERIALIZED_OBJECT:
			transmitter = new ObjectTransmitter(clientSocket);
			builder = new ObjectBuilder();
			break;
		case XML:
			transmitter = new XmlTransmitter(clientSocket);
			builder = new XmlBuilder();
			break;
		default:
			transmitter = null;
			builder = null;
			break;
		}
	}
	
	public Server(TextLogWindow _log) {
		super();
		this.log = _log;
	}

	/**
	 * 
	 * @param port
	 * @return
	 */
	protected int initConnection(int port){
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			log.println("Waiting for connection on port "+port);
			clientSocket = serverSocket.accept();
			log.println("Incomming connection");
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
			return -1;
		}
		return 1;
	}
	
	protected void closeConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
	}
	
	protected HashMap<String, Object> formatOutputs() {
		HashMap<String, Object> returnPacket = new HashMap<String, Object>();
		ArrayList<Double> outputArray = new ArrayList<Double>();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		Double i = (double) cal.get(Calendar.MINUTE);
		outputArray.add(i);
		returnPacket.put("outarray", outputArray);
		return returnPacket;
	}

	public void run(int port) {
		initConnection(port);
		initBuilderAndTransmitter();
		boolean _continue = true;
		while(_continue) {
			System.out.println("Waiting for inputs...");
			HashMap<String, Object> inputs = builder.handle(transmitter.receive());
			if(inputs == null) {
				_continue = false;
				log.print("Connection interupted");
				break;
			}
			System.out.println("Reception complete!");
			transmitter.send(builder.build(formatOutputs()));
			System.out.println("Outputs sent!");
		}
		closeConnection();
	}
}

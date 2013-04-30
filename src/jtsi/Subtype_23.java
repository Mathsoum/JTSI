package jtsi;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Subtype_23 extends Subtype {

	public Subtype_23(TrnsysInterface _interface) {
		super(_interface);
		try {
			Socket sock = new Socket("192.168.1.10", 1337);
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			xml.append("<inputs>\n");
			xml.append("\t<input id=\"1\">13</input>\n");
			xml.append("\t<input id=\"2\">37</input>\n");
			xml.append("\t<input id=\"3\">42</input>\n");
			xml.append("\t<input id=\"4\">23</input>\n");
			xml.append("\t<input id=\"5\">51</input>\n");
			xml.append("</inputs>\n");
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(xml.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Subtype_23(null);
	}

}

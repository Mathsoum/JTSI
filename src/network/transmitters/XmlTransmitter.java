package network.transmitters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class XmlTransmitter extends AbstractTransmitter {
	public XmlTransmitter(Socket sock) {
		super(sock);
	}

	@Override
	public void send(Object xmlContent) {
		String xml = (String) xmlContent;
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			bw.write(xml);
			bw.flush();
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
	}

	@Override
	public Object receive() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String current = br.readLine();
			while(current != null && !current.equals("</xml>")) {
				builder.append(current);
				builder.append('\n');
				current = br.readLine();
			}
			if (current == null) {
				return null;
			}
			builder.append(current);
			return builder.toString();
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
		return null;
	}
}

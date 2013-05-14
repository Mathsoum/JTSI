package network.transmitters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class RawTextTransmitter extends AbstractTransmitter {
	public RawTextTransmitter(Socket sock) {
		super(sock);
	}

	@Override
	public void send(Object xmlContent) {
		String xml = (String) xmlContent;
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			bw.write(xml);
			bw.write('\n');
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
			return br.readLine();
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
		return null;
	}
}

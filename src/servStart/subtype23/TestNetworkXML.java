package servStart.subtype23;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import jtsi.util.subtype23.XMLStringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestNetworkXML {
	public TestNetworkXML() {
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(1337);
			Socket cliSock = servSock.accept();
			ObjectInputStream ois = new ObjectInputStream(cliSock.getInputStream());
			String str = (String) ois.readObject();
			System.out.println(str);
			Document xml = XMLStringReader.parseString(str);
			Element root = xml.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("input");
			for(int i=0; i<nodeList.getLength(); ++i) {
				System.out.println("Node "+i+" <"+nodeList.item(i).getAttributes().getNamedItem("id")+"> ==> "+nodeList.item(i).getTextContent());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				servSock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestNetworkXML();
	}

}

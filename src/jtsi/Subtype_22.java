package jtsi;

import jtsi.util.subtype22.XMLStringReader;
import network.Client;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Subtype_22 extends Subtype_2 {

	public Subtype_22(TrnsysInterface _interface) {
		super(_interface);
	}

	@Override
	protected String buildPacket() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		builder.append("<inputs>\n");
		for(int i=0; i<trnsysInterface.getNbInputs(); ++i) {
			builder.append("\t<input id=\""+i+"\">"+trnsysInterface.getXin(i)+"</input>\n");
		}
		builder.append("</inputs>\n");
		return builder.toString();
	}

	/**
	 * One simulation step
	 *   * Send inputs to server
	 *   * Wait for response (outputs)
	 *   * Parse XML response
	 *   * Set TRNSys output array
	 */
	@Override
	public void step() {
		++stepCount;
		Client client = getNetwork();
		if(client != null) {
			client.sendToServer(buildPacket());
			Object response = client.waitInput();
			if(response != null && response instanceof String) {
				Document xml = XMLStringReader.parseString((String) response);
				NodeList outputs = xml.getDocumentElement().getChildNodes();
				for(int i=0; i<outputs.getLength(); ++i) {
					trnsysInterface.setXout(i, new Double(outputs.item(i).getTextContent()).doubleValue());
				}
			}
		}
	}
}

package network.inputHandler;

public class XmlHandler implements NetworkHandler {

	@Override
	public void handle(Object o) {
		System.out.println("Handling XML");
	}

	@Override
	public Object buildResponsePacket() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		builder.append("<ouputs>\n");
		builder.append("\t<ouput id=\"1\">42</output>\n");
		builder.append("</outputs>\n");
		return builder.toString();
	}
}

package network.inputHandler;

public class JsonHandler implements NetworkHandler {

	@Override
	public void handle(Object o) {
		System.out.println("Handling JSON");
	}

	@Override
	public Object buildResponsePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}

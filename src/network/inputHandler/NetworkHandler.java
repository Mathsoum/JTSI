package network.inputHandler;

public interface NetworkHandler {

	public abstract void handle(Object o); 
	public abstract Object buildResponsePacket();
}

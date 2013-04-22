package servStart.server;

public interface ObjectHandler {

	public abstract void handle(Object o); 
	public abstract Object buildResponsePacket();
}

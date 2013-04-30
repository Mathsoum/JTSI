package network.inputHandler;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ObjectHandler implements NetworkHandler {

	@Override
	public void handle(Object object) {
		System.out.println("Handling object");
	}

	@Override
	public Object buildResponsePacket() {
		HashMap<String, Object> returnPacket = new HashMap<String, Object>();
		Double[] outputArray = new Double[1];
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		double i = cal.get(Calendar.MINUTE);
		outputArray[0] = i;
		returnPacket.put("outarray", outputArray);
		return returnPacket;
	}
}

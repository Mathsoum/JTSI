package servStart.server;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import panel.TextLogWindow;

public class Main_TestObjectServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TextLogWindow tlw = new TextLogWindow();
		ObjectServer os = new ObjectServer(tlw);
		
		os.initConnection(4444);
		os.waitForIncomingObject(new ObjectHandler() {
			
			@Override
			public void handle(Object o) {
				//je suppose que l'objet est une hashtable
				System.out.println("loooool");
				
			}
			
			@Override
			public Object buildResponsePacket(){
				Hashtable<String, Object> returnPacket = new Hashtable<String, Object>();
				
				Double[] outputArray = new Double[1];
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTimeInMillis(System.currentTimeMillis());
				double i = cal.get(Calendar.MINUTE);
				outputArray[0] = i;
				
				returnPacket.put("outarray", outputArray);
				
				return returnPacket;
			}
			
		});
		

	}

}

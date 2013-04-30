package jtsi;

import jtsi.util.LogWindow;
import jtsi.util.subtype1.Window;

public class Subtype_1 extends Subtype {
	public Window window;
	
	public LogWindow logwindow ;
	protected int stepcounter = 0;
	public Subtype_1(TrnsysInterface _interface) {
		super(_interface);
		window = new Window();
		
		logwindow = new LogWindow();
	}
	
	
	@Override
	public void step() {
		
		window.notReady = true;
		
		int hourSinceStart = trnsysInterface.getTimestep();
		stepcounter ++;
		
		logwindow.addText("step "+stepcounter);
		window.timestep().setText("hour: "+ hourSinceStart + "  /n step:"+stepcounter);
		
		while (window.notReady && !window.playMode){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

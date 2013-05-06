package jtsi;

import java.lang.reflect.Constructor;

public class TrnsysInterface {
	private double[] xin;
	private double[] xout;
	private int[] info;
	private double[] parameter;
	private Subtype subtype;
	private int timestep;
	
	
	public TrnsysInterface(int _uniqueId) {
		try {
			Class<?> classe = Class.forName("jtsi.Subtype_"+_uniqueId);
			Constructor<?> c = classe.getConstructor(TrnsysInterface.class);
			Object args[] = {this};
			subtype = (Subtype) c.newInstance(args);
		} catch (Exception e) {
			
		}
	}
	
	/*
	 * method used by C :
	 * 
	 * 	jmethodID setXin = je->GetMethodID(je->FindClass(LOADEDCLASS),"setXin","([D)V");
		jmethodID setTimestep = je->GetMethodID(je->FindClass(LOADEDCLASS),"setTimestep","(I)V");
		jmethodID step = je->GetMethodID(je->FindClass(LOADEDCLASS),"step","()V");
		jmethodID getXout = je->GetMethodID(je->FindClass(LOADEDCLASS),"getXout","()[D");
	 */
	public void setXin(double _xin[]) {
		xin = _xin;
	}
	public void step() {
		subtype.step();
	}
	public double[] getXout() {
		return xout;
	}
	public void setTimestep(int _timestep) {
		timestep = _timestep;
	}
	
	
	
	
	public double getXin(int _i) {
		if (_i<xin.length)
			return xin[_i];
		return 0;
	}
	public int getInfo(int _i) {
		if (_i<info.length)
			return info[_i];
		return 0;
	}
	public double getParameter(int _i) {
		return parameter[_i];
	}
	public void setXout(int _i, double _value) {
		if (_i < xout.length)
			xout[_i] = _value;
	}
	public void setInfo(int _info[]) {
		info = _info;
		prepareOutput();
	}
	private void prepareOutput() {
		xout = new double[info[5]];
	}
	public void setParameters(double _parameter[]) {
		parameter = _parameter;
	}
	
	/**
	 * Hour since simulation started
	 * @return
	 */
	public int getTimestep() {
		return timestep;
	}
	/**
	 * Returns the number of outputs
	 * @return
	 */
	public int getNbOutputs() {
		return (int) parameter[1];
	}
	/**
	 * Returns the number of inputs
	 * @return
	 */
	public int getNbInputs() {
		return (int)parameter[0];
	}
	
	
}

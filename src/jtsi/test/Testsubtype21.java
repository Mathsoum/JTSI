package jtsi.test;

import jtsi.TrnsysInterface;

public class Testsubtype21 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double [] paramsArray = {1d,1d};
		double [] inarray = {1d};
		
		TrnsysInterface trnsysIface = new TrnsysInterface(21);
		trnsysIface.setParameters(paramsArray);
		trnsysIface.setXin(inarray);
		
		for(int i = 0 ; i< 100 ; i++){
			trnsysIface.step();
		}
		System.out.println("END  OF TEST");
	}
}

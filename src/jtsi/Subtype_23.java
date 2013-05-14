package jtsi;

import network.DataType;

public class Subtype_23 extends Subtype_2 {
	public Subtype_23(TrnsysInterface _interface) {
		super(_interface);
		initClient(DataType.JSON);
	}
}

package jtsi;

import network.DataType;

public class Subtype_21 extends Subtype_2 {
	public Subtype_21(TrnsysInterface _interface) {
		super(_interface);
		initClient(DataType.SERIALIZED_OBJECT);
	}
}

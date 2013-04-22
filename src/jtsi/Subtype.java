package jtsi;



public abstract class Subtype {
	protected TrnsysInterface trnsysInterface;
	
	public Subtype(TrnsysInterface _interface) {
		trnsysInterface = _interface;
	}
	public abstract void step();
}

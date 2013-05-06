package network.builders;

import java.util.HashMap;

public class ObjectBuilder extends AbstractBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> handle(Object object) {
		if(object != null && object instanceof HashMap<?,?>) {
			return (HashMap<String, Object>) object;
		}
		return null;
	}

	@Override
	public Object build(HashMap<String, Object> formattedInputs) {
		return formattedInputs;
	}

}

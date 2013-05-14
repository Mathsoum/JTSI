package network.builders;

import java.util.HashMap;
import java.util.Map;

public class ObjectBuilder extends AbstractBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> handle(Object object) {
		if(object != null && object instanceof HashMap<?,?>) {
			return (HashMap<String, Object>) object;
		}
		return null;
	}

	@Override
	public Object build(Map<String, Object> formattedInputs) {
		return formattedInputs;
	}

}

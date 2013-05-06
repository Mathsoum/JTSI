package network.builders;

import java.util.HashMap;

public abstract class AbstractBuilder {
	public abstract HashMap<String, Object> handle(Object rawObject);
	public abstract Object build(HashMap<String, Object> formattedData);
}

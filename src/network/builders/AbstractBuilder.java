package network.builders;

import java.util.Map;

public abstract class AbstractBuilder {
	public abstract Map<String, Object> handle(Object rawObject);
	public abstract Object build(Map<String, Object> formattedData);
}

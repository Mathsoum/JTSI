package network.builders;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonBuilder extends AbstractBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> handle(Object receivedObject) {
		if(receivedObject != null && receivedObject instanceof String) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) parser.parse((String) receivedObject);
				System.out.println("Handled : "+jsonObject.toJSONString());
				return (Map<String,Object>) jsonObject;
			} catch (ParseException e) {
				System.err.println("JSON parser exception : "+e.getMessage());
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object build(Map<String, Object> formattedInputs) {
		JSONObject builder = new JSONObject();
		builder.putAll(formattedInputs);
		System.out.println("Built : "+builder.toJSONString());
		return builder.toJSONString();
	}

}

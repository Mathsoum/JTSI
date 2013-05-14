package network.builders;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import jtsi.util.subtype22.XmlStringReader;

public class XmlBuilder extends AbstractBuilder {

	@Override
	public Map<String, Object> handle(Object receivedObject) {
		if(receivedObject != null && receivedObject instanceof String) {
			XmlStringReader reader = new XmlStringReader((String) receivedObject);
			return reader.getMapNodeValues();
		}
		return null;
	}

	@Override
	public Object build(Map<String, Object> formattedData) {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		builder.append("<xml>");
		for(String str : formattedData.keySet()) {
			if(formattedData.get(str) instanceof Collection) {
				builder.append(buildArray(str, (Collection<?>) formattedData.get(str)));
			} else {
				builder.append("<"+str+">");
				builder.append(formattedData.get(str));
				builder.append("</"+str+">");
			}
		}
		builder.append("</xml>");
		return builder.toString();
	}

	private String buildArray(String str, Collection <?> collection) {
		StringBuilder builder = new StringBuilder();
		for(Iterator<?> it = collection.iterator() ; it.hasNext() ; ) {
			Object current = it.next();
			builder.append("<"+str+">");
			if (current instanceof Collection) {
				builder.append(buildArray("sub_"+str, (Collection<?>) current));
			} else {
				builder.append(current);
			}
			builder.append("</"+str+">");
		}
		return builder.toString();
	}

}

package network.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jtsi.util.subtype22.XmlStringReader;

public class XmlBuilder extends AbstractBuilder {

	@Override
	public HashMap<String, Object> handle(Object receivedObject) {
		if(receivedObject != null && receivedObject instanceof String) {
			XmlStringReader reader = new XmlStringReader((String) receivedObject);
			List<String> nodes = reader.getNodes();
			List<String> values = reader.getValues();
			HashMap<String, Object> ret = new HashMap<>();
			for(int i=0; i<nodes.size(); ++i) {
				if(nodes.get(i).contains("array")) {
					String valArrayS = values.get(i);
					List<String> valListS = Arrays.asList(valArrayS.substring(1, valArrayS.length()-2).split(", "));
					List<Double> valList = new ArrayList<>();
					for(String str : valListS) {
						valList.add(new Double(str));
					}
					ret.put(nodes.get(i), valList);
				} else {
					ret.put(nodes.get(i), new Double(values.get(i)));
				}
			}
			return ret;
		}
		return null;
	}

	@Override
	public Object build(HashMap<String, Object> formattedData) {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		builder.append("<xml>\n");
		for(String str : formattedData.keySet()) {
			builder.append("\t<"+str+">"+formattedData.get(str)+"</"+str+">\n");
		}
		builder.append("</xml>\n");
//		System.out.println("Built string :\n"+builder.toString());
		return builder.toString();
	}

}

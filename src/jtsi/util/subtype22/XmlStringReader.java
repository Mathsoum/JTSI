package jtsi.util.subtype22;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlStringReader {
	private Map<String, Object> mapNodeValues;
	
	private Map<String, Object> parseContent(Node node) {
		NodeList nodeList = node.getChildNodes();
		Map<String, Object> mapNodeValues = new HashMap<String, Object>();
		for(int i=0; i<nodeList.getLength(); ++i) {
			if(nodeList.item(i).getNodeType() == Element.ELEMENT_NODE) {
				NodeList currentTagList =
						((Element) node).getElementsByTagName(nodeList.item(i).getNodeName());
				Node tagNode = currentTagList.item(0);
				String tagName = tagNode.getNodeName();
				if(currentTagList.getLength() > 1) {
					if(!mapNodeValues.containsKey(tagName)) {
						mapNodeValues.put(tagName, parseArray(currentTagList));
					}
				} else {
					try {
						mapNodeValues.put(tagName, new Double(Double.parseDouble(tagNode.getTextContent())));
					} catch (NumberFormatException e) {
						mapNodeValues.put(tagName, tagNode.getTextContent());
					}
				}
			}
		}
		return mapNodeValues;
	}
	
	private List<Object> parseArray(NodeList nodeList) {
		List<Object> list = new ArrayList<Object>();
		for(int i=0; i<nodeList.getLength(); ++i) {
			System.out.println("Parsing array, node "+i+" : type = "+nodeList.item(i).getNodeType()+" element = "+Node.ELEMENT_NODE+" // text = "+nodeList.item(i).getTextContent());
			if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if(nodeList.item(i).hasChildNodes()
						&& nodeList.item(i).getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
					list.add(parseContent(nodeList.item(i)));
				}
				try {
					list.add(new Double(Double.parseDouble(nodeList.item(i).getTextContent())));
				} catch (NumberFormatException e) {
					list.add(nodeList.item(i).getTextContent());
				}
			}
		}
		return list;
	}

	public XmlStringReader(String str) {
		mapNodeValues = parseContent(parseString(str).getDocumentElement());
	}
	
	public Map<String, Object> getMapNodeValues() {
		return mapNodeValues;
	}

	public Document parseString(String str) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(str));
	        return builder.parse(is);
		} catch (ParserConfigurationException e) {
			System.err.println("Parser config exception : "+e.getMessage());
		} catch (SAXException e) {
			System.err.println("SAX exception : "+e.getMessage());
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
		return null;
	}
}

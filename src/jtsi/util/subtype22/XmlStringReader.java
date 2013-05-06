package jtsi.util.subtype22;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlStringReader {
	private Document xml;
	private List<String> nodes;
	private List<String> values;
	
	private void parseContent() {
		NodeList nodeList = xml.getDocumentElement().getChildNodes();
		nodes = new ArrayList<String>();
		values = new ArrayList<String>();
		for(int i=0; i<nodeList.getLength(); ++i) {
			Node currentNode = nodeList.item(i);
			if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
				nodes.add(currentNode.getNodeName());
				values.add(currentNode.getTextContent());
			}
		}
	}
	
	public XmlStringReader(String str) {
		xml = parseString(str);
		parseContent();
	}
	
	public List<String> getNodes() {
		return nodes;
	}

	public List<String> getValues() {
		return values;
	}

	public static Document parseString(String str) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(str));
	        return builder.parse(is);
		} catch (ParserConfigurationException e) {
			System.err.println("Parsing config exception : "+e.getMessage());
		} catch (SAXException e) {
			System.err.println("SAX exception : "+e.getMessage());
		} catch (IOException e) {
			System.err.println("I/O exception : "+e.getMessage());
		}
		return null;
	}
}

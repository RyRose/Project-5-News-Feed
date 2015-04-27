package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import web.XmlParser;

public class XmlTest {
	
	
	@Test
	public void testXmlItemParsing() throws XMLStreamException, IOException {
		URL url = getClass().getResource("test.xml");
		XmlParser parser = new XmlParser();
		System.out.println(parser.readLink(url));
	}

}

package test;

import static org.junit.Assert.*;
import interfaces.Feed;

import java.io.IOException;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import models.FeedImpl;

import org.junit.Test;

import web.RssThread;

public class XmlTest {
	
	URL url = getClass().getResource("test.xml");
	
	@Test
	public void testXmlItemParsing() throws XMLStreamException, IOException, InterruptedException {
		RssThread parser = new RssThread(url.toString(), null);
		parser.readLink(url.toString());
	}

}

package web;

import interfaces.Article;
import interfaces.Feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import models.ArticleImpl;
import models.FeedImpl;


public class XmlParser {
	
	private boolean isPullingArticles; // For testing's sake so I don't have to wait for download
	
	public XmlParser() {
		isPullingArticles = true;
	}
	
	public void disableArticlePulling() {
		isPullingArticles = false;
	}
	
	public void enableArticlePulling() {
		isPullingArticles = true;
	}

	public Feed readLink( String xmlLink ) throws XMLStreamException, IOException {
		return readLink( new URL(xmlLink) );
	}

	public Feed readLink( URL xmlLink ) throws XMLStreamException, IOException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = xmlLink.openStream();
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		Feed feed = new FeedImpl();
		feed.setRssLink( xmlLink.toString() );

		XMLEvent event;
		while (eventReader.hasNext()) {
			event = eventReader.nextEvent();
			if (event.isStartElement()) {
				switch( event.asStartElement().getName().getLocalPart() ) {
				case "item":
					feed.add( getArticle(eventReader) );
					break;
				case "title":
					feed.setTitle( extractData(eventReader) );
					break;
				case "description":
					feed.setDescription( extractData(eventReader) );
					break;
				case "link":
					feed.setSiteLink( extractData(eventReader) );
					break;
				case "pubDate":
					feed.setDate( extractData(eventReader) );
					break;
				case "language":
					feed.setLanguage( extractData(eventReader) );
					break;
				case "copyright":
					feed.setCopyright( extractData(eventReader) );
					break;
				}
			} else if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == "channel") {
						return feed;
					} 
			}
		}
		
		throw new IOException();
	}

	private Article getArticle( XMLEventReader reader ) throws XMLStreamException, IOException {
		Article article = new ArticleImpl();
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			
			if (event.isStartElement()) {
				switch( event.asStartElement().getName().getLocalPart() ) {
				case "link":
					article.setLink( extractData(reader) );
					break;
				case "title":
					article.setTitle( extractData(reader) );
					break;
				case "description":
					article.setDescription( extractData(reader) );
					break;
				case "pubDate":
					article.setDate( extractData(reader) );
					break;
				case "author":
					article.setAuthor( extractData(reader) );
					break;
				}
			}
			
			if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
								
				if (endElement.getName().getLocalPart() == "item") {
					
					if (isPullingArticles)
						article.setText( getArticleText(article.getLink()) );
					
					return article;
				} 
			} 
		}
		
		throw new XMLStreamException();
	}
	
	private String getArticleText( String link ) throws IOException { // TODO: Thread it or shred it. It is slowing down everything.
		URL url = new URL(link);
		InputSource source = new InputSource();
		
		source.setEncoding("UTF-8");
		source.setByteStream(url.openStream());
		
		BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
		try {
			return extractor.getText(source);
		} catch (BoilerpipeProcessingException e) {
			return "";
		}
	}
	

	private String extractData( XMLEventReader reader ) throws XMLStreamException {
		XMLEvent event = reader.nextEvent();
		return event.isEndElement() ? "" : event.asCharacters().getData(); // <-- If the content is blank, the reader moves over to the next
	}																	   // element instead of to the blank content. This takes care of that.
}

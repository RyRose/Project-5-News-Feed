package web;

import interfaces.Article;
import interfaces.Feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import models.ArticleImpl;
import models.FeedImpl;


public class XmlParser {
	
	TableColumn<Article, String> column;
	ObservableList<Article> articles;
	
	public XmlParser() {
		column = null;
	}
	
	public XmlParser(TableColumn<Article, String> tableColumn, ObservableList<Article> articles) {
		column = tableColumn;
		this.articles = articles;
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
			} else if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
								
				if (endElement.getName().getLocalPart() == "item") {
					new ArticlePuller(article, column, articles).start();
					return article;
				} 
			} 
		}
		
		throw new XMLStreamException();
	}	

	private String extractData( XMLEventReader reader ) throws XMLStreamException {
		XMLEvent event = reader.nextEvent();
		return event.isEndElement() ? "" : event.asCharacters().getData(); // <-- If the content is blank, the reader moves over to the next
	}																	   // element instead of to the blank content. This takes care of that.
}

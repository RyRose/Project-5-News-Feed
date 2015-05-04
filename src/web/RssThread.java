package web;

import interfaces.Article;
import interfaces.Feed;
import interfaces.FeedListener;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import models.ArticleImpl;
import models.FeedImpl;


public class RssThread extends Thread {
	
	private int numArticlesLeft;
	private FeedListener listener;
	private String rssLink;
	
	private ArrayBlockingQueue<Article> articleQueue;
		
	public RssThread( String rssLink, FeedListener listener ) {
		this.rssLink = rssLink;
		articleQueue = new ArrayBlockingQueue<Article>(4);
		numArticlesLeft = 0;
		this.listener = listener;
	}
	
	@Override
	public void run() {
		try {
			readLink(rssLink);
		} catch (XMLStreamException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void readLink( String xmlLink ) throws XMLStreamException, IOException, InterruptedException {
		readLink( new URL(xmlLink) );
	}

	public void readLink( URL xmlLink ) throws XMLStreamException, IOException, InterruptedException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		URLConnection in = xmlLink.openConnection();
		XMLEventReader eventReader = inputFactory.createXMLEventReader( in.getInputStream() );
		Feed feed = new FeedImpl();
		feed.setRssLink( xmlLink.toString() );

		XMLEvent event;
		while (eventReader.hasNext()) {
			event = eventReader.nextEvent();
			if (event.isStartElement()) {
				switch( event.asStartElement().getName().getLocalPart() ) {
				case "item":
					extractArticle(eventReader);
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
						finalize(feed);
					} 
			}
		}
	}

	private void extractArticle( XMLEventReader reader ) throws XMLStreamException, IOException {
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
				if (endElement.getName().getLocalPart().equals("item")) {
					numArticlesLeft++;
					new ArticlePuller(article, articleQueue).start();
					return;
				} 
			} 
		}
		
		throw new XMLStreamException();
	}	

	private String extractData( XMLEventReader reader ) throws XMLStreamException {
		XMLEvent event = reader.nextEvent();
		return event.isEndElement() ? "" : event.asCharacters().getData(); // <-- If the content is blank, the reader moves over to the next
	}																	   // element instead of to the blank content. This takes care of that.
	
	private void finalize( Feed feed ) throws InterruptedException {
		while( numArticlesLeft > 0 ) {
			feed.add( articleQueue.take() );
			numArticlesLeft--;
		}
		
		Platform.runLater( () -> {
			listener.addFeed(feed);
		} );
	}
}

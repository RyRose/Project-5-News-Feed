package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import interfaces.ArticleView;
import interfaces.Feed;
import database.Database;
import javafx.collections.ObservableList;
import web.XmlParser;

public class FeedManager {
	
	private XmlParser parser;
	private Database database;
	
	public FeedManager(ObservableList<ArticleView> articles) {
		parser = new XmlParser(articles);
		try {
			database = new Database("news.db");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Feed getFeed(String rss_link) throws XMLStreamException, IOException {
		Feed feed;
		// TODO: Check if a feed in database. Then, return it along with its articles if it does.
		
		// Else, it pulls it from the web. 
		feed = parser.readLink(rss_link);
		
		// TODO: then it adds the feed to the database along with its articles. Note, need to delay adding to database because
		// it takes a bit to download the articles.
		
		return feed;
	}
	
	public List<Feed> getStoredFeeds() { // TODO: returns all of the feeds in the database so they can be displayed when the application is loaded
		return null;
	}
	
}

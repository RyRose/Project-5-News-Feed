package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import interfaces.Article;
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

	public Feed getFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
		Feed feed = (database.isFeedInDB(rss_link)) ? database.getFeed(rss_link) : parser.readLink(rss_link);
		// If the feed is in the database, retrieve it from there.
		if (database.isFeedInDB(rss_link)){
			System.out.println("TRYING TO GET FEED FROM DATABASE");
			feed = database.getFeed(rss_link);
		}
		
		// Else, pull it from the web and add it to the database.
		else{
			System.out.println("TRYING TO GET FEED FROM WEB");
			feed = parser.readLink(rss_link);
			// TODO: delay for the parser to download?
			database.addFeed(rss_link, feed.getTitle());
			System.out.println("FEED ADDED TO DB");
			
			for (Article art: feed.getArticles()){
				database.addArticle(rss_link, art.getAuthor(), art.getDate(), art.getTitle(), art.getText(), art.getDescription());
			}
		}
		return feed;
	}
	
	public List<Feed> getStoredFeeds() throws SQLException {
		return database.getAllFeeds();
	}
	
}

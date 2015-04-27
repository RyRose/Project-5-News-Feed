package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import interfaces.Article;
import interfaces.Feed;
import database.Database;
import javafx.scene.control.TableColumn;
import web.XmlParser;

public class FeedManager {
	
	private XmlParser parser;
	private Database database;
	
	public FeedManager(TableColumn<Article, String> column) {
		parser = new XmlParser(column);
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
			feed = database.getFeed(rss_link);
		}
		
		// Else, pull it from the web and add it to the database.
		else{
			feed = parser.readLink(rss_link);
			// TODO: delay for the parser to download?
			database.addFeed(rss_link, feed.getTitle());
			
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

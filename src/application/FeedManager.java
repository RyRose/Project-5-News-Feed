package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import interfaces.Article;
import interfaces.Feed;
import interfaces.FeedListener;
import database.Database;
import web.RssThread;

public class FeedManager {
	
	private FeedListener listener;
	private Database database;
	
	public FeedManager(FeedListener listener) {
		this.listener = listener;
		
		try {
			database = new Database("news.db");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	public Feed getFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
//		Feed feed = (database.isFeedInDB(rss_link)) ? database.getFeed(rss_link) : parser.readLink(rss_link);
//		// If the feed is in the database, retrieve it from there.
//		if (database.isFeedInDB(rss_link)){
//			System.out.println("TRYING TO GET FEED FROM DATABASE");
//			feed = database.getFeed(rss_link);
//		}
//		
//		// Else, pull it from the web and add it to the database.
//		else{
//			System.out.println("TRYING TO GET FEED FROM WEB");
//			feed = parser.readLink(rss_link);
//			// TODO: delay for the parser to download?
//			database.addFeed(rss_link, feed.getTitle());
//			System.out.println("FEED ADDED TO DB");
//			
//			for (Article art: feed.getArticles()){
//				database.addArticle(rss_link, art.getAuthor(), art.getDate(), art.getTitle(), art.getText(), art.getDescription());
//			}
	
	public void addFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
		if (database.isFeedInDB(rss_link)) {
			listener.addFeed( getFeed(rss_link) );
			return;
		}
		System.out.println("Starting new RssThread");
		new RssThread(rss_link, listener).run();
	}
	
	public void addFeed(Feed feed) throws SQLException {
		if (database.isFeedInDB(feed.getRssLink())) {
			// TODO: remove all the articles of feed in database because this in a refresh state
			// Or, set up the database such that duplicate articles cannot be added.
			return;
		} else { // We are adding for the first time.
			System.out.println("adding feed to db");
			database.addFeed(feed.getRssLink(), feed.getTitle());
		}
		
		System.out.println("adding new feed's articles to db");
		for (Article art: feed.getArticles()){
			database.addArticle(feed.getRssLink(), art.getAuthor(), art.getDate(), art.getTitle(), art.getText(), art.getDescription());
		}
	}
	
	public Feed getFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
		if (!database.isFeedInDB(rss_link))
			throw new IllegalStateException("Feed does not exist. Add it first.");
		
		return database.getFeed(rss_link);
	}
	
	public List<Feed> getStoredFeeds() throws SQLException {
		return database.getAllFeeds();
	}
	
	public void refreshFeeds(String rss_link) {
		new RssThread(rss_link, listener).start();
	}
	
}

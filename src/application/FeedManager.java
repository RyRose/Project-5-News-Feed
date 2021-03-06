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
	
	public void addFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
		if (database.isFeedInDB(rss_link)) {
			listener.showFeed( getFeed(rss_link) );
		} else
			pullFeedFromWeb(rss_link);
	}
	
	public void addFeed(Feed feed) throws SQLException {
		if (database.isFeedInDB(feed.getRssLink())) {
			database.removeArticles(feed.getRssLink());
		} else  // We are adding for the first time.
			database.addFeed(feed.getRssLink(), feed.getTitle());
		
		for (Article art: feed.getArticles()){
			database.addArticle(feed.getRssLink(), art.getAuthor(), art.getDate(), art.getTitle(), art.getText(), art.getDescription());
		}
	}
	
	public Feed getFeed(String rss_link) throws XMLStreamException, IOException, SQLException {
		System.out.println("getFeed()");
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
	
	void pullFeedFromWeb(String rss_link){
		new RssThread(rss_link, listener).start();
	}
	
	public void resetDatabase() {
		try {
			database.deleteTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		database.createTables();
	}
}

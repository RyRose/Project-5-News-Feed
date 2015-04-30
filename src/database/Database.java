package database;

import interfaces.Article;
import interfaces.Feed;

import java.sql.*;
import java.util.ArrayList;

import models.ArticleImpl;
import models.FeedImpl;

public class Database { // TODO: Make all statements PreparedStatements to prevent SQL injection from malicious RSS feeds/ RSS links
	
	Connection con;
	Statement stat;
	ArrayList<String> feeds = new ArrayList<String>();
	
	public Database(String filename) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + filename);
        stat = con.createStatement();
        createTables();
	}
	
	public void createTables() {
		try {
			stat.executeUpdate("CREATE TABLE FeedLinkTable (RSSLink TEXT, FeedName TEXT)");
			stat.executeUpdate("CREATE TABLE ArticleTable (RSSLink TEXT, Author TEXT, Date TEXT, Title TEXT, Contents TEXT, Description TEXT)");
		}
		catch (SQLException sq){
			return;
		}
	}
	
	public void deleteTables() throws SQLException {
		stat.executeUpdate("DROP TABLE FeedLinkTable");
		stat.executeUpdate("DROP TABLE ArticleTable");
	}
	
	public void addFeed(String rssLink, String feedName) throws SQLException {
		if (!isFeedInDB(rssLink)){
			stat.executeUpdate("INSERT INTO FeedLinkTable VALUES ('" + rssLink + "', '" + feedName + "')");
			feeds.add(rssLink);
			System.out.println("DB added feed: " + rssLink);
		}
		else return;
	}
	
	public void addArticle(String rssLink, String author, String date, String title, String contents, String description) throws SQLException {
		PreparedStatement sql = con.prepareStatement("INSERT INTO ArticleTable VALUES (?, ?, ?, ?, ?, ?)");
		sql.setString(1, rssLink);
		sql.setString(2, author);
		sql.setString(3, date);
		sql.setString(4, title);
		sql.setString(5, contents);
		sql.setString(6, description);
		sql.execute();
		//stat.executeUpdate("INSERT INTO ArticleTable VALUES ('" + rssLink + "', '" + author + "', '" + date + "', '" + title + "', '" + contents + "', '" + description + "')");
	}
	
	public String getRSSLink(String feedName) throws SQLException{
		ResultSet results = stat.executeQuery("SELECT * FROM FeedLinkTable WHERE FeedName = '" + feedName + "'");
		String rssLink = results.getString("RSSLink");
		results.close();
		return rssLink;
	}
	
	public String getFeedName(String rssLink) throws SQLException{
		ResultSet results = stat.executeQuery("SELECT * FROM FeedLinkTable WHERE FeedName = '" + rssLink + "'");
		String feedName = results.getString("FeedName");
		results.close();
		return feedName;
	}
	
	public Feed getFeed(String rssLink) throws SQLException{
		Feed feed = new FeedImpl();
		
		feed.setRssLink(rssLink);
		feed.setTitle(getFeedName(rssLink));
		
		ResultSet results = stat.executeQuery("SELECT * FROM ArticleTable WHERE RSSLINK = '" + rssLink + "'");
		
		while (results.next()){
			Article article = new ArticleImpl();
			article.setTitle(results.getString("Title"));
			article.setAuthor(results.getString("Author"));
			article.setDate(results.getString("Date"));
			article.setDescription(results.getString("Description"));
			article.setText(results.getString("Contents"));
			feed.add(article);
		}
		return feed;
	}
	
	public boolean isFeedInDB(String rssLink){
		System.out.println("isFeedInDB got arg: " + rssLink);
		for (String feed : feeds){
			System.out.println("feeds list item: " + feed);
		}
		System.out.println(feeds.contains(rssLink));
		return feeds.contains(rssLink);
	}
	
	ArrayList<String> getAllRSSLinks() throws SQLException{
		ArrayList<String> links = new ArrayList<String>();
		ResultSet results = stat.executeQuery("SELECT * FROM FeedLinkTable");
		while (results.next()){
			links.add(results.getString("RSSLink"));
		}
		return links;
	}
	
	public ArrayList<Feed> getAllFeeds() throws SQLException{
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		for (String rssLink : getAllRSSLinks()){
			feeds.add(getFeed(rssLink));
		}
		return feeds;
	}
}
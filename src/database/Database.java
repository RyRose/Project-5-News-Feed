package database;

import interfaces.Article;
import interfaces.Feed;

import java.sql.*;
import java.util.ArrayList;

import models.ArticleImpl;
import models.FeedImpl;

public class Database {
	
	Connection con;
	Statement stat;
	PreparedStatement sql;
	
	public Database(String filename) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + filename);
        stat = con.createStatement();
        createTables();
	}
	
	public void createTables() {
		try {
			sql = con.prepareStatement("CREATE TABLE FeedLinkTable (RSSLink TEXT, FeedName TEXT)");
			sql.execute();
			sql = con.prepareStatement("CREATE TABLE ArticleTable (RSSLink TEXT, Author TEXT, Date TEXT, Title TEXT, Contents TEXT, Description TEXT)");
			sql.execute();
		}
		catch (SQLException sq){
			return;
		}
	}
	
	public void deleteTables() throws SQLException {
		sql = con.prepareStatement("DROP TABLE FeedLinkTable");
		sql.execute();
		sql = con.prepareStatement("DROP TABLE ArticleTable");
		sql.execute();
	}
	
	public void addFeed(String rssLink, String feedName) throws SQLException {
		if (!isFeedInDB(rssLink)){
			sql = con.prepareStatement("INSERT INTO FeedLinkTable VALUES (?, ?)");
			sql.setString(1, rssLink);
			sql.setString(2, feedName);
			sql.execute();
		}
		else return;
	}
	
	public void addArticle(String rssLink, String author, String date, String title, String contents, String description) throws SQLException {
		sql = con.prepareStatement("INSERT INTO ArticleTable VALUES (?, ?, ?, ?, ?, ?)");
		sql.setString(1, rssLink);
		sql.setString(2, author);
		sql.setString(3, date);
		sql.setString(4, title);
		sql.setString(5, contents);
		sql.setString(6, description);
		sql.execute();
	}
	
	public String getRSSLink(String feedName) throws SQLException{
		sql = con.prepareStatement("SELECT * FROM FeedLinkTable WHERE FeedName = ?");
		sql.setString(1, feedName);
		sql.execute();
		ResultSet results = sql.getResultSet();
		String rssLink = results.getString("RSSLink");
		results.close();
		return rssLink;
	}
	
	public String getFeedName(String rssLink) throws SQLException{
		sql = con.prepareStatement("SELECT * FROM FeedLinkTable WHERE RSSLink = ?");
		sql.setString(1, rssLink);
		sql.execute();
		ResultSet results = sql.getResultSet();
		String feedName = results.getString("FeedName");
		results.close();
		return feedName;
	}
	
	public Feed getFeed(String rssLink) throws SQLException{
		Feed feed = new FeedImpl();
		
		feed.setRssLink(rssLink);
		feed.setTitle(getFeedName(rssLink));
		
		sql = con.prepareStatement("SELECT * FROM ArticleTable WHERE RSSLINK = ?");
		sql.setString(1, rssLink);
		sql.execute();
		ResultSet results = sql.getResultSet();
		
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
	
	public boolean isFeedInDB(String rssLink) throws SQLException{
		sql = con.prepareStatement("SELECT * FROM FeedLinkTable WHERE RSSLink = ?");
		sql.setString(1, rssLink);
		sql.execute();
		ResultSet results = sql.getResultSet();
		return results.isBeforeFirst();
	}
	
	ArrayList<String> getAllRSSLinks() throws SQLException{
		ArrayList<String> links = new ArrayList<String>();
		sql = con.prepareStatement("SELECT * FROM FeedLinkTable");
		sql.execute();
		ResultSet results = sql.getResultSet();
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
	
	public void removeArticles(String rssLink) throws SQLException{
		sql = con.prepareStatement("DELETE FROM ArticleTable WHERE RSSLINK = ?");
		sql.setString(1, rssLink);
		sql.execute();
	}
}
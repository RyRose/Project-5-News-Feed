package test;

import static org.junit.Assert.*;
import interfaces.Article;
import interfaces.Feed;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.ArticleImpl;
import models.FeedImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Database;

public class DatabaseTest {
	private Database db;
	
	@Before
	public void createDatabase() throws ClassNotFoundException, SQLException {
		db = new Database("testdb");
	}
	
	@After
	public void deleteDatabase() throws SQLException {
		db.deleteTables();
	}

	@Test
	public void testAddFeed() throws SQLException {
		db.addFeed("rssLink", "feedName");
		assertEquals(db.getFeedName("rssLink"), "feedName");
	}
	
	@Test
	public void testAddArticle() throws SQLException {
		db.addFeed("rssLink", "feedName");
		db.addArticle("rssLink", "author", "date", "title", "contents", "description");
		ArrayList<Feed> allFeeds = db.getAllFeeds();
		Feed feed = allFeeds.get(0);
		List<Article> articles = feed.getArticles();
		Article dbArticle = articles.get(0);
		
		assertEquals(dbArticle.getAuthor(), "author");
		assertEquals(dbArticle.getDate(), "date");
		assertEquals(dbArticle.getTitle(), "title");
		assertEquals(dbArticle.getText(), "contents");
		assertEquals(dbArticle.getDescription(), "description");
	}
	
	@Test
	public void testGetRssLink() throws SQLException{
		db.addFeed("rssLink", "feedName");
		assertEquals(db.getRSSLink("feedName"), "rssLink");
	}
	
	@Test
	public void testGetFeedName() throws SQLException{
		db.addFeed("rssLink", "feedName");
		assertEquals(db.getFeedName("rssLink"), "feedName");
	}
	
	@Test
	public void testGetFeed() throws SQLException{
		db.addFeed("rssLink", "feedName");
		db.addArticle("rssLink", "author1", "date1", "title1", "contents1", "description1");
		db.addArticle("rssLink", "author2", "date2", "title2", "contents2", "description2");
		Feed dbFeed = db.getFeed("rssLink");
		
		Feed referenceFeed = buildFeed("rssLink", "feedName");
		
		assertTrue(dbFeed.equals(referenceFeed));
	}
	
	@Test
	public void testIsFeedInDB() throws SQLException{
		assertFalse(db.isFeedInDB("rssLink"));
		db.addFeed("rssLink", "feedName");
		assertTrue(db.isFeedInDB("rssLink"));
	}
	
	@Test
	public void testGetAllRSSLinks() throws SQLException{
		ArrayList<String> links = new ArrayList<String>();
		assertTrue(links.equals(db.getAllRSSLinks()));
		links.add("rssLink1");
		links.add("rssLink2");
		links.add("rssLink3");
		db.addFeed("rssLink1", "feedName1");
		db.addFeed("rssLink2", "feedName2");
		db.addFeed("rssLink3", "feedName3");
		assertTrue(links.equals(db.getAllRSSLinks()));
	}
	
	@Test
	public void testGetAllFeeds() throws SQLException{
		ArrayList<Feed> referenceFeeds = new ArrayList<Feed>();
		referenceFeeds.add(buildFeed("rssLink1", "feedName1"));
		referenceFeeds.add(buildFeed("rssLink2", "feedName2"));
		
		db.addFeed("rssLink1", "feedName1");
		db.addFeed("rssLink2", "feedName2");
		db.addArticle("rssLink1", "author1", "date1", "title1", "contents1", "description1");
		db.addArticle("rssLink1", "author2", "date2", "title2", "contents2", "description2");
		db.addArticle("rssLink2", "author1", "date1", "title1", "contents1", "description1");
		db.addArticle("rssLink2", "author2", "date2", "title2", "contents2", "description2");
		
		assertTrue(db.getAllFeeds().equals(referenceFeeds));
	}
	
	@Test
	public void testRemoveArticles() throws SQLException {
		db.addArticle("rssLink", "author1", "date1", "title1", "contents1", "description1");
		db.addArticle("rssLink", "author2", "date2", "title2", "contents2", "description2");
		db.removeArticles("rssLink");
		assertFalse(db.hasArticles());
	}
	
	@Test
	public void testHasArticles() throws SQLException {
		assertFalse(db.hasArticles());
		db.addArticle("rssLink", "author1", "date1", "title1", "contents1", "description1");
		assertTrue(db.hasArticles());
	}
	
	Feed buildFeed(String rssLink, String feedName){
		Feed feed = new FeedImpl();
		feed.setRssLink(rssLink);
		feed.setTitle(feedName);
		
		Article article1 = new ArticleImpl();
		article1.setAuthor("author1");
		article1.setDate("date1");
		article1.setTitle("title1");
		article1.setText("contents1");
		article1.setDescription("description1");
		feed.add(article1);
		
		Article article2 = new ArticleImpl();
		article2.setAuthor("author2");
		article2.setDate("date2");
		article2.setTitle("title2");
		article2.setText("contents2");
		article2.setDescription("description2");
		feed.add(article2);
		
		return feed;
	}
}

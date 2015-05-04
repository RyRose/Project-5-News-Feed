package test;

import static org.junit.Assert.*;
import interfaces.Article;
import interfaces.Feed;
import interfaces.FeedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import models.ArticleImpl;
import models.FeedImpl;

import org.junit.Test;

import web.ArticlePuller;
import web.RssThread;

public class WebTest {
	
	ArticlePuller puller = new ArticlePuller(null, null, true);
	TestListener listener = new TestListener();

	URL article_txt = getClass().getResource("article.txt");
	URL article_html = getClass().getResource("article.html");
	URL test_rss_feed = getClass().getResource("test.xml");
	
	String dummy_text = "abcdefghijklmnopqrstuvwxyz123456789";
	
	@Test
	public void testArticleParsing() throws IOException {
		StringBuffer articleText = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(article_txt.openStream()));
		
		while (reader.ready())
			articleText.append(reader.readLine() + "\n");
		
		articleText.deleteCharAt( articleText.toString().length() - 1); // deletes extra newline
		
		assertEquals(articleText.toString(), puller.getArticleText( WebTest.class.getResource("article.html").toString() ));
	}
	
	@Test (expected=MalformedURLException.class)
	public void testInvalidLink() throws IOException {
		puller.getArticleText(dummy_text);
	}
	
	@Test
	public void testXmlParsing() throws XMLStreamException, IOException, InterruptedException {
		RssThread thread = new RssThread( test_rss_feed.toString(), listener, false);
		
		thread.readLink(test_rss_feed);
				
		assertEquals( listener.feedAdded, buildTestFeed() );
	}
	
	private Feed buildTestFeed() {
		Feed feed = new FeedImpl();
		feed.setRssLink(test_rss_feed.toString());
		feed.setTitle("News Releases");
		feed.setSiteLink("https://www.hendrix.edu/news/RssFeed.ashx?fol=235");
		feed.setDescription("");
		
		Article article = new ArticleImpl();
		article.setTitle("Campus Kitty 2014-2015 Check Ceremony");
		article.setLink("https://www.hendrix.edu/news/news.aspx?id=73484");
		article.setDescription("\n");
		article.setAuthor("Robert O");
		article.setDate("Thu, 23 Apr 2015 21:24:31 GMT");
		
		feed.add(article);
		
		article = new ArticleImpl();
		article.setTitle("Hendrix Choir Spring Concert: A Gospel Celebration");
		article.setLink("https://www.hendrix.edu/news/news.aspx?id=73483");
		article.setDescription("\n");
		article.setAuthor("Robert O");
		article.setDate("Thu, 23 Apr 2015 20:58:11 GMT");
		
		feed.add(article);
		
		return feed;
	}
	
	private class TestListener implements FeedListener {

		public Feed feedAdded;
		
		@Override public void addFeed(Feed feed) { feedAdded = feed; }
		@Override public void showFeed(Feed feed) {}
	}

}

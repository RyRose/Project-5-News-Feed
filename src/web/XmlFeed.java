package web;

import models.ArticleImpl;
import models.FeedImpl;
import interfaces.Article;
import interfaces.Feed;

public class XmlFeed { // TODO: Add Jackson XML annotations to support parsing of the rss feed
	
	public XmlItem[] items;
	
	// not empty
	public String rssLink;
	public String title;
	public String description;
	public String siteLink;
	
	// Possibly empty
	public String date;
	public String language;
	public String copyright;
	
	public Feed toModel() {
		Feed feed = new FeedImpl();
		feed.setTitle(title);
		feed.setLanguage(language);
		feed.setRssLink(rssLink);
		feed.setSiteLink(siteLink);
		feed.setDate(date);
		feed.setDescription(description);
		feed.setCopyright(copyright);
		
		for( XmlItem item : items )
			feed.add(item.toModel());
		
		return feed;
	}
	
	public static class XmlItem {
		public String title;
		public String link;
		public String description;
		public String date;
		public String author;
		
		public Article toModel() {
			Article article = new ArticleImpl();
			article.setAuthor(author);
			article.setDate(date);
			article.setDescription(description);
			article.setLink(link);
			article.setTitle(title);
			return article;
		}
	}
}

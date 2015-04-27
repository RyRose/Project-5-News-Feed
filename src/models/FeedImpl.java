package models;

import java.util.ArrayList;
import java.util.List;

import interfaces.Article;
import interfaces.Feed;

public class FeedImpl implements Feed {
	
	private ArrayList<Article> articles;
	
	// not empty
	private String rssLink;
	private String title;
	private String description;
	private String siteLink;
	
	// Possibly empty
	private String date;
	private String language;
	private String copyright;
	
	public FeedImpl() {
		rssLink = "";
		title = "";
		description = "";
		siteLink = "";
		date = "";
		language = "";
		copyright = "";
		articles = new ArrayList<Article>();
	}
	
	@Override
	public int hashCode() {
		return articles.hashCode() + rssLink.hashCode() + title.hashCode() + description.hashCode() + siteLink.hashCode() + date.hashCode() 
				+ language.hashCode() + copyright.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FeedImpl) {
			FeedImpl other = (FeedImpl) obj;
			
			return  other.articles.equals(articles) &&
					other.rssLink.equals(rssLink) &&
					other.title.equals(title) &&
					other.description.equals(description) &&
					other.siteLink.equals(siteLink) &&
					other.date.equals(date) &&
					other.language.equals(language) &&
					other.copyright.equals(copyright);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ rssLink: ").append(rssLink).append(", title: ").append(title).append(", description: ");
		builder.append(description).append(", date: ").append(date);
		builder.append(", language: ").append(language).append(", copyright: ").append(copyright).append(", articles ").append(articles).append("}");
		return builder.toString();
	}
		
	@Override
	public void add( Article article ) { articles.add(article); }
	
	// Getters and Setters
	
	@Override public List<Article> getArticles() { return articles; }

	@Override public String getRssLink() { return rssLink; }
	@Override public void setRssLink(String link) { this.rssLink = link; }

	@Override public String getTitle() { return title; }
	@Override public void setTitle(String title) { this.title = title; }

	@Override public String getSiteLink() { return siteLink; }
	@Override public void setSiteLink(String link) { this.siteLink = link; }

	@Override public String getDescription() { return description; }
	@Override public void setDescription(String description) { this.description = description; }

	@Override public String getDate() { return date; }
	@Override public void setDate(String date) { this.date = date; }

	@Override public String getLanguage() { return language; }
	@Override public void setLanguage(String language) { this.language = language; }

	@Override public String getCopyright() { return copyright; }
	@Override public void setCopyright(String copyright) { this.copyright = copyright; }
}

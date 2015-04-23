package interfaces;

import java.util.List;

public interface Feed {
	
	public List<Article> getArticles();
	public void add(Article article);


	// Not empty
	public String getRssLink(); // links to the RSS feed itself
	public void setRssLink(String link);
	
	public String getTitle(); // title
	public void setTitle(String title);
	
	public String getSiteLink(); // link- links to somewhere on the site. Possibly the RSS feed but doesn't have to.
	public void setSiteLink(String link);
	
	public String getDescription(); // description
	public void setDescription(String description);

	// Possibly empty
	public String getDate(); // pubDate- the last time an article has been published
	public void setDate(String date);
	
	public String getLanguage(); // language
	public void setLanguage(String language);
	
	public String getCopyright(); // copyright
	public void setCopyright(String copyright);	
}

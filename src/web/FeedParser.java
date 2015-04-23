package web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import interfaces.Article;
import interfaces.Feed;

public class FeedParser {
	
	XmlMapper mapper;
	
	public FeedParser() {
		mapper = new XmlMapper();
	}

	public Feed parse( String rss_link ) throws MalformedURLException {
		URL url = new URL(rss_link);
		
		XmlFeed xmlFeed = new XmlFeed();
				
		try {
			xmlFeed = mapper.readValue(url, XmlFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Feed feed = xmlFeed.toModel();
		
		for( Article article : feed.getArticles() ) {
			article.setText( extractArticle( article.getLink() ) );
		}
		
		return feed;
	}
	
	public String extractArticle( String link ) { // TODO: Implement pulling html and  boilerpipe parsing of html of link when pulled
		return "";
	}
}

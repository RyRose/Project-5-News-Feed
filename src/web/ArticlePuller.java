package web;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import interfaces.Article;
import interfaces.ArticleView;

public class ArticlePuller extends Thread {

	Article article;
	ObservableList<ArticleView> articles;
	
	public ArticlePuller( Article article, ObservableList<ArticleView> articles ) {
		this.article = article;
		this.articles = articles;
	}

	@Override
	public void run() {
		try {
			article.setText( getArticleText(article.getLink()) );
			
			Platform.runLater( () -> {
				ArticleView view = new ArticleView();
				articles.add( view.convert(article) );
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getArticleText( String link ) throws IOException {
		URL url = new URL(link);
		InputSource source = new InputSource();
		
		source.setEncoding("UTF-8");
		source.setByteStream(url.openStream());
		
		BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
		try {
			return extractor.getText(source);
		} catch (BoilerpipeProcessingException e) {
			return "";
		}
	}
}

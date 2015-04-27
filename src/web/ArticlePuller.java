package web;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import interfaces.Article;

public class ArticlePuller extends Thread {

	Article article;
	TableColumn<Article, String> column;
	ObservableList<Article> articles;
	
	public ArticlePuller( Article article, TableColumn<Article, String> column, ObservableList<Article> articles ) {
		this.article = article;
		this.column = column;
		this.articles = articles;
	}

	@Override
	public void run() {
		try {
			article.setText( getArticleText(article.getLink()) );
			
			Platform.runLater( () -> {
				if (column != null) {
					articles.add( article );
					column.setVisible(false); // Gross, hacky way to refresh the tableView's columns when there is new text.
					column.setVisible(true);
				}
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

package web;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import interfaces.Article;

public class ArticlePuller extends Thread {

	Article article;
	
	public ArticlePuller( Article article ) {
		this.article = article;
	}

	@Override
	public void run() {
		try {
			article.setText( getArticleText(article.getLink()) );
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

package web;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import interfaces.Article;

public class ArticlePuller extends Thread {

	Article article;
	private ArrayBlockingQueue<Article> articleQueue;
	private boolean isPullingArticles;
	
	public ArticlePuller( Article article, ArrayBlockingQueue<Article> articleQueue, boolean isPullingArticles ) {
		this.article = article;
		this.articleQueue = articleQueue;
		this.isPullingArticles = isPullingArticles;
	}

	@Override
	public void run() {
		try {
			article.setText( getArticleText(article.getLink()) );
			articleQueue.put(article);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getArticleText( String link ) throws IOException {
		if (!isPullingArticles)
			return "";
		
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

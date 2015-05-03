package interfaces;

import javafx.scene.text.Text;

public class ArticleView {
	
	public Text title;
	public Text link;
	public Text author;
	public Text date;
	public Text description;
	public Text text;
	
	public ArticleView( Article art ) {
		title = convertToText(art.getTitle());
		link = convertToText(art.getLink());
		author = convertToText(art.getAuthor());
		date = convertToText(art.getDate());
		description = convertToText(art.getDescription());
		text = convertToText(art.getText());
		
		//Setting wrapping width based off of the columns.
		title.setWrappingWidth(200);
		author.setWrappingWidth(150);
		date.setWrappingWidth(100);
		text.setWrappingWidth(350);
	}
	
	public Text getTitle() {
		return title;
	}
	
	public Text getLink() {
		return link;
	}
	
	public Text getAuthor() {
		return author;
	}
	
	public Text getDate() {
		return date;
	}
	
	public Text getDescription() {
		return description;
	}
	
	public Text getText() {
		return text;
	}

	public Text convertToText(String string) {
		Text newText = new Text (string);
		return newText;
	}
}

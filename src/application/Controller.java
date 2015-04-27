package application;

import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import web.XmlParser;
import interfaces.Article;
import interfaces.Feed;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class Controller {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	
	//TreeTableView Table and Columns
	@FXML
	private TableView<Article> table;
	@FXML
	private TableColumn<Article, String> title;
	@FXML
	private TableColumn<Article, String> author;
	@FXML
	private TableColumn<Article, String> date;
	@FXML
	private TableColumn<Article, String> article;
	
	private TableColumn<Article, String> text;
		
	//Non FXML items
	private ObservableList<Article> articles;
	private String feedURL;
	private XmlParser parser = new XmlParser();
	
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles.\nPlease take note that pulling the article text may take a few."));
		
		//This is based off of the Article interface. If that is changed, please adjust this.
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles."));
			
		//Not sure if these declarations are correct. If not, I'll work on it when I find the fix.
		title.setCellValueFactory(new PropertyValueFactory<Article, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Article, String>("date"));
		article.setCellValueFactory(new PropertyValueFactory<Article, String>("text"));
		author.setCellValueFactory(new PropertyValueFactory<Article, String>("author"));
		
		table.setItems(articles);

		//Keylistener for enter key.
		userInput.setOnKeyPressed(event -> {
			KeyCode key = event.getCode();
			if (key == KeyCode.ENTER) {add();}
		});
	}
	
	@FXML
	public void add() {
		if (userInput.getText().length() == 0 || userInput.getText() == null) {
			userInput.setPromptText("Please enter a URL before pressing the button or enter key.");
			return;
		}
		
		feedURL = userInput.getText();
		userInput.clear();
		

		XmlParser parser = new XmlParser(text);

		Feed feed;
		List<Article> returnedArticles;
		
		try {
			feed = parser.readLink(feedURL);
			returnedArticles = feed.getArticles();
			for (Article art : returnedArticles) {
				addArticle(art);
			}
		} catch (XMLStreamException | IOException e) { // Means error in XML link or no internet available
			userInput.setText(e.toString());
			table.setPlaceholder(new Label("Looks like something went wrong."));
		}
	}

	//This can be used for testing and refreshing the feed.
	public void addRefreshed(String testFeed) {
		Feed feed;
		List<Article> returnedArticles;
		
		try {
			feed = parser.readLink(testFeed);
			returnedArticles = feed.getArticles();
			for (Article art : returnedArticles) {
				addArticle(art);
			}
		} catch (XMLStreamException | IOException e) { // Means error in XML link or no internet available
			userInput.setText(e.toString());
			table.setPlaceholder(new Label("Looks like something went wrong."));
		}
		
	}
	
	//Separate method for adding to the list of articles.
	//This is useful when testing adding to the columns.
	private void addArticle(Article art) {
		String tempString = wrapString(art.getText());
		art.setText(tempString);
		articles.add(art);
	}
	
	//Wraps the text of the article.
	//This doesn't work yet.
	private String wrapString(String newText) {
		StringBuilder sb = new StringBuilder(newText);

		int i = 0;
		while (i + 500 < sb.length() && (i = sb.lastIndexOf(" ", i + 500)) != -1) {
		    sb.replace(i, i + 1, "\n");
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	//Test method using Hendrix news feed at https://www.hendrix.edu/news/RssFeed.ashx?fol=235.
	@FXML
	private void testRSSAdding() {
		clear();
		String hendrixFeed = "https://www.hendrix.edu/news/RssFeed.ashx?fol=235";
		addRefreshed(hendrixFeed);
	}
	
	//Clears table. Used every time a user makes a request.
	@FXML
	private void clear() {
		articles.clear();
		table.setVisible(true);
	}
	
	//Quits application.
	@FXML
	public void endApplication(){
		Platform.exit();
	}

}

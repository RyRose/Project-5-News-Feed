package application;

import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import web.XmlParser;
import interfaces.Article;
import interfaces.ArticleView;
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
import javafx.scene.text.Text;

public class Controller {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	
	//TreeTableView Table and Columns
	@FXML
	private TableView<ArticleView> table;
	@FXML
	private TableColumn<ArticleView, Text> title;
	@FXML
	private TableColumn<ArticleView, Text> author;
	@FXML
	private TableColumn<ArticleView, Text> date;
	@FXML
	private TableColumn<ArticleView, Text> article;
	
		
	//Non FXML items
	private ObservableList<ArticleView> articles;
	private String feedURL;
	private FeedManager manager;
	
	@FXML
	public void initialize() {
		manager = new FeedManager(article);
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles.\nPlease take note that pulling the article text may take a few."));
		
		//This is based off of the Article interface. If that is changed, please adjust this.
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles."));
			
		//Not sure if these declarations are correct. If not, I'll work on it when I find the fix.
		title.setCellValueFactory(new PropertyValueFactory<ArticleView, Text>("title"));
		date.setCellValueFactory(new PropertyValueFactory<ArticleView, Text>("date"));
		article.setCellValueFactory(new PropertyValueFactory<ArticleView, Text>("text"));
		author.setCellValueFactory(new PropertyValueFactory<ArticleView, Text>("author"));
		
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
			feed = manager.getFeed(testFeed);
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
		ArticleView temp = new ArticleView();
		temp = temp.convert(art);
		articles.add(temp);
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

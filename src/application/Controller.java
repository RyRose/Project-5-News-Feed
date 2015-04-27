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
	private TableColumn<Article, String> descriptionAndText;
	
	//Non FXML items
	private ObservableList<Article> articles;
	private String feedURL;
	
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles."));
		
		//Not sure if these declarations are correct. If not, I'll work on it when I find the fix.
		title.setCellValueFactory(new PropertyValueFactory<Article, String>("title"));
		author.setCellValueFactory(new PropertyValueFactory<Article, String>("author"));
		date.setCellValueFactory(new PropertyValueFactory<Article, String>("date"));
		descriptionAndText.setCellValueFactory(new PropertyValueFactory<Article, String>("article_text"));
		
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
		
		XmlParser parser = new XmlParser();
		
		Feed feed;
		List<Article> returnedArticles;
		
		try {
			feed = parser.readLink(feedURL);
			returnedArticles = feed.getArticles();
			for (Article art : returnedArticles) {
				System.out.println(art);
				addArticle(art);
			}
		} catch (XMLStreamException | IOException e) { // Means error in XML link or no internet available
			userInput.setText(e.toString());
			table.setPlaceholder(new Label("Looks like something went wrong."));
		} finally {
			clear();
		}
	}
	
	//Separate method for adding to the list of articles.
	//This is useful when testing adding to the columns.
	public void addArticle(Article art) {
		articles.add(art);
	}
	
	@FXML
	public void testRSSAdding() {
	}
	
	@FXML
	public void clear() {
		articles.clear();
		table.setVisible(true);
	}
	
	@FXML
	public void endApplication(){
		Platform.exit();
	}

}

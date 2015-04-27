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
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;

public class Controller {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	
	//TreeTableView Table and Columns
	@FXML
	private TreeTableView<Article> table;
	@FXML
	private TreeTableColumn<Article, String> title;
	@FXML
	private TreeTableColumn<Article, String> author;
	@FXML
	private TreeTableColumn<Article, String> date;
	@FXML
	private TreeTableColumn<Article, String> descriptionAndText;
	
	//Non FXML items
	private ObservableList<Article> articles;
	private String feedURL;
	
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles."));
		
		//Not sure if these declarations are correct. If not, I'll work on it when I find the fix.
		title.setCellValueFactory(new TreeItemPropertyValueFactory<Article, String>("title"));
		author.setCellValueFactory(new TreeItemPropertyValueFactory<Article, String>("author"));
		date.setCellValueFactory(new TreeItemPropertyValueFactory<Article, String>("date"));
		descriptionAndText.setCellValueFactory(new TreeItemPropertyValueFactory<Article, String>("description"));
		
		
		table.getColumns().addAll(title, author, date, descriptionAndText);
		table.setVisible(true);

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
		try {
			feed = parser.readLink(feedURL);
			List<Article> returnedArticles = feed.getArticles();
			for (int i = 0; i < returnedArticles.size(); i ++) {
				addArticle(returnedArticles.get(i));
			}
		} catch (XMLStreamException | IOException e) { // Means error in XML link or no internet available
			userInput.setText(e.toString());
		} finally {
			clear();
		}
	}
	
	//Separate method for adding to the list of articles.
	//This is useful when testing adding to the columns.
	public void addArticle(Article art) {
		articles.add(art);
		
		//Creates separate tree items for all the required information.
		TreeItem<String> artTitle = new TreeItem<>(art.getTitle());
		TreeItem<String> artAuthor = new TreeItem<>(art.getAuthor());
		TreeItem<String> artDate = new TreeItem<>(art.getDate());
		TreeItem<String> artDesc = new TreeItem<>(art.getDescription());
		TreeItem<String> artText = new TreeItem<>(art.getText());
		
		//Sets the text as the child of the description.
		//This will incorporate the idea of the actual article being a dropdown item.
		artDesc.getChildren().add(artText);
		artDesc.setExpanded(false);
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

package application;

import interfaces.Article;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;

public class Controller {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	
	//TreeTableView Table and Columns
	@FXML
	private TreeTableView<Article> table;
	//Not sure if these declarations are correct. If not, I'll work on it when I find the fix.
	@FXML
	private TreeTableColumn<Article, String> title = new TreeTableColumn<>("title");
	@FXML
	private TreeTableColumn<Article, String> author = new TreeTableColumn<>("author");
	@FXML
	private TreeTableColumn<Article, String> date = new TreeTableColumn<>("date");
	@FXML
	private TreeTableColumn<Article, String> text = new TreeTableColumn<>("text");
	
	//Non FXML items
	private ObservableList<Article> articles;
	private String feedURL;
	
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your chosing above in order to view the related articles."));
		table.getColumns().addAll(title, author, date, text);
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
	}
	
	@FXML
	public void endApplication(){
		Platform.exit();
	}

}

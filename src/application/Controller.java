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

public class Controller {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	
	//TreeTableView Table and Columns
	@FXML
	private TreeTableView<Article> table;
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
	
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();	
		table.setPlaceholder(new Label("Enter the RSS feed of your chosing above in order to view the related articles."));
		table.getColumns().addAll(title, author, date, text);
		table.setVisible(true);	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@FXML
	public void endApplication(){
		Platform.exit();
	}

}

package application;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.stream.XMLStreamException;

import interfaces.Article;
import interfaces.Feed;
import interfaces.FeedListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;																																																																																																			
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class Controller implements FeedListener {
	//FXML Objects Not Involved in TreeTableView
	@FXML
	private TextField userInput;
	@FXML
	private TabPane pane;
	@FXML
	private BorderPane object;
	
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
	private SystemTrayListener sysTray;	
	private int index;
	private int tabCount = 1;
		
	@FXML
	public void initialize() {
		articles = FXCollections.observableArrayList();
		manager = new FeedManager(this);
		//index = TabIndex.getInstance().nextIndex();
		sysTray = SystemTrayListener.getInstance();
		pane.getSelectionModel().getSelectedItem().setText("Tab: " + tabCount);
		table.setPlaceholder(new Label("Enter the RSS feed of your choosing above in order to view the related articles.\nPlease take note that pulling the article text may take a few."));
		//This is based off of the Article interface. If that is changed, please adjust this.
		table.setPlaceholder(new Label("Please enter a complete URL before pressing the button or enter key.\n"
					+ "Make sure to include http:// or https:// in your URL."));
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
		
		start();
	}
	
	private void start() {
		try {
			if (manager.getStoredFeeds().size() > index) {initializeExistingInformation(manager.getStoredFeeds().get(index));}
		} catch (SQLException e) {e.printStackTrace();}	
	}
	
	private void initializeExistingInformation(Feed feed) {
		feedURL = feed.getRssLink();
		showFeed(feed);
		testRefresh();
	}
	
	@FXML
	public void add() {
		if (userInput.getText().length() == 0 || userInput.getText() == null) {
			userInput.setPromptText("Please enter a URL before pressing the button or enter key.");
			return;
		}
		feedURL = userInput.getText();
		userInput.clear();
		addUsingString(feedURL);
	}
	
	@Override
	public void addFeed( Feed feed ) {
		try {
			manager.addFeed(feed);
			sysTray.displayRefresh(feed.getTitle());
			showFeed( manager.getFeed( feed.getRssLink() ) );
		} catch (SQLException | XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void showFeed( Feed feed ) {
		clear();
		pane.getSelectionModel().getSelectedItem().setText(feed.getTitle());
		for (Article art : feed.getArticles()) {
			articles.add( new ArticleView(art) );
		}
	}

	//This can be used for testing and refreshing the feed.
	public void addUsingString(String userFeed) {		
		try {
			manager.addFeed(userFeed);
			pane.getSelectionModel().getSelectedItem().setText(userFeed);
			feedURL = userFeed;
		} catch (XMLStreamException | IOException | SQLException e) {e.printStackTrace();}
	}
	
	@FXML
	private void nextArticle() {
		if (table.getSelectionModel().getSelectedIndex() + 1 < articles.size()) {
			table.getSelectionModel().select(table.getSelectionModel().getSelectedIndex() + 1);
			table.scrollTo(table.getSelectionModel().getSelectedIndex());
		}
	}
	
	@FXML
	private void previousArticle() {
		if (table.getSelectionModel().getSelectedIndex() - 1 >= 0) {
			table.getSelectionModel().select(table.getSelectionModel().getSelectedIndex() - 1);
			table.scrollTo(table.getSelectionModel().getSelectedIndex());
		}
	}
	
	@FXML private void testRefresh() {manager.refreshFeeds(feedURL);}
	@FXML private void hideTitle() {title.setVisible(false);}
	@FXML private void hideAuthor() {author.setVisible(false);}
	@FXML private void hideDate() {date.setVisible(false);}
	@FXML private void hideArticle() {article.setVisible(false);}
	@FXML private void showTitle() {title.setVisible(true);}
	@FXML private void showAuthor() {author.setVisible(true);}
	@FXML private void showDate() {date.setVisible(true);}
	@FXML private void showArticle() {article.setVisible(true);}
	@FXML public void removeTab() {pane.getTabs().remove(pane.getSelectionModel().getSelectedItem());}
	@FXML private void getNPR() {addUsingString("http://www.npr.org/rss/rss.php?id=1001");}
	@FXML private void getHendrix() {addUsingString("https://www.hendrix.edu/news/RssFeed.ashx?fol=235");}
	@FXML private void getESPN() {addUsingString("http://sports.espn.go.com/espn/rss/news");}
	@FXML private void getGizmodo() {addUsingString("http://feeds.gawker.com/gizmodo/full#_ga=1.116383045.299019652.1421158651");}
	@FXML private void getRollingStone() {addUsingString("http://www.rollingstone.com/news.rss");}
	@FXML public void endApplication(){Platform.exit();}
	@FXML private BorderPane getBorderPane() {return object;}
	@FXML private void clearDatabase() { clear(); manager.resetDatabase();}
	@FXML private void clear() {articles.clear(); table.setVisible(true);}
	
	@FXML
	private void expandArticle() {
		String articleText = table.getSelectionModel().getSelectedItem().getText().getText();
		JTextArea msg = new JTextArea(articleText);
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(msg);
		JOptionPane.showMessageDialog(null, scrollPane);
	}
	
	@FXML
	public void addTab() { 
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.load(this.getClass().getResource("GUI Version 1.fxml").openStream());
			Controller temp = loader.getController();
			BorderPane newPane = temp.getBorderPane();
			Tab newTab = new Tab();
			newTab.setContent(newPane);
			tabCount += 1;
			newTab.setText("Tab: " + tabCount);
			pane.getTabs().add(newTab);
			pane.getSelectionModel().select(newTab);
		} catch (IOException e) {
		 	e.printStackTrace();
		}
	}
}
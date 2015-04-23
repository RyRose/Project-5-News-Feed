package interfaces;

public interface Article {

	public String getTitle(); // title- Article Title
	public void setTitle(String title);
		
	public String getLink(); // link- Link to article
	public void setLink(String link);
	
	public String getAuthor(); // author- Article Author
	public void setAuthor(String author);
	
	public String getDate(); // pubDate- date article published
	public void setDate(String date);
	
	public String getDescription();
	public void setDescription(String description); // description
	
	public String getText(); // Article Text pulled from the link
	public void setText(String article_text);
}

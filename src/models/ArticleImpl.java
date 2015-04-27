package models;

import interfaces.Article;

public class ArticleImpl implements Article {
	
	private String title;
	private String link;
	private String author;
	private String date;
	private String text;
	private String description;
	
	public ArticleImpl() {
		title = "";
		link = "";
		author = "";
		date = "";
		text = "";
		description = "";
	}
	
	@Override
	public int hashCode() {
		return title.hashCode() + link.hashCode() + author.hashCode() + date.hashCode() + text.hashCode() + description.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ArticleImpl) {
			ArticleImpl other = (ArticleImpl) obj;
			
			return  other.title.equals(title) &&
					other.link.equals(link) &&
					other.author.equals(author) &&
					other.date.equals(date) &&
					other.text.equals(text) &&
					other.description.equals(description);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n{").append(" title: ").append(title).append(", description: ").append(description);
		builder.append(", date: ").append(date).append(", link: ").append(link).append(", text: ").append(text);
		builder.append(", author: ").append(author).append("}");
		return builder.toString();
	}
	
	// Getters and Setters

	@Override public String getTitle() { return title; }
	@Override public void setTitle(String title) { this.title = title; }

	@Override public String getLink() { return link; }
	@Override public void setLink(String link) { this.link = link; }

	@Override public String getAuthor() { return author; }
	@Override public void setAuthor(String author) { this.author = author; }

	@Override public String getDate() { return date; }
	@Override public void setDate(String date) { this.date = date; }

	@Override public String getText() { return text;}
	@Override public void setText(String article_text) { this.text = article_text; }

	@Override public String getDescription() { return description; }
	@Override public void setDescription(String description) { this.description = description; }
}

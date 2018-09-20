package com.example.jnbcb.qrtextbook.query;
//modify access of getters setters as needed
import java.util.List;

/**
 * Holds information about the book and a list of query results 
 * @author jnbcb
 *
 */
public class Textbook 
{
	private List<Result> results;
	private String title;
	private String publisher;
	private String edition;
	private String yearPublished;
	private String author;
	private boolean success; // True if query produced valid results
	
	public Textbook(String title, boolean success)
	{
		this.title = title;
		this.success = success;
	}

	public List<Result> getResults()
	{
		return results;
	}

	public void setResults(List<Result> results)
	{
		this.results = results;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getEdition()
	{
		return edition;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}

	public String getYearPublished()
	{
		return yearPublished;
	}

	public void setYearPublished(String yearPub)
	{
		this.yearPublished = yearPub;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Override
	public String toString(){
		return "Title: " + this.title + "\nEdition: " + edition + "\nAuthor: " + author;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}

package com.example.jnbcb.qrtextbook.query;
// modify access of getters setters as needed
// add comparator by price
public class Result 
{
	private String url; // url of website to buy
	private String companyName; // EX. Amazon
	private float price; // price
	private String type; // ebook, rental, buy
	private String condition; // condition of book, new or used
	
	public Result(String url, String companyName, float price, String type, String condition)
	{
		this.url = url;
		this. companyName = companyName;
		this.price = price;
		this.type = type;
		this.condition = condition;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}
	public float getPrice() 
	{
		return price;
	}
	public void setPrice(float price) 
	{
		this.price = price;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getCondition()
	{
		return condition;
	}
	public void setCondition(String condition)
	{
		this.condition = condition;
	}
	
	@Override
	public String toString(){
		return companyName + " Price: " + price + " Type: " + type + " Condition: " + condition;
	}
}

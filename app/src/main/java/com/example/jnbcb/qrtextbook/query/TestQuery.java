package com.example.jnbcb.qrtextbook.query;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class TestQuery {

	String testISBN = "978-0321971944";
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException
	{
		TestQuery test = new TestQuery();
		Textbook text1 = DirectTextbook.query(test.testISBN);
		System.out.println(text1);
		for (Result result : text1.getResults()){
			System.out.println(result);
		}
		
	}

}

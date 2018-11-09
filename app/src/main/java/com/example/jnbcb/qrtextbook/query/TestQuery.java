package com.example.jnbcb.qrtextbook.query;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TestQuery {

	String testISBN = "978-0321971944";
	String title = "chemistry";

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException
	{
		TestQuery test = new TestQuery();
		List<Textbook> text1 = DirectTextbook.queryTitle(test.title);
		//String text1 = DirectTextbook.queryTitle(test.title).toString();
		System.out.println(text1.toString());
//		for (Result result : text1.getResults()){
//			System.out.println(result);
//		}

	}

}

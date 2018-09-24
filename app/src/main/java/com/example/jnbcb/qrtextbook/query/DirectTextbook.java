package com.example.jnbcb.qrtextbook.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The class queries the DirectTextbook API when given the barcode and returns the resulting Textbook object
 */
public class DirectTextbook {
    private final static String API_KEY = "05e681cb4e446607f509014d7a04219e"; // Must use for all queries

    private static String query = "https://www.directtextbook.com/xml.php?key=" + API_KEY + "&ean="; // query to execute

    private static Document getXMLResponse(String isbn) throws SAXException, ParserConfigurationException, IOException {
        URL url = new URL(query + isbn);
        URLConnection conn = url.openConnection();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // The xml response had leading whitespace that had to be removed
        StringBuilder xmlString = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = bufferedReader.readLine().trim();
        xmlString.append(line + "\n");
        while ((line = bufferedReader.readLine()) != null) {
            xmlString.append(line + "\n");
            //System.out.println(line); // testing
        }
        String xmlResponse = xmlString.toString();
        bufferedReader.close();
        Document doc = db.parse(new InputSource(new StringReader(xmlResponse)));
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static Textbook parseXML(Document doc) {
        Textbook textBook = DirectTextbook.createTextbook(doc);
        if (textBook.isSuccess()) {
            textBook.setResults(DirectTextbook.getResults(doc, textBook.getIsbn()));
        }
        return textBook;
    }

    private static Textbook createTextbook(Document doc) {
        Textbook textBook;
        NodeList attribute;
        try {
            attribute = doc.getElementsByTagName("title");
            String title = attribute.item(0).getTextContent();
            textBook = new Textbook(title, true);
        } catch (NullPointerException e) {
            return new Textbook("Failed", false);
        }

        attribute = doc.getElementsByTagName("author");
        String author = attribute.item(0).getTextContent();
        textBook.setAuthor(author);

        attribute = doc.getElementsByTagName("publisher");
        String publisher = attribute.item(0).getTextContent();
        textBook.setPublisher(publisher);

        attribute = doc.getElementsByTagName("publicationdate");
        String yearPub = attribute.item(0).getTextContent();
        textBook.setYearPublished(yearPub);

        attribute = doc.getElementsByTagName("edition");
        if (attribute.item(0) != null) {
            String edition = attribute.item(0).getTextContent();
            textBook.setEdition(edition);
        } else {
            textBook.setEdition("N/A");
        }

        attribute = doc.getElementsByTagName("ean");
        String isbn = attribute.item(0).getTextContent();
        textBook.setIsbn(isbn);

        return textBook;
    }

    private static List<Result> getResults(Document doc, String isbn) {
        NodeList items = doc.getElementsByTagName("item");
        //System.out.println(items.getLength()); // testing
        Element element;
        NodeList attribute;
        String vendor;
        String url;
        float price;
        String condition;
        String type;
        List<Result> results = new ArrayList<>();
        Result result;
        for (int index = 0; index < items.getLength(); index++) {
            element = (Element) items.item(index);

            attribute = element.getElementsByTagName("vendor");
            vendor = attribute.item(0).getTextContent();

            attribute = element.getElementsByTagName("url");
            url = attribute.item(0).getTextContent();

            attribute = element.getElementsByTagName("price");
            price = Float.valueOf(attribute.item(0).getTextContent());

            attribute = element.getElementsByTagName("condition");
            condition = attribute.item(0).getTextContent();

            if (condition.equals("new")) {
                type = "buy";
            } else if (condition.equals("rental")) {
                condition = "";
                type = "rental";
            } else if (condition.equals("used")) {
                type = "buy";
            } else if (condition.equals("ebook")) {
                condition = "";
                type = "ebook";
            } else {
                type = "";
            }
            result = new Result(url, vendor, price, type, condition, isbn);
            results.add(result);
        }
        return results;
    }

    public static Textbook query(String isbn) throws SAXException, IOException, ParserConfigurationException {
        return DirectTextbook.parseXML(DirectTextbook.getXMLResponse(isbn));
    }
}

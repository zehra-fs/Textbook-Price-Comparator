package com.example.jnbcb.qrtextbook.query;

import android.util.Log;

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
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The class queries the DirectTextbook API when given the barcode and returns the resulting Textbook object
 */
public class DirectTextbook {

    private final static String API_KEY = "05e681cb4e446607f509014d7a04219e"; // Must use for all queries

    private static String queryISBN = "https://www.directtextbook.com/xml.php?key=" + API_KEY + "&ean="; // queryISBN to execute

    //http://www.directtextbook.com/xml_search.php?key=05e681cb4e446607f509014d7a04219e&query=macionis
    private static String queryTitle = "https://www.directtextbook.com/xml_search.php?key=" + API_KEY + "&query=";
    /**
     * Queries for xml response and creates Document
     *
     * @param isbn isbn of the textbook aka barcode
     * @return Document used to parse
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    private static Document getXMLResponse(String isbn) throws SAXException, ParserConfigurationException, IOException {
        URL url = new URL(queryISBN + isbn);
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

    private static Document getXMLResponseTitle(String title) throws SAXException, ParserConfigurationException, IOException {
        URL url = new URL(queryTitle + title);
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
        //doc.getElementsByTagName("title");
        return doc;

    }

    /**
     * Parses Document to created a textbook and results if queryISBN was successful
     *
     * @param doc Document to be parsed
     * @return Textbook
     */
    private static Textbook parseXML(Document doc) {
        Textbook textBook = DirectTextbook.createTextbook(doc);
        if (textBook.isSuccess()) {
            textBook.setResults(DirectTextbook.getResults(doc, textBook.getIsbn()));
        }
        return textBook;
    }

    private static List<Textbook> parseTitlesXML(Document doc) {
        List<Textbook> textBook = DirectTextbook.getTitleResults(doc);
//        if (textBook.isSuccess()) {
//            textBook.setResults(DirectTextbook.getResults(doc, textBook.getIsbn()));
//        }
        return textBook;
    }

    /**
     * Parses Document to get textbook
     *
     * @param doc Document to be parsed
     * @return Textbook
     */
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
        if (attribute.item(0) != null) {
            String author = attribute.item(0).getTextContent();
            textBook.setAuthor(author);
        } else {
            textBook.setAuthor("");
        }

        attribute = doc.getElementsByTagName("publisher");
        if (attribute.item(0) != null) {
            String publisher = attribute.item(0).getTextContent();
            textBook.setPublisher(publisher);
        } else {
            textBook.setPublisher("");
        }

        attribute = doc.getElementsByTagName("publicationdate");
        if (attribute.item(0) != null) {
            String yearPub = attribute.item(0).getTextContent();
            textBook.setYearPublished(yearPub);
        } else {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            textBook.setYearPublished(year + "");
        }

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
        NodeList titles = doc.getElementsByTagName("book");
        Element element;
        ArrayList<String> titlesAL = new ArrayList<>();
        NodeList attribute;
        String vendor;
        String url;

        float price;
        String condition;
        String type;
        List<Result> results = new ArrayList<>();
        Result result;

        for (int index = 0; index < titles.getLength(); index++) {
            element = (Element) titles.item(index);
            attribute = element.getElementsByTagName("title");
            if (attribute.item(0) != null) {
                titlesAL.add(attribute.item(0).getTextContent());
            } else {
                titlesAL.add("");
            }
        }
        for (int index = 0; index < items.getLength(); index++) {
            element = (Element) items.item(index);

            attribute = element.getElementsByTagName("vendor");
            if (attribute.item(0) != null) {
                vendor = attribute.item(0).getTextContent();
            } else {
                vendor = "";
            }


            attribute = element.getElementsByTagName("url");
            if (attribute.item(0) != null) {
                url = attribute.item(0).getTextContent();
            } else {
                url = "";
            }

            attribute = element.getElementsByTagName("price");
            if (attribute.item(0) != null) {
                price = Float.valueOf(attribute.item(0).getTextContent());
            } else {
                price = 0;
            }

            attribute = element.getElementsByTagName("condition");
            if (attribute.item(0) != null) {
                condition = attribute.item(0).getTextContent();
            } else {
                condition = "";
            }

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
            result = new Result(titlesAL.get(0), url, vendor, price, type, condition, isbn);
            results.add(result);

        }
        return results;
    }

    private static List<Textbook> getTitleResults(Document doc) {
        NodeList items = doc.getElementsByTagName("book");
        Element element;
        NodeList attribute;
        String title;
        String author;
        String publisher;
        String publicationdate;
        String ean;
        String edition;
        String format;

        List<Textbook> bookResults = new ArrayList<>();
        Textbook book;

        for (int index = 0; index < items.getLength(); index++) {
            element = (Element) items.item(index);

            attribute = element.getElementsByTagName("title");
            if (attribute.item(0) != null) {
                title = attribute.item(0).getTextContent();
            } else {
                title = "";
            }

            attribute = element.getElementsByTagName("author");
            if (attribute.item(0) != null) {
                author = attribute.item(0).getTextContent();
            } else {
                author = "";
            }

            attribute = element.getElementsByTagName("publisher");
            if (attribute.item(0) != null) {
                publisher = attribute.item(0).getTextContent();
            } else {
                publisher = "";
            }

            attribute = element.getElementsByTagName("publicationdate");
            if (attribute.item(0) != null) {
                publicationdate = attribute.item(0).getTextContent();
            } else {
                publicationdate = "";
            }

            attribute = element.getElementsByTagName("ean");
            if (attribute.item(0) != null) {
                ean = attribute.item(0).getTextContent();
            } else {
                ean = "";
            }
            attribute = element.getElementsByTagName("edition");
            if (attribute.item(0) != null) {
                edition = attribute.item(0).getTextContent();
            } else {
                edition = "";
            }
//            attribute = element.getElementsByTagName("format");
//            if (attribute.item(0) != null) {
//                format = attribute.item(0).getTextContent();
//            } else {
//                format = "";
//            }

            book = new Textbook(ean, title, author, publisher, publicationdate, edition, true);
           // result = new Result(url, vendor, price, type, condition, isbn);
            bookResults.add(book);
        }
        return bookResults;
    }

    /**
     * Public static method to execute queryISBN and return textbook
     *
     * @param isbn ISBN of book aka barcode
     * @return Textbook
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static Textbook query(String isbn) throws SAXException, IOException, ParserConfigurationException {
        return DirectTextbook.parseXML(DirectTextbook.getXMLResponse(isbn));
    }

    public static List<Textbook> queryTitle(String bookTitle) throws SAXException, IOException, ParserConfigurationException
    {
        return DirectTextbook.parseTitlesXML(DirectTextbook.getXMLResponseTitle(bookTitle));
    }
}

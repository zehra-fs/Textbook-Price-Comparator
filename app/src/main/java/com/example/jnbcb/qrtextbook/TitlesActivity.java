package com.example.jnbcb.qrtextbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.jnbcb.qrtextbook.query.DirectTextbook;
import com.example.jnbcb.qrtextbook.query.Textbook;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TitlesActivity extends AppCompatActivity {

    private String bookTitle;
    private List<Textbook> textbookTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        queryForBook();
    }

    public void queryForBook() {
        /*Log.i("SearchTitle", "Search Btn clicked");
        EditText enterTitle = (EditText)findViewById(R.id.enterTitle);
        bookTitle = enterTitle.getText().toString();
        bookTitle = bookTitle.replaceAll("\\s", "+");
        Log.i("SearchTitle", bookTitle);*/
        bookTitle = getIntent().getExtras().getString("titleSearched");
        try {
            textbookTitles = DirectTextbook.queryTitle(bookTitle);
        } catch (SAXException e) {
            Log.e("ResultLoader SAX", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("ResultLoader IO", e.getMessage());
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e("ResultLoader Parser", e.getMessage());
            e.printStackTrace();
        }
        System.out.println(textbookTitles);
        // Log.i("SearchTitle", textbookTitles.toString());
    }
}

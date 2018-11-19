package com.example.jnbcb.qrtextbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jnbcb.qrtextbook.query.DirectTextbook;
import com.example.jnbcb.qrtextbook.query.Textbook;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitlesActivity extends AppCompatActivity {

    private String bookTitle;
    private List<Textbook> textbookTitles;
    private ArrayAdapter<Textbook> titleAdapter;
    @BindView(R.id.titles_listView)
    ListView mListViewTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);

        ButterKnife.bind(this);
        textbookTitles = new ArrayList<>();
        //List<String> titlesList = new ArrayList<>();
        titleAdapter = new ArrayAdapter<>
                (this, R.layout.list_item_textbook, R.id.text_name, textbookTitles );
        mListViewTitles.setAdapter(titleAdapter);
        queryForBook();

        mListViewTitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Textbook textbook = titleAdapter.getItem(position);
                ResultsActivity.currentTextbook = textbook;
                Intent intent = ResultsActivity.historyIntent(view.getContext(), textbook.getIsbn());
                startActivity(intent);
            }
        });

    }

    public void queryForBook() {
        /*Log.i("SearchTitle", "Search Btn clicked");
        EditText enterTitle = (EditText)findViewById(R.id.enterTitle);
        bookTitle = enterTitle.getText().toString();
        bookTitle = bookTitle.replaceAll("\\s", "+");
        Log.i("SearchTitle", bookTitle);*/
        bookTitle = getIntent().getExtras().getString("titleSearched");
        try {
            textbookTitles.addAll(DirectTextbook.queryTitle(bookTitle));
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
        titleAdapter.notifyDataSetChanged();
        // Log.i("SearchTitle", textbookTitles.toString());
    }
}

package com.example.jnbcb.qrtextbook.listview;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.jnbcb.qrtextbook.ResultsActivity;
import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.*;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * This class executes the query in a background thread and stores the resulting textbook information in the DB
 */
public class ResultLoader extends AsyncTaskLoader<List<Result>> {

    private String barcode;
    private Context context;

    public ResultLoader(Context context, String barcode) {
        super(context);
        this.barcode = barcode;
        this.context = context;
    }

    @Nullable
    @Override
    public List<Result> loadInBackground() {
        return null;
    }

    @Nullable
    @Override
    protected List<Result> onLoadInBackground() {
        try {
            ResultsActivity.currentTextbook = DirectTextbook.query(barcode);
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
        if (ResultsActivity.currentTextbook == null) {
            ResultsActivity.currentTextbook = new Textbook("", false);
        }
        ApplicationDB db = ApplicationDB.getInMemoryDatabase(context.getApplicationContext());
        try {
            db.textbookModel().insertTextbook(ResultsActivity.currentTextbook);
            for (Result result : ResultsActivity.currentTextbook.getResults()) {
                db.resultModel().insertResult(result);
            }

        } catch (SQLiteConstraintException error) {
            Log.e("Constraint error", "Textbook exists in DB, " + error.getMessage());
        }
        /** testing
         List<Textbook> books = db.textbookModel().getAllTextbooks();
         for (Textbook book : books) {
         Log.e("book", book.getTitle());
         }
         List<Result> results = db.resultModel().getResults(ResultsActivity.currentTextbook.getIsbn());
         Log.e("Result ", "length for this textbook " + results.size());
         results = db.resultModel().getAll();
         Log.e("Result ", "length total " + results.size());
         //db.textbookModel().deleteTextbook(ResultsActivity.currentTextbook.getIsbn());
         //db.textbookModel().deleteAll();
         books = db.textbookModel().getAllTextbooks();
         Log.e("Texts ", "length total " + books.size());
         for (Textbook book : books) {
         Log.e("book", book.getTitle());
         }
         **/
        return ((List<Result>) ResultsActivity.currentTextbook.getResults());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

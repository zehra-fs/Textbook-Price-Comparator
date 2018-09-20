package com.example.jnbcb.qrtextbook.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jnbcb.qrtextbook.ResultsActivity;
import com.example.jnbcb.qrtextbook.query.*;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


// done

public class ResultLoader extends AsyncTaskLoader<List<Result>> {

    private String barcode;

    public ResultLoader(Context context, String barcode){
        super(context);
        this.barcode = barcode;
    }

    @Nullable
    @Override
    public List<Result> loadInBackground() {
        return null;
    }

    @Nullable
    @Override
    protected List<Result> onLoadInBackground() {
        Textbook textbook;
        List<Result> results;
        try {
            ResultsActivity.currentTextbook = DirectTextbook.query(barcode);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return ((List<Result>) ResultsActivity.currentTextbook.getResults());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

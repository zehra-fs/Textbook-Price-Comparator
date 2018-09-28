package com.example.jnbcb.qrtextbook.listview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jnbcb.qrtextbook.FavoriteActivity;
import com.example.jnbcb.qrtextbook.HistoryActivity;
import com.example.jnbcb.qrtextbook.R;

import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Textbook> {
    private Context context;

    public HistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Textbook> articles) {
        super(context, resource, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_textbook, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Textbook textbook = getItem(position);
        if (textbook != null) {
            holder.textName.setText(textbook.getTitle());
        }
        final ApplicationDB db = ApplicationDB.getInMemoryDatabase(view.getContext());
        holder.delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    public void run() {
                        db.textbookModel().deleteTextbook(textbook.getIsbn());
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((HistoryActivity) context).dataChanged();
                            }
                        });
                    }
                };
                thread.start();
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView textName;
        private Button delBut;

        private ViewHolder(View view) {
            textName = (TextView) view.findViewById(R.id.text_name);
            delBut = (Button) view.findViewById(R.id.delete_history);
        }
    }
}

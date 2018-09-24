package com.example.jnbcb.qrtextbook.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jnbcb.qrtextbook.R;
import com.example.jnbcb.qrtextbook.query.Result;
import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Textbook>
{
    public HistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Textbook> articles) {
        super(context, resource, articles);
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
        Textbook textbook = getItem(position);
        if (textbook != null) {
            holder.textName.setText(textbook.getTitle());
        }
        return view;
    }

    private class ViewHolder {
        private TextView textName;

        private ViewHolder(View view) {
            textName = (TextView) view.findViewById(R.id.text_name);
        }
    }
}

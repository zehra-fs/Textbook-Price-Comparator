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

import java.util.List;

import com.example.jnbcb.qrtextbook.R;
import com.example.jnbcb.qrtextbook.query.*;

public class ResultAdapter extends ArrayAdapter<Result> {

    public ResultAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> articles) {
        super(context, resource, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Result result = getItem(position);
        if (result != null){
            holder.vendorName.setText(result.getCompanyName());
            holder.price.setText(String.format("%.2f", result.getPrice()));
            holder.condition.setText(result.getCondition());
            holder.url = result.getUrl();
        }
        return view;
    }

    private class ViewHolder {
        private TextView vendorName;
        private TextView price;
        private TextView condition;
        private String url;

        private ViewHolder(View view)
        {
            vendorName = (TextView) view.findViewById(R.id.vendor_name);
            condition = (TextView) view.findViewById(R.id.condition);
            price = (TextView) view.findViewById(R.id.price);
        }

    }
}

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jnbcb.qrtextbook.FavoriteActivity;
import com.example.jnbcb.qrtextbook.R;
import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.Result;

import java.util.List;

import butterknife.BindView;

public class FavoriteAdapter extends ArrayAdapter<Result> {

    private final Context context;

    public FavoriteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> articles) {
        super(context, resource, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_favorite, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final View newView = view;
        final Result result = getItem(position);
        if (result != null) {
            holder.vendorName.setText(result.getCompanyName());
            holder.price.setText(String.format("%.2f", result.getPrice()));
            holder.condition.setText(result.getCondition());
            final ApplicationDB db = ApplicationDB.getInMemoryDatabase(view.getContext());
            holder.delBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread() {
                        public void run() {
                            result.setFavorited(false);
                            db.resultModel().updateResult(result);
                            Log.e("removed favorite", result.toString());
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FavoriteActivity) context).dataChanged();
                                }
                            });
                        }
                    };
                    thread.start();
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        private TextView vendorName;
        private TextView price;
        private TextView condition;
        private Button delBut;

        private ViewHolder(View view) {
            vendorName = (TextView) view.findViewById(R.id.vendor_name_favorite);
            condition = (TextView) view.findViewById(R.id.condition_favorite);
            price = (TextView) view.findViewById(R.id.price_favorite);
            delBut = (Button) view.findViewById(R.id.delete);
        }
    }
}

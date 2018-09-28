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

import java.util.List;

import com.example.jnbcb.qrtextbook.FavoriteActivity;
import com.example.jnbcb.qrtextbook.R;
import com.example.jnbcb.qrtextbook.ResultsActivity;
import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.*;

/**
 * This class fills the listview for the results activity
 */
public class ResultListAdapter extends ArrayAdapter<Result> {


    private Context context;

    public ResultListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> articles) {
        super(context, resource, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Result result = getItem(position);
        if (result != null) {
            holder.vendorName.setText(result.getCompanyName());
            holder.price.setText(String.format("%.2f", result.getPrice()));
            holder.condition.setText(result.getCondition());
            final ApplicationDB db = ApplicationDB.getInMemoryDatabase(view.getContext());
            if (result.isFavorited()) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.favBut.setVisibility(View.INVISIBLE);

                    }
                });
            } else {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.favBut.setVisibility(View.VISIBLE);

                    }
                });
            }
            holder.favBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread() {
                        public void run() {
                            result.setFavorited(true);
                            db.resultModel().updateResult(result);
                            Log.e("favorited ", result.toString());
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ResultsActivity) context).dataChanged();
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
        private Button favBut;

        private ViewHolder(View view) {
            vendorName = (TextView) view.findViewById(R.id.vendor_name);
            condition = (TextView) view.findViewById(R.id.condition);
            price = (TextView) view.findViewById(R.id.price);
            favBut = (Button) view.findViewById(R.id.favorite);
        }
    }
}

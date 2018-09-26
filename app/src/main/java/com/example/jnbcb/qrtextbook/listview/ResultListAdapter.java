package com.example.jnbcb.qrtextbook.listview;

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

import com.example.jnbcb.qrtextbook.R;
import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.*;

/**
 * This class fills the listview for the results activity
 */
public class ResultListAdapter extends ArrayAdapter<Result> {

    public ResultListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> articles) {
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
        final View newView = view;
        final Result result = getItem(position);
        if (result != null) {
            holder.vendorName.setText(result.getCompanyName());
            holder.price.setText(String.format("%.2f", result.getPrice()));
            holder.condition.setText(result.getCondition());
            holder.url = result.getUrl();
            final ApplicationDB db = ApplicationDB.getInMemoryDatabase(view.getContext());
            holder.favBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread() {
                        public void run() {
                            result.setFavorited(true);
                            result.setId(0);
                            db.resultModel().insertResult(result);
                            Log.e("Added to DB", result.toString());
                        }
                    };
                    thread.start();
                }
            });
//            holder.delBut.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Thread thread = new Thread() {
//                        public void run() {
//                            if (result.isFavorited()){
//                                db.resultModel().deleteFavorite(result.getId());
//
//                            }
//                        }
//                    };
//                    thread.start();
//                }
//            });
        }
        return view;
    }

    private class ViewHolder {
        private TextView vendorName;
        private TextView price;
        private TextView condition;
        private Button favBut;
        private Button delBut;
        private String url;

        private ViewHolder(View view) {
            vendorName = (TextView) view.findViewById(R.id.vendor_name);
            condition = (TextView) view.findViewById(R.id.condition);
            price = (TextView) view.findViewById(R.id.price);
            favBut = (Button) view.findViewById(R.id.favorite);
            //delBut = (Button) view.findViewById(R.id.delete);
        }
    }
}

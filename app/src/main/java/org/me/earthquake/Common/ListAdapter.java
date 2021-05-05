/**
 * @author Aby Mokua
 * @id S1732294
 */

package org.me.earthquake.Common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.me.earthquake.Model.Item;

import org.me.earthquake.R;

public class ListAdapter extends ArrayAdapter<String> {
    ArrayList<Item> items;
    Activity context;
    boolean title;

    public ListAdapter(Activity context, ArrayList<Item> items, String [] array, boolean title) {
        super(context, R.layout.row, array);
        this.items = items;
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ListHolder listHolder = null;

        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.row, null, true);
            listHolder = new ListHolder(view, title);
            view.setTag(listHolder);
        }else{
            listHolder = (ListHolder) view.getTag();
        }

        listHolder.getLocation().setText(items.get(i).getLocation());
        listHolder.getDate().setText(items.get(i).getDate());

        if(title){
            listHolder.getTitle().setText(items.get(i).getTitle());
        }

        listHolder.getDepth().setText(items.get(i).getDepth());

        listHolder.getMagnitude().setText(items.get(i).getMagnitude());
        listHolder.getDescription().setText(items.get(i).getDescription());

        return view;
    }

    //    @NonNull
//    @Override
//    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
//
//        return new ListHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ListHolder holder, int i) {
//        holder.getTitle().setText(items.get(i).getTitle());
//        holder.getDate().setText(items.get(i).getDescription());
//        holder.getTime().setText(items.get(i).getTime());
//        holder.getMagnitude().setText(items.get(i).getMagnitude());
//        holder.getDescription().setText(items.get(i).getDescription());
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
}

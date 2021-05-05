/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.me.earthquake.Activity.MainActivity;
import org.me.earthquake.Model.Item;
import org.me.earthquake.Common.ListAdapter;

import org.me.earthquake.R;

public class ListFragment extends Fragment{
    private ListView listView;
    private Spinner spinner;
    private Button btn1;
    private EditText date1;
    private int mYear, mMonth, mDay;
    private ArrayList<Item> data;
    private ArrayList<Item> filtered_data;
    private AutoCompleteTextView search;
    private static String[] feed;

    public ListFragment(){
        this.data = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        init(view);

        filtered_data = null;

        if(savedInstanceState != null){
            data = (ArrayList<Item>) savedInstanceState.getSerializable("data");
        }else{
            data = (ArrayList<Item>) getArguments().getSerializable("data");
        }

        itemsAsString(data);

        ListAdapter listAdapter = new ListAdapter(getActivity(), data, feed, true);
        listView.setAdapter(listAdapter);

        //Creating the instance of ArrayAdapter containing list of fruit names
        Log.e("Debudding: ", feed[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, feed);

        search.setThreshold(1);
        search.setAdapter(adapter);

        return view;
    }

    public void init(View view){
        listView = view.findViewById(R.id.listview);
        btn1     = view.findViewById(R.id.button1);
        date1    = view.findViewById(R.id.date1);
        search   = view.findViewById(R.id.search);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                DetailFragment fragment = new DetailFragment();
                Bundle bundle = new Bundle();

                Item item = null;

                if(filtered_data != null){
                    item = filtered_data.get(i);
                }else{
                    item = data.get(i);
                }

                bundle.putSerializable("item", item);
                fragment.setArguments(bundle);

                MainActivity.fragment = fragment;

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

        });

        search.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filtered_data = getFilteredSData(search.getText().toString());
                itemsAsString(filtered_data);
                ListAdapter listAdapter = new ListAdapter(getActivity(), filtered_data, feed, true);
                listView.setAdapter(listAdapter);
            }

        });
    }

    private void itemsAsString(ArrayList<Item> items){
        feed  = new String[items.size()];

        for (int i = 0; i < items.size(); ++i) {
            feed[i] = items.get(i).getLocation();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
    }

    private ArrayList<Item> getFilteredSData(String toString) {
        ArrayList<Item> filtered_data = new ArrayList<>();

        for ( Item item : data) {
            if(item.getLocation().equalsIgnoreCase(toString)){
                filtered_data.add(item);
            }
        }

        return filtered_data;
    }
}
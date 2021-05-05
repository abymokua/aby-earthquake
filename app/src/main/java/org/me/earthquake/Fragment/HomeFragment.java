/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.me.earthquake.Common.Common;
import org.me.earthquake.Common.ListAdapter;
import org.me.earthquake.Activity.MainActivity;
import org.me.earthquake.Model.Item;
import org.me.earthquake.R;

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnMapReadyCallback {
    private ListView listView;
    private Button btn1, btn2;
    private EditText date1, date2;
    private int mYear, mMonth, mDay;
    private ArrayList<Item> data, filtered_data;
    private SupportMapFragment mapFragment;

    public HomeFragment (){
        this.data = new ArrayList<>();
        this.filtered_data = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        mapFragment.getMapAsync(this);
        listView.setOnItemClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        if(savedInstanceState != null){

            filtered_data = (ArrayList<Item>) savedInstanceState.getSerializable("filtered_data");
            data = (ArrayList<Item>) savedInstanceState.getSerializable("data");
            String d = savedInstanceState.getString("date1");
            date1.setText(d);

            d = savedInstanceState.getString("date2");
            date2.setText(d);

            String array[] = itemsAsString(filtered_data);
            ListAdapter listAdapter = new ListAdapter(getActivity(), filtered_data, array, true);
            listView.setAdapter(listAdapter);
        }else{
            data = (ArrayList<Item>) getArguments().getSerializable("data");
        }

        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        for (Item item : data) {
            createMarker(googleMap, Double.parseDouble(item.getLat().trim()), Double.parseDouble(item.getLon().trim()), item.getLocation(), item.getTitle());
        }
    }

    protected void createMarker(GoogleMap googleMap, double latitude, double longitude, String title, String snippet) {
        googleMap.addMarker(new MarkerOptions()
                 .position(new LatLng(latitude, longitude))
                 .anchor(0.5f, 0.5f).title(title)
                 .snippet(snippet));
    }

    private void init(View view){
        btn1  = view.findViewById(R.id.button1);
        btn2  = view.findViewById(R.id.button2);
        date1 = view.findViewById(R.id.date1);
        date2 = view.findViewById(R.id.date2);
        listView    = view.findViewById(R.id.listview);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    }

    private String [] itemsAsString(ArrayList<Item> items){
        String array[] = new String[items.size()];

        for (int i = 0; i < items.size(); ++i)
            array[i] = items.get(i).getTitle();

        return array;
    }

    private ArrayList<Item> getItems(EditText date1, EditText date2){
        ArrayList<Item> list = new ArrayList<>(data);

        Date d_conv1 = null, d_conv2 = null;
        SimpleDateFormat spd1 = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        SimpleDateFormat spd2 = new SimpleDateFormat("dd-MM-yyyy");

        try {
            d_conv1 = spd2.parse((date1.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {         d_conv2 = spd2.parse((date2.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date;

        for(int i = list.size()-1; i >= 0; --i){
            date = null;

            try {
                date = spd1.parse(list.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(d_conv1 != null){
                if(date.before(d_conv1)){
                    list.remove(i);
                }
            }

            if(d_conv2 != null){
                if(date.after(d_conv2)){
                    list.remove(i);
                }
            }
        }

        return list;
    }

    public ArrayList<Item> nearest_elements(ArrayList<Item> data){
        filtered_data = new ArrayList<>();

        Item nearest_north = null;
        Item nearest_south = null;
        Item nearest_west  = null;
        Item nearest_east  = null;
        Item largest_magnitude = new Item(data.get(0));
        Item shallowest_earthquake = new Item(data.get(0));

        for(Item item : data){
            //finding item of greatest magnitude
            if(Double.parseDouble(largest_magnitude.getMagnitude().trim().split(" ")[0]) < Double.parseDouble(item.getMagnitude().trim().split(" ")[0])) {
                largest_magnitude = new Item(item);
            }

            //finding shallowest earthquake
            if(Double.parseDouble(shallowest_earthquake.getDepth().trim().split(" ")[0]) < Double.parseDouble(item.getDepth().trim().split(" ")[0])) {
                shallowest_earthquake = new Item(item);
            }

            //finding nearest_north item
            if(Double.parseDouble(item.getLat().trim()) > Common.mau_lat){
                if(nearest_north != null){
                    if(Double.parseDouble(item.getLat().trim()) < Double.parseDouble(nearest_north.getLat().trim())){
                        nearest_north = new Item(item);
                    }
                }else{
                    nearest_north = new Item(item);
                }
            }else if(nearest_south != null){
                        if(Double.parseDouble(item.getLat().trim()) > Double.parseDouble(nearest_south.getLat().trim())){
                            nearest_south = new Item(item);
                        }
                    }else{
                        nearest_south = new Item(item);
                    }

            if(Double.parseDouble(item.getLon().trim()) > Common.mau_long){
                if(nearest_east != null){
                    if(Double.parseDouble(item.getLon().trim()) < Double.parseDouble(nearest_east.getLon().trim())){
                        nearest_east = new Item(item);
                    }
                }else{
                    nearest_east = new Item(item);
                }
            }else if(nearest_west != null){
                if(Double.parseDouble(item.getLat().trim()) > Double.parseDouble(nearest_west.getLat().trim())){
                    nearest_west = new Item(item);
                }
            }else{
                nearest_west = new Item(item);
            }
        }

        nearest_east.setTitle("nearest east");
        filtered_data.add(nearest_east);

        nearest_south.setTitle("nearest south");
        filtered_data.add(nearest_south);

        nearest_north.setTitle("nearest north");
        filtered_data.add(nearest_north);

        nearest_west.setTitle("nearest west");
        filtered_data.add(nearest_west);

        largest_magnitude.setTitle("largest magnitude");
        filtered_data.add(largest_magnitude);

        shallowest_earthquake.setTitle("shallowest earthquake");
        filtered_data.add(shallowest_earthquake);

        return filtered_data;
    }

    @Override
    public void onClick(View v) {

        if (v == btn1 || v == btn2) {
            final View view = v;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker dp, int year, int monthOfYear, int dayOfMonth) {
                            if(view == btn1){
                                date1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }else{
                                date2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }

                            ArrayList<Item> items = getItems(date1, date2);

                            if(items.size() == 0){
                                Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
                            }else{
                                items = nearest_elements(items);
                            }

                            String array[] = itemsAsString(items);
                            ListAdapter listAdapter = new ListAdapter(getActivity(), items, array, true);
                            listView.setAdapter(listAdapter);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filtered_data", filtered_data);
        outState.putSerializable("data", data);
        outState.putString("date1", date1.getText().toString());
        outState.putString("date2", date2.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        Item item = filtered_data.get(i);

        bundle.putSerializable("item", item);
        fragment.setArguments(bundle);

        MainActivity.fragment = fragment;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}

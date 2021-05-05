/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.earthquake.Model.Item;
import org.me.earthquake.R;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private ArrayList<Item> data;
    private SupportMapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        data = (ArrayList<Item>) getArguments().getSerializable("data");

        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        for (Item item : data) {
            createMarker(googleMap, Double.parseDouble(item.getLat().trim()), Double.parseDouble(item.getLon().trim()), item.getLocation(), item.getTitle()+": M "+item.getMagnitude());
        }
    }

    protected void createMarker(GoogleMap googleMap, double latitude, double longitude, String title, String snippet) {

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f).title(title)
                .snippet(snippet));
    }
}

/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.me.earthquake.Fragment.AboutFragment;
import org.me.earthquake.Fragment.DetailFragment;
import org.me.earthquake.Fragment.HomeFragment;
import org.me.earthquake.Fragment.ListFragment;
import org.me.earthquake.Fragment.MapFragment;
import org.me.earthquake.Model.Item;
import org.me.earthquake.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private ArrayList<Item> data;
    public Bundle bundle;
    public static Fragment fragment;
    private BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bundle = new Bundle();

        if(savedInstanceState != null){
            fragment =  getSupportFragmentManager().getFragment(savedInstanceState,"fragment");
            data = (ArrayList<Item>) savedInstanceState.getSerializable("data");
        }else {
            try {
                data = new MyParser().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            fragment = new HomeFragment();
        }

        bundle.putSerializable("data", data);
        fragment.setArguments(bundle);

        bottom_nav = findViewById(R.id.bottom_navigation);
        bottom_nav.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "MY_FRAGMENT").commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                HomeFragment home = new HomeFragment();
                bundle.putSerializable("data", data);
                home.setArguments(bundle);
                fragment = home;
                break;

            case R.id.nav_about:
                fragment = new AboutFragment();
                break;

            case R.id.nav_list:
                ListFragment list = new ListFragment();
                bundle.putSerializable("data", data);
                list.setArguments(bundle);
                fragment = list;
                break;

            case R.id.nav_map:
                MapFragment map = new MapFragment();
                bundle.putSerializable("data", data);
                map.setArguments(bundle);
                fragment = map;
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment , "MY_FRAGMENT").commit();
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyParser extends AsyncTask<Void, ArrayList<Item>, ArrayList<Item>>{
        private ArrayList<Item> data = new ArrayList<>();
        private NodeList nodes;

        //
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //
        protected ArrayList<Item> doInBackground(Void... voids) {
            data = getData();
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
        }

       //
        protected void onProgressUpdate(ArrayList<Item>... values) {
            super.onProgressUpdate(values);
        }

        private ArrayList getData(){
            ArrayList<Item> data = null;

            try {
                java.net.URL url = new URL("https://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
                URLConnection conn = url.openConnection();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());

                nodes = doc.getElementsByTagName("item");
                data  = parseData(nodes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        private ArrayList<Item> parseData(NodeList nodes){
            Item newItem = null;
            ArrayList<Item> items = new ArrayList<Item>();
            Element line = null;

            for (int i = 0; i < nodes.getLength(); i++) {
                newItem = new Item();

                Element element = (Element) nodes.item(i);

                //Adding the title
                NodeList title = element.getElementsByTagName("title");
                line = (Element) title.item(0);
                newItem.setDescription(line.getTextContent());

                //Adding the description
                NodeList description = element.getElementsByTagName("description");
                line = (Element) description.item(0);
                String temp = line.getTextContent();

                for (String dt : temp.split(";")) {
                    String var = dt.substring(0, dt.indexOf(":")).trim();

                    if(var.equalsIgnoreCase("Origin date/time")){
                        newItem.setDate(dt.substring(dt.indexOf(":")+1).trim());
                    }else if(var.equalsIgnoreCase("Location")){
                        newItem.setLocation(dt.substring(dt.indexOf(":")+1).trim());
                    }else if(var.equalsIgnoreCase("Lat/long")){
                        String spl[] = dt.substring(dt.indexOf(":")+1).split(",");
                        newItem.setLat(spl[0].trim());
                        newItem.setLon(spl[1].trim());
                    }else if(var.equalsIgnoreCase("Magnitude")){
                        newItem.setMagnitude(dt.substring(dt.indexOf(":")+1).trim());
                    }else if(var.equalsIgnoreCase("Depth")){
                        newItem.setDepth(dt.substring(dt.indexOf(":")+1).trim());
                    }
                }

                items.add(newItem);
            }

            return items;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
        outState.putSerializable("data", data);
    }

    private String findFragment(){
        Fragment myFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("MY_FRAGMENT");
        String fragment_name = "";

        if(myFragment instanceof DetailFragment){
            fragment_name = "DetailFragment";
        }else if(myFragment instanceof HomeFragment){
            fragment_name = "HomeFragment";
        }else if(myFragment instanceof ListFragment){
            fragment_name = "ListFragment";
        }else if(myFragment instanceof AboutFragment){
            fragment_name = "AboutFragment";
        }

        return fragment_name;
    }

    private int getSelectedItem(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                return menuItem.getItemId();
            }
        }
        return 0;
    }
}

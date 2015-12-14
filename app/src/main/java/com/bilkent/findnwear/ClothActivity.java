package com.bilkent.findnwear;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.Model.Response.Base;
import com.bilkent.findnwear.Utilities.API;
import com.bilkent.findnwear.Utilities.Storage;
import com.bilkent.findnwear.Utilities.StringUtilities;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClothActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_PERMISSIONS = 25;
    private Cloth cloth;
    private ImageView photoImageView;
    private TextView colorTextView;
    private TextView brandTextView;
    private TextView priceTextView;
    private TextView storeLinkTextView;
    private LocationManager locationManager;
    private MaterialSpinner storeSpinner;
    private ArrayList<Map.Entry<Store, Double>> finalList;
    private FloatingActionButton addWishlistButton;

    public class Store implements Comparable<Store>{

        public double longitude;
        public double latitude;
        public String name;

        @Override
        public int compareTo(Store another) {
            return name.compareTo(another.name);
        }
    }

    public class Brand{
        public String name;
        public Store[] stores;
    }

    private static double[][] boynerPoints = {{39.908963, 32.784469},
            {39.901852, 32.698638},
            {39.922392, 32.835281},
            {39.951086, 32.838714},
            {39.945706, 32.781561},
            {39.970180, 32.837523},
            {39.887891, 32.940681},
            {39.852994, 32.835806},
            {39.960971, 32.752036}};

    private static String[] boynerStores = {"Cepa Boyner",
            "Gordion Boyner",
            "Bahçeli Boyner",
            "AnkaMall Boyner",
            "Acity Boyner",
            "Antares Boyner",
            "Natavega Boyner",
            "Panora Boyner",
            "Batıkent Boyner"};

    private static double[][] mangoPoints = {{39.908963, 32.784469},
            {39.905802, 32.870300},
            {39.875772, 32.878883},
            {39.887891, 32.940681},
            {39.917916, 32.861717},
            {39.922392, 32.835281},
            {39.951086, 32.838714},
            {39.901852, 32.698638},
            {39.965823, 32.641304},
            {40.016588, 32.829444}};

    private static String[] mangoStores = {"CEPA",
            "TUNALI",
            "365 AVM",
            "NATAVEGA",
            "KIZILAY",
            "BAHÇELİEVLER",
            "ANKAMALL",
            "GORDION",
            "OPTIMUM",
            "FORUMAVM"};

    //Koton Noktalar
    private static double[][] kotonPoints = {	{39.965823, 32.641304}	,
            {39.901852, 32.698638},
            {39.908963, 32.784469},
            {39.908963, 32.784469},
            {39.922392, 32.835281},
            {39.852994, 32.835806},
            {39.875772, 32.878883},
            {39.905802, 32.870300},
            {39.917916, 32.861717}};
    private static String[] kotonStores = {"OPTIMUM",
            "GORDION",
            "CEPA",
            "KENTPARK",
            "BAHÇELİEVLER",
            "PANORA",
            "365 AVM",
            "TUNALI",
            "KIZILAY"};

    //Zara Noktalar
    private static double[][] zaraPoints = {	{39.908963, 32.784469},
            {39.852994, 32.835806},
            {39.951086, 32.838714},
            {39.901852, 32.698638}};
    private static String[] zaraStores = {"KENTPARK",
            "PANORA",
            "ANKAMALL",
            "GORDION"};

    private static Brand[] brands;

    {
        brands = new Brand[4];

        finalList = new ArrayList<>();

        Brand boyner = new Brand();
        boyner.name = "Boyner";
        boyner.stores = new Store[boynerStores.length];
        for(int i=0; i < boynerStores.length; i++){
            boyner.stores[i] = new Store();
            boyner.stores[i].name = boynerStores[i];
            boyner.stores[i].latitude = boynerPoints[i][0];
            boyner.stores[i].longitude = boynerPoints[i][1];
        }
        brands[0] = boyner;

        Brand mango = new Brand();
        mango.name = "Mango";
        mango.stores = new Store[mangoStores.length];
        for(int i=0; i < mangoStores.length; i++){
            mango.stores[i] = new Store();
            mango.stores[i].name = mangoStores[i];
            mango.stores[i].latitude = mangoPoints[i][0];
            mango.stores[i].longitude = mangoPoints[i][1];
        }
        brands[1] = mango;

        Brand koton = new Brand();
        koton.name = "Koton";
        koton.stores = new Store[kotonStores.length];
        for(int i=0; i < kotonStores.length; i++){
            koton.stores[i] = new Store();
            koton.stores[i].name = kotonStores[i];
            koton.stores[i].latitude = kotonPoints[i][0];
            koton.stores[i].longitude = kotonPoints[i][1];
        }
        brands[2] = koton;

        Brand zara = new Brand();
        zara.name = "Zara";
        zara.stores = new Store[zaraStores.length];
        for(int i=0; i < zaraStores.length; i++){
            zara.stores[i] = new Store();
            zara.stores[i].name = zaraStores[i];
            zara.stores[i].latitude = zaraPoints[i][0];
            zara.stores[i].longitude = zaraPoints[i][1];
        }
        brands[3] = zara;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        cloth = new Gson().fromJson(getIntent().getStringExtra("data"), Cloth.class);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        String title = StringUtilities.upperCaseEachWord(cloth.getDescription() + " from " + cloth.brand);
        collapsingToolbarLayout.setTitle(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        photoImageView = (ImageView) findViewById(R.id.photo_imageview);
        Picasso.with(this).load(cloth.photo_url).into(photoImageView);

        colorTextView = (TextView) findViewById(R.id.color_textview);
        brandTextView = (TextView) findViewById(R.id.brand_textview);
        priceTextView = (TextView) findViewById(R.id.price_textview);
        storeLinkTextView = (TextView) findViewById(R.id.store_link_textview);
        storeSpinner = (MaterialSpinner) findViewById(R.id.store_spinner);
        addWishlistButton = (FloatingActionButton) findViewById(R.id.fab);

        storeSpinner.setHint(getString(R.string.waiting_for_location));

        colorTextView.setText(cloth.color);
        brandTextView.setText(cloth.brand);
        priceTextView.setText(cloth.price + " TL");
        storeLinkTextView.setText(Html.fromHtml("<a href='" + cloth.url + "'>" + cloth.url + "</a>"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1, new String[0]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        addWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API.instance.addWishlist(Storage.getUser().id, cloth.id, new Callback<Base>() {
                    @Override
                    public void success(Base base, Response response) {
                        if(base.success){
                            Toast.makeText(ClothActivity.this, getString(R.string.added_to_wishlist), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(ClothActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.d("Stores", "request is taken");
        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                || grantResults.length >= 2 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        locationManager.removeUpdates(this);
        double myLocX = location.getLatitude();
        double myLocY = location.getLongitude();

        String storeName = cloth.brand; // İLGİLİ STORE NAME ÜRÜN DETAY SAYFASINDA ÜRÜNÜN DATABASEDEN ÜRÜNÜN MARKASINI ÇEKEREK GELECEK!!
        Log.d("StoreName", storeName);

        HashMap<Store, Double> map = new HashMap<>();

        Brand currentBrand = null;
        for(int i=0; i < brands.length; i++){
            if(brands[i].name.equals(storeName)){
                currentBrand = brands[i];
                break;
            }
        }

        if(currentBrand == null){
            Log.d("Stores", "returned null");
            storeSpinner.setHint(getString(R.string.couldnot_find_store));
            return;
        }

        for (int i = 0; i < currentBrand.stores.length; i++) {
            double distance = calcDistance(currentBrand.stores[i].latitude, currentBrand.stores[i].longitude, myLocX, myLocY);
            map.put(currentBrand.stores[i], distance);
        }

        LinkedHashMap<Store, Double> sortedMap = sortHashMapByValuesD(map);
        finalList = new ArrayList<>();
        for(Map.Entry<Store, Double> store:sortedMap.entrySet()){
            finalList.add(store);
        }

        int size = (5 > currentBrand.stores.length) ? currentBrand.stores.length : 5;
        String[] storeNames = new String[size];
        for(int i=0; i < size; i++){
            storeNames[i] = finalList.get(i).getKey().name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1, storeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);
        storeSpinner.setHint(getString(R.string.goto_a_nearby_store));

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < finalList.size()) {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=" +
                            finalList.get(position).getKey().longitude + ", " +
                            finalList.get(position).getKey().latitude + "");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public LinkedHashMap<Store,Double> sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Store,Double> sortedMap = new LinkedHashMap<Store,Double>();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((Store)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}

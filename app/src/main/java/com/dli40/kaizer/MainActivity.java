package com.dli40.kaizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final String LOAD_METHOD_ID = "load_method_id";
    private FusedLocationProviderClient mFusedLocationClient;// to access device location easily
    public Location mLastLocation = new Location(""); //will replace soon
    static final int MAX_SIZE = 50; //max number of restaurants returned from list at time

//    private TextView mLocationTextView; //to display final restaurant result

    private Random rand; //RNG used to pick restaurant
    private ArrayList<Restaurant> restaurants = new ArrayList<>();
    private YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();

    @Override
    public void onTaskCompleted(String result) {
        // Update the UI
        Log.i("stuff", "found loction");
//        mLocationTextView.setText(getString(R.string.address_text,
//                result, System.currentTimeMillis()));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("stuff", "on created");

//        int code = getIntent().getIntExtra(LOAD_METHOD_ID, 0);
//        if (code==100) //will implement with switch later lmao
//        {
//            String busId = getIntent().getStringExtra("busId");
//            //getReviews(busId); //not sure where withResult comes in yet...may need to return smt
//        }
//


        final Button button = findViewById(R.id.randSelect); //the button to press to start activity
        // mLocationTextView = findViewById(R.id.textview_location); //initalize textview
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        rand = new Random(); //initialize rng

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses buttons
                if (restaurants.size() == 50) {
                    Log.i("stuff", "kept previous data");
                    Restaurant restaurant = pickRestaurant(restaurants);
                    displayRestaurant(restaurant); //WILL START NEW ACTIVITY
                } else {
                    getLocation();

                    try {
                        YelpFusionApi yelpFusionApi = apiFactory.createAPI(API_KEY);
                        String term = "restaurants";
                        String latitude = mLastLocation.getLatitude() + "";
                        String longitude = mLastLocation.getLongitude() + "";
                        String radius = "10000";
                        boolean open_now = true;
                        HashMap<String, String> requestMap = requestBuildMap(term, latitude, longitude, radius, MAX_SIZE, open_now);
                        Log.i("stuff", "built the map");
                        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                            @Override
                            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                                SearchResponse searchResponse = response.body();
                                ArrayList<Business> bus = searchResponse.getBusinesses();

                                updateRestaurants(bus); //updating current list of restaurants

                                Restaurant restaurant = pickRestaurant(restaurants);
                                displayRestaurant(restaurant); //WILL START NEW ACTIVITY
                                //integrate google maps turn by turn direction actiity
                            }

                            @Override
                            public void onFailure(Call<SearchResponse> call, Throwable t) {
                                // HTTP error happened, do something to handle it.
                                Log.i("stuff", "ERROR HAPPENED CALLBACK FAILED");
                            }
                        };
                        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(requestMap);
                        call.enqueue(callback);
                        Log.i("stuff", "enqueed callback");

                    } catch (IOException e) {

                        Log.i("bad", "IO ERROR IN MAIN");
                    } catch (Exception e) {
                        Log.i("stuff", "unknown error to me lmao");
                    }
                }
            }
        });
    }

    private HashMap<String, String> requestBuildMap(String term, String latitude, String longitude, String radius, int limit, boolean open_now) {
        HashMap<String, String> map = new HashMap<>();
        map.put("term", term);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("radius", radius);
        map.put("limit", limit + "");
        map.put("open_now", String.valueOf(open_now));
        return map;
    }


    public void updateRestaurants(ArrayList<Business> businesses) {
        for (int i = 0; i < MAX_SIZE; i++)//this is actualy really stupid, whati f too little????
        {
            Business b = businesses.get(i); //ADD ALL RSTAURANTS FOR FAST ACCESS LATER
            if (b != null) //safety first but slightly less efficient
            {
                // busIds.add(bus.get(i).getId()); //.getId()
                String busId = b.getId();
                String name = b.getName();
                String price = b.getPrice();
                double distance = b.getDistance();
                double rating = b.getRating();
                Integer reviewCount = b.getReviewCount();
                ArrayList categories = b.getCategories();
                //String displayAddress=  b.getLocation();
                ArrayList hours = b.getHours();
                String photoUrl = b.getImageUrl();
                Restaurant res = new Restaurant(busId, name, price, distance,
                        rating, reviewCount, photoUrl, hours, categories);
                restaurants.add(res);
            }
        }
        Log.i("stuff", "updated restaurants, listi s: " + restaurants.toString());
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Log.i("stuff", "Loction found in client: " + location.toString());
                                mLastLocation = location;
                                Log.i("stuff", "found location");
                            } else {
                                Log.i("stuff", "no location");
//                                mLocationTextView.setText(R.string.no_location);
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Restaurant pickRestaurant(ArrayList<Restaurant> restaurants) {
        int i = rand.nextInt(restaurants.size());
        return restaurants.get(i);
    }

    private void displayRestaurant(Restaurant restaurant) {
        //move to new page
        String busId = restaurant.getBusId();
        String name = restaurant.getName();
        String price = restaurant.getPrice();
        double distance = restaurant.getDistance();
        Double rating = restaurant.getRating();
        Integer reviewCount = restaurant.getReviewCount();
        //String displayAddress=  b.getLocation();
        ArrayList hours = restaurant.getHours();
        String photoUrl = restaurant.getPhotoUrl();
        ArrayList categories = restaurant.getCategories();
        Log.i("stuff", "categories is: " + categories.toString());

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);

        /*Can't pass in restaurant as intent, so pass in each component in k,v pairs*/
        intent.putExtra("busId", busId);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("distance", distance);
        intent.putExtra("rating", rating);
        intent.putExtra("reviewCount", reviewCount);
        intent.putExtra("hours", hours);
        intent.putExtra("photoUrl", photoUrl);
        intent.putExtra("categories", categories);

        Log.i("stuff", "about to launch next activity: food");

        startActivity(intent);

        //also have buttons for google navigation, yelp page, uber eats
    }
}

//<activity
//            android:name=".MapsActivity"
//            android:label="@string/title_activity_maps"></activity> IN MANIFEST




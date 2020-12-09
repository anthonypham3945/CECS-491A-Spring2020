package com.example.quikpik;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*class that controls the navigation drawer and manages scene fragments*/
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth; //initialize firebase authentication
    FirebaseUser currentUser; // initialize current user in the login
    DrawerLayout drawerLayout;//drawer for navigation
    ActionBarDrawerToggle actionBarDrawerToggle;//action of the navigation drawer
    Toolbar toolbar; // toolbar of the navigation
    NavigationView navigationView; // initialize how the navigation looks
    FragmentManager fragmentManager;//manager of the fragment scenes
    FragmentTransaction fragmentTransaction;//switches from fragment to fragment
    SupportMapFragment mapFragment;

    TextView mLocationTextView;

    private TextView title, alias;
    private ImageView boxArt;
    private RestaurantRecyclerViewAdapter adapter;
    private RecyclerView restaurantRecyclerView;
    Button refresh_button;
    TextView information_text;
    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String API_KEY2 = "Bearer FzyeiETw6JAgMPWfM62hPYyBlK0OYtNDHwPZ0XK9rhLz1zlFOb_qlL09OaQqJBnCuQCYciWuaBjvhW6pXC5tNvuh63yvaolx_6GCKeLu-PJ7C2e5fFBdBZJpDzF2X3Yx";

    public static final String TAG = "Yelp";

    private static final int PERMISSIONS_REQUEST = 100;

    private ListView mListView;
    private View mLayout;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String API_KEY = "AIzaSyBO39VlB_M8vKWDyOhIt_U8GwjGsoUEkBY";
    //private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    //private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";


    /*overrided method that creates the default state of the layout*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mapFragment = SupportMapFragment.newInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);//assigns tool bar to the toolbar in the xml file
        setSupportActionBar(toolbar);//gives toolbar action bar support
        mAuth = FirebaseAuth.getInstance();//gets the current athentication


        refresh_button = (Button) findViewById(R.id.refresh_button);
        information_text = (TextView) findViewById(R.id.information_text);

        makeAPICall();

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //simply makes another API call which will repopulate recycler list view on page.
                makeAPICall();
            }
        });

//        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                //FirebaseFirestore op = FirebaseFirestore.getInstance();
//                //DocumentReference ref = op.collection("user-rating").document();
//                String YourRatingValue = String.valueOf(rating);
//                //ref.set(YourRatingValue);
//                Toast.makeText(MainActivity.this, "Your Rating :" + YourRatingValue, Toast.LENGTH_SHORT).show();
//            }
//        });

        Intent intent = getIntent();
        String longitude = intent.getStringExtra("long");
        String latitude = intent.getStringExtra("lat");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mLayout = findViewById(R.id.container_fragment);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
            int radius = 1000;
            Double lng = location.getLongitude();
            Double lat = location.getLatitude();
            ArrayList<Place> list = search(lat, lng, radius);
            for(int i=0; i < list.size();i++) {
                System.out.println("Hello");
            }
//            if (list != null)
//            {
//                mListView = (ListView) findViewById(R.id.listView);
//                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
//                mListView.setAdapter(adapter);
//            }

        }



//        Location location = lm.getLastKnownLocation();
//        int radius = 1000;
//        Double lng = location.getLongitude();
//        Double lat = location.getLatitude();
//
//        ArrayList<Place> list = search(lat, lng, radius);
//        for(int i=0; i < list.size();i++) {
//            System.out.println("Hello");
//        }
//        if (list != null)
//        {
//            mListView = (ListView) findViewById(R.id.listView);
//            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
//            mListView.setAdapter(adapter);
//        }







        //Check whether GPS tracking is enabled//

        //LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
        }

        //Check whether this app has access to the location permission//

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the location permission has been granted, then start the TrackerService//

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {

//If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }


        currentUser = mAuth.getCurrentUser();//gets the current user logged in
        drawerLayout = findViewById(R.id.drawer);//the actual navigation drawer
        navigationView = findViewById(R.id.navigationView);//how it looks
        navigationView.setNavigationItemSelectedListener(this);//make the items selectable in the navigation drawer

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.open, R.string.close); // initialize the action of the drawer eithern open or close
        drawerLayout.addDrawerListener(actionBarDrawerToggle);//passes it to the drawerlayout
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);//showws if navigator is opened
        actionBarDrawerToggle.syncState();//syncs the drawer to the state

        //load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();
        updateNavHeader();//updates user in the header
        Load_setting();
        //showRestaurants();
        //

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makeAPICall()
    {
        //list of restaurants to display
        List<YelpRestaurant> restaurants = new ArrayList<>();
        //retrofit initialization
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpServiceInterface service = retrofit.create(YelpServiceInterface.class);
        //used for getting location
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        //list of addresses (used in getting city/state name based on location.)


        //sets up recyclerview
        adapter = new RestaurantRecyclerViewAdapter(this, restaurants);
        restaurantRecyclerView = findViewById(R.id.restaurant_list);
        restaurantRecyclerView.setAdapter(adapter);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        restaurantRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);

        //location initialized to Mountain View, California
        //latitude and longitude included for Mountain View.
        Double lat=37.3861;
        Double lng=122.0839;
        String city = "Mountain View";
        String state = "CA";
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //starts location manager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Permissions check
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //gets location
            Location location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
            int radius = 1000;
            //latitude and longitude coordinates
            lng = location.getLongitude();
            lat = location.getLatitude();
        }
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //grab city and state name if available, otherwise defaults to "Mountain View CA"
        if (addresses.size() > 0) {
            city = (addresses.get(0).getLocality());
            state = (addresses.get(0).getAdminArea());
        }
        else {

        }

        //Combines city/state to be formatted for API string
        String searchLocation = city + ", " + state;

        //TODO: Use Firebase Preferences instead of preset preferences!!!
        String[] foods = {"Tacos",  "Pizza", "Chicken", "Ramen", "Juice", "Ice Cream", "Chinese", "Mexican", "Burgers"};

        //random number to select from list of food preferences.
        //TODO: instead, make a shuffled list based on ALL user preferences.
        int randomNum = ThreadLocalRandom.current().nextInt(0, foods.length-1 + 1);

        //sets up refresh button to display text
        Button refreshButton;
        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setText("Refresh Listings");


        //The actual API call code.
        //consists of [KEY, TERM, LATITUDE, LONGITUDE]
        FirebaseAuth mAuth;//user in the firebase db
        User user;
        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        user = new User(currentUser.getEmail()); //creates an object for user to store their info
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user-preferences").document(currentUser.getEmail());
        Double finalLat = lat;
        Double finalLng = lng;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        ArrayList<String> choices = (ArrayList<String>) doc.get("Restaurant Choices");
                        if(choices.size() != 0) {
                            int randomNum = ThreadLocalRandom.current().nextInt(0, choices.size());
                            String choice = choices.get(randomNum);
                            //sets up a string in HTML (the <b></b> allows for bold font)
                            String informationText = String.format("Showing listings for <b>%s</b> <br> in <b>%s</b> <br> @ <b>(%.2f lat, %.2f long)</b>", choice, searchLocation, finalLat, finalLng);
                            //sets the TextView to the string created, displaying what they're searching for and the location.
                            information_text.setText(Html.fromHtml(informationText));
                            service.getTasks(API_KEY2, choice, finalLat, finalLng).enqueue(new retrofit2.Callback<YelpSearchResult>() {
                                @Override
                                public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                                    //Log.i(TAG, response.toString());
                                    if (response.body() == null) {
                                        Log.i(TAG, "Did not receive valid response");
                                    }
                                    restaurants.addAll(response.body().restaurants);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<YelpSearchResult> call, Throwable t) {
                                    Log.v(TAG, "Failed");
                                }
                            });

                        } else {
                            //sets up a string in HTML (the <b></b> allows for bold font)
                            String choice = foods[randomNum];
                            String informationText = String.format("Showing listings for <b>%s</b> <br> in <b>%s</b> <br> @ <b>(%.2f lat, %.2f long)</b>", choice, searchLocation, finalLat, finalLng);
                            //sets the TextView to the string created, displaying what they're searching for and the location.
                            information_text.setText(Html.fromHtml(informationText));
                            service.getTasks(API_KEY2, choice, finalLat, finalLng).enqueue(new retrofit2.Callback<YelpSearchResult>() {
                                @Override
                                public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                                    //Log.i(TAG, response.toString());
                                    if(response.body() == null) {
                                        Log.i(TAG, "Did not receive valid response");
                                    }
                                    restaurants.addAll(response.body().restaurants);
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onFailure(retrofit2.Call<YelpSearchResult> call, Throwable t) {
                                    Log.v(TAG, "Failed");
                                }
                            });
                        }
                    } else {

                    }
                }

            }
        });


    }







    public void startRestaurants() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();
        }
    }

    public void showRestaurants(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, show restaurants
            Snackbar.make(mLayout,
                    "Location permission available. Show restaurants.",
                    Snackbar.LENGTH_SHORT).show();
            startRestaurants();
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Location access is required to display restaurants near you.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting location permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }



    public static ArrayList<Place> search(double lat, double lng, int radius) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));
            sb.append("&type=restaurant");
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                resultList.add(place);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }


    //Value Object for the ArrayList
    public static class Place {
        private String reference;
        private String name;

        public Place(){
            super();
        }
        @Override
        public String toString(){
            return this.name; //This is what returns the name of each restaurant for array list
        }
    }


    /*this method updates the navigator header with the current user email and photo*/
    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.navigationView);//initializes the navigation view
        View headerView = navigationView.getHeaderView(0);//gets the header view
        //TextView navUsername = headerView.findViewById(R.id.nav_username); // when we have more user info we plan to display their name as well
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);//gets user email
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);//gets user picture

        navUserEmail.setText(currentUser.getEmail());//sets user email to the text in the header
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//...then start the GPS tracking service//

            startTrackerService();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

//Start the TrackerService//

    private void startTrackerService() {
        //startService(new Intent(this, TrackingService.class));
        requestLocationUpdates();

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

//Close MainActivity//

    }


    //Initiate the request to track the device's location//

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(1000000000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //Get a reference to the database, so your app can perform read and write operations//
                    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    DocumentReference ref = db.collection("user-location").document(currentUser.getEmail());
                    Location location = locationResult.getLastLocation();

                    if (location != null) {
                        //Save the location data to the database//
                            System.out.println("Saved Location.");
                            ref.set(location);
                    }
                }
            }, null);
        }
    }
    private void Load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT",false);
        if(chk_night){
            drawerLayout.setBackgroundColor(Color.parseColor("#222222"));
            toolbar.setBackgroundColor(Color.parseColor("#222222"));
            information_text.setTextColor(Color.parseColor(("white")));
            //navigationView.setBackgroundColor(Color.parseColor("#222222"));

        }
        else{
            information_text.setTextColor(Color.parseColor("black"));
            drawerLayout.setBackgroundColor(Color.parseColor("white"));
            toolbar.setBackgroundColor(Color.parseColor("#0097a7"));

        }
    }




    /*method that hides the navigator when the back button is pressed*/
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if(drawer.isDrawerOpen(GravityCompat.START)){ // if the navigator is open
            drawer.closeDrawer(GravityCompat.START);//close it
        }else{
            super.onBackPressed();// do normal back button functions
        }
    }


    /*overrided method that swithes fragments when the item is clicked on the navigator*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);//close the drawer when an item is selected
        if(item.getItemId() == R.id.home){//if the home item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), MainActivity.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.nearby_place){//if the maps item is clicked
//            fragmentManager = getSupportFragmentManager();
//            fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_fragment, new MapsFragment());//replace the current fragment with the maps fragment
//            fragmentTransaction.commit();//commits the changes to the app
            Intent profileActivity = new Intent(getApplicationContext(), MapsActivity.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.profile){ //if the logout item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), Preferences.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
//        else if(item.getItemId() == R.id.lookup) { //if the restaurant lookup item is clicked
//            Intent profileActivity = new Intent(getApplicationContext(), YelpActivity.class);//takes the user to the restaurant lookup page
//            startActivity(profileActivity);//start the lookup activity
//            finish();//finishes the process
//        }

//        else if(item.getItemId() == R.id.search) { //if the restaurant lookup item is clicked
//            Intent searchActivity = new Intent(getApplicationContext(), SearchFragment.class);//takes the user to the restaurant lookup page
//            startActivity(searchActivity);//start the lookup activity
//            finish();//finishes the process
//        }
        else if(item.getItemId() == R.id.settings){ //if the logout item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), Preference.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.logout){ //if the logout item is clicked
            FirebaseAuth.getInstance().signOut();//signs the current user out
            Intent loginActivity = new Intent(getApplicationContext(), Login.class);//takes the user back to the login screen
            startActivity(loginActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.saved){ //if the logout item is clicked
            Intent savedActivity = new Intent(getApplicationContext(), SavedRestaurant.class);//takes the user back to the login screen
            startActivity(savedActivity);//start the login activity
            finish();//finishes the process
        }

        return true;
    }


    private EditText editText;
    public void sendMessage(View view) {
        /*
        String message = editText.getText().toString();
        if (message.length() > 0) {
            editText.getText().clear();
        }
        */
    }

    protected void onResume(){
        Load_setting();
        super.onResume();
    }
}

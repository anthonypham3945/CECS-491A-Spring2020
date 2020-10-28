package com.example.quikpik;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mapFragment = SupportMapFragment.newInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);//assigns tool bar to the toolbar in the xml file
        setSupportActionBar(toolbar);//gives toolbar action bar support
        mAuth = FirebaseAuth.getInstance();//gets the current athentication

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Double lng = location.getLongitude();
        Double lat = location.getLatitude();
        int radius = 1000;



        ArrayList<Place> list = search(lat, lng, radius);
        for(int i=0; i < list.size();i++) {
            System.out.println("Hello");
        }
        if (list != null)
        {
            mListView = (ListView) findViewById(R.id.listView);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
            mListView.setAdapter(adapter);
        }





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
            //navigationView.setBackgroundColor(Color.parseColor("#222222"));
        }
        else{
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
        else if(item.getItemId() == R.id.lookup) { //if the restaurant lookup item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), RestaurantLookup.class);//takes the user to the restaurant lookup page
            startActivity(profileActivity);//start the lookup activity
            finish();//finishes the process
        }

        else if(item.getItemId() == R.id.search) { //if the restaurant lookup item is clicked
            Intent searchActivity = new Intent(getApplicationContext(), SearchFragment.class);//takes the user to the restaurant lookup page
            startActivity(searchActivity);//start the lookup activity
            finish();//finishes the process
        }
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

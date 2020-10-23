package com.example.quikpik;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import org.w3c.dom.Text;

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
    private static final int PERMISSIONS_REQUEST = 100;



    /*overrided method that creates the default state of the layout*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);//assigns tool bar to the toolbar in the xml file
        setSupportActionBar(toolbar);//gives toolbar action bar support
        mAuth = FirebaseAuth.getInstance();//gets the current athentication

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //FirebaseFirestore op = FirebaseFirestore.getInstance();
                //DocumentReference ref = op.collection("user-rating").document();
                String YourRatingValue = String.valueOf(rating);
                //ref.set(YourRatingValue);
                //Toast.makeText(MainActivity.this, "Your Rating :" + YourRatingValue, Toast.LENGTH_SHORT).show();
            }
        });


        //Check whether GPS tracking is enabled//

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        /*else if(item.getItemId() == R.id.nearby_restaurants){//if the maps item is clicked
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new PlacesRecyclerViewAdapter());//replace the current fragment with the maps fragment
            fragmentTransaction.commit();//commits the changes to the app
        }*/
        else if(item.getItemId() == R.id.profile){ //if the logout item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), Preferences.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.lookup) { //if the restaurant lookup item is clicked
            Intent profileActivity = new Intent(getApplicationContext(), YelpActivity.class);//takes the user to the restaurant lookup page
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

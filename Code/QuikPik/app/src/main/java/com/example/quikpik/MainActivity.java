package com.example.quikpik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    /*overrided method that creates the default state of the layout*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);//assigns tool bar to the toolbar in the xml file
        setSupportActionBar(toolbar);//gives toolbar action bar support
        mAuth = FirebaseAuth.getInstance();//gets the current athentication
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
            Intent profileActivity = new Intent(getApplicationContext(), Profile.class);//takes the user back to the login screen
            startActivity(profileActivity);//start the login activity
            finish();//finishes the process
        }
        else if(item.getItemId() == R.id.maps){//if the maps item is clicked
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MapsFragment());//replace the current fragment with the maps fragment
            fragmentTransaction.commit();//commits the changes to the app
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
}

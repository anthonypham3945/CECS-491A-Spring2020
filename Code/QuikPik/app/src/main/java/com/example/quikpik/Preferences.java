package com.example.quikpik;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Preferences extends AppCompatActivity{

    Button restaurants, done, dressCodes, flavors, allergies, cancel;
    TextView title, txtPreference;
    String[] restaurantList, dressList, flavorList, allergyList;
    private FirebaseAuth mAuth;//user in the firebase db
    User user;
    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();   //retrieves an instance of the firestore database
        restaurants = (Button) findViewById(R.id.btnRestaurants);   // initialize button to open list of restaurants
        dressCodes = (Button) findViewById(R.id.btnDresscode);  //initialize button for dress codes
        flavors = (Button) findViewById(R.id.btnFlavor);  //initialize button for dress codes
        allergies = (Button) findViewById(R.id.btnAllergy);  //initialize button for dress codes

        txtPreference = (TextView) findViewById(R.id.txtPreferences);


        done = (Button) findViewById(R.id.done);   //button to open list of restaurants
        cancel = (Button) findViewById(R.id.cancel);   //button to open list of restaurants

        restaurantList = getResources().getStringArray(R.array.restaurant_type);        //get array list of restaurants stored in array file
        dressList = getResources().getStringArray(R.array.Dress_Codes);        //get array list of dress codes stored in array file
        flavorList = getResources().getStringArray(R.array.flavor_type);        //get array list of dress codes stored in array file
        allergyList = getResources().getStringArray(R.array.allergy_type);        //get array list of dress codes stored in array file

        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        user = new User(currentUser.getEmail()); //creates an object for user to store their info

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user-preferences").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        txtPreference.setText("Preferences: " + doc.getData().toString());
                    } else {
                        txtPreference.setText("Preferences:");
                    }
                }

            }
        });

        final ArrayList<Integer> userRestaurants = new ArrayList<>();   //holds items that the user has selected
        restaurants.setOnClickListener(new View.OnClickListener() { //listener for the restaurant button
            @Override
            public void onClick(View v) {   //calls the create check list method to create the alert dialog
                createCheckList("Restaurant Types", restaurantList, userRestaurants);    //passes the the title and list of strings
            }
        });

        final ArrayList<Integer> userDresses = new ArrayList<>();   //holds items that the user has selected
        dressCodes.setOnClickListener(new View.OnClickListener() {  //listener for the dress code button
            @Override
            public void onClick(View v) {       //calls the create check list method to create the alert dialog
                createCheckList("Dress Codes", dressList, userDresses);      //passes the the title and list of strings
            }
        });

        final ArrayList<Integer> userFlavors = new ArrayList<>();   //holds items that the user has selected
        flavors.setOnClickListener(new View.OnClickListener() {  //listener for the dress code button
            @Override
            public void onClick(View v) {       //calls the create check list method to create the alert dialog
                createCheckList("Flavor Types", flavorList, userFlavors);      //passes the the title and list of strings
            }
        });

        final ArrayList<Integer> userAllergy = new ArrayList<>();   //holds items that the user has selected
        allergies.setOnClickListener(new View.OnClickListener() {  //listener for the dress code button
            @Override
            public void onClick(View v) {       //calls the create check list method to create the alert dialog
                createCheckList("Allergy Types", allergyList, userAllergy);      //passes the the title and list of strings
            }
        });

        final DocumentReference ref = db.collection("user-preferences").document(currentUser.getEmail());   //initialize a reference to the database of the current user

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.set(user.getPreferences());    //creates or updates the data collection of the user
                startActivity(new Intent(getApplicationContext(),MainActivity.class));//go back to login screen
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));//go back to login screen
            }
        });
    }


//
//    /*method that hides the navigator when the back button is pressed*/
//    public void onBackPressed(){
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
//        if(drawer.isDrawerOpen(GravityCompat.START)){ // if the navigator is open
//            drawer.closeDrawer(GravityCompat.START);//close it
//    }else{
//        super.onBackPressed();// do normal back button functions
//    }
//}


    /**
     *
     * @param title of the builder window
     * @param list of strings to display for each check box
     */
    private void createCheckList(final String title, final String[] list, final ArrayList<Integer> userItems) {
        final ArrayList<String> choices = new ArrayList<>();        //stores the array of strings of the choices the user has checked
        final boolean[] checkedItems = new boolean[list.length];      // initialize boolean array size for each check box for the neutral button
        //final ArrayList<Integer> userItems = new ArrayList<>();   //holds items that the user has selected
        AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);    //build a pop-up display for 1-3 buttons allowing for chaining of calls
        builder.setTitle(title);  //set title text  for the dialog's window
        for(int i = 0; i < userItems.size(); i++) {
            checkedItems[userItems.get(i)] = true;
        }
        builder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {    //passes the array string of the list of restaurants, and the array of check boxes of each item
            @Override                                               //a method is invoked when a button in the dialog is pressed
            public void onClick(DialogInterface dialog, int position, boolean isChecked) { //listener for each check boxes

                if(isChecked) { //if item is checked return true
                    if(!userItems.contains(position)) { //if the checked item is not in the array of checked items
                        userItems.add(position);    //add the checked item in the array
                    }
                } else if(userItems.contains(position)) {   //if the item is in the array of user selected items
                    userItems.remove(userItems.indexOf(position));  //remove the item from the list
                }
            }
        });

        builder.setCancelable(false);   //sets whether the dialog is cancellable or not
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {     //button for Ok to exit

            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < userItems.size(); i++) { //iterates through the items the user has checked
                    if(!choices.contains(list[userItems.get(i)])) {     //gets the item from the position from the list and checks if the array of choices contains that item
                        choices.add(list[userItems.get(i)]);    //if not then add that item
                    }
                }
                if(title.equals("Restaurant Types")) {  //if the title of the builder matches
                    user.addRestaurantChoices(choices); //then add the list of items in the user object
                }
                if(title.equals("Dress Codes")) {   //if the title  of the builder matches
                    user.addDressChoices(choices);  //then add the list of items in the user object
                }
                if(title.equals("Flavor Types")) {   //if the title  of the builder matches
                    user.addFlavorChoices(choices);  //then add the list of items in the user object
                }
                if(title.equals("Allergy Types")) {   //if the title  of the builder matches
                    user.addAllergyChoices(choices);  //then add the list of items in the user object
                }
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {  //sets a listener to be invoked when the button is pressed
            @Override
            public void onClick(DialogInterface dialog, int which) {    //exits the interface on click
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {   //calls the method listener to uncheck all the boxes
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < checkedItems.length; i++) {  //loops through all the checked items
                    userItems.clear();          //removes all the items the user selected
                }
            }
        });
        AlertDialog dialog = builder.create();  //creates the alert dialog with the arguments set in the builder
        dialog.show();  //displays the dialog
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#f34235"));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#f34235"));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#f34235"));
    }

    public void onBackPressed() {
        Intent intent=new Intent(Preferences.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

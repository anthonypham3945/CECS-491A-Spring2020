package com.example.quikpik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    Button restaurants, done, dressCodes;
    TextView title;
    String[] restaurantList, dressList;
    //boolean[] checkedItems; //holds all the checked items
    private FirebaseAuth mAuth;//user in the firebase db
    User user;
    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();   //retrieves an instance of the firestore database

        restaurants = (Button) findViewById(R.id.btnRestaurants);   // initialize button to open list of restaurants
        dressCodes = (Button) findViewById(R.id.btnDresscode);  //initialize button for dress codes
        done = (Button) findViewById(R.id.done);   //button to open list of restaurants
        restaurantList = getResources().getStringArray(R.array.restaurant_type);        //get array list of restaurants stored in array file
        dressList = getResources().getStringArray(R.array.Dress_Codes);        //get array list of dress codes stored in array file

        //checkedItems = new boolean[restaurantList.length];      // initialize boolean array size for each check box

        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        user = new User(currentUser.getEmail()); //creates an object for user to store their info

        restaurants.setOnClickListener(new View.OnClickListener() { //listener for the restaurant button
            @Override
            public void onClick(View v) {   //calls the create check list method to create the alert dialog
                createCheckList("Restaurant Types", restaurantList);    //passes the the title and list of strings
            }
        });

        dressCodes.setOnClickListener(new View.OnClickListener() {  //listener for the dress code button
            @Override
            public void onClick(View v) {       //calls the create check list method to create the alert dialog
                createCheckList("Dress Codes", dressList);      //passes the the title and list of strings
            }
        });

        final DocumentReference ref = db.collection("qp-users").document(currentUser.getEmail());   //initialize a reference to the database of the current user

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.set(user.getUser());    //creates or updates the data collection of the user
                startActivity(new Intent(getApplicationContext(),MainActivity.class));//go back to login screen
            }
        });
    }

    /**
     *
     * @param title of the builder window
     * @param list of strings to display for each check box
     */
    private void createCheckList(final String title, final String[] list) {
        final ArrayList<String> choices = new ArrayList<>();        //stores the array of strings of the choices the user has checked
        final boolean[] checkedItems = new boolean[list.length];      // initialize boolean array size for each check box for the neutral button
        final ArrayList<Integer> userItems = new ArrayList<>();   //holds items that the user has selected
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);    //build a pop-up display for 1-3 buttons allowing for chaining of calls
        builder.setTitle(title);  //set title text  for the dialog's window
        builder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {    //passes the array string of the list of restaurants, and the array of check boxes of each item
            @Override                                               //a method is invoked when a button in the dialog is pressed
            public void onClick(DialogInterface dialog, int position, boolean isChecked) { //listener for each check boxes
                if(isChecked) { //if item is checked return true
                    if(!userItems.contains(position)) { //if the checked item is not in the array of checked items
                        userItems.add(position);    //add the checked item in the array
                    }
                } else if(userItems.contains(position)) {   //if the item is in the array of user selected itemsd
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
                    checkedItems[i] = false;    //set each checked item to false to uncheck each box
                    userItems.clear();          //removes all the items the user selected
                }
            }
        });
        AlertDialog dialog = builder.create();  //creates the alert dialog with the arguments set in the builder
        dialog.show();  //displays the dialog
    }
}

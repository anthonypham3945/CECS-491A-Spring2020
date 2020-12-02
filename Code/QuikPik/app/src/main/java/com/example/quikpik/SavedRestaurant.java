package com.example.quikpik;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SavedRestaurant extends AppCompatActivity {
    private RecyclerView restaurantRecyclerView;
    private FirebaseAuth mAuth;//user in the firebase db
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_restaurant);
        setTitle("Saved Restaurants");
        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        List<Restaurant> restaurants = new ArrayList<>();
        adapter = new CustomAdapter(restaurants);
        restaurantRecyclerView = findViewById(R.id.restaurant_list);
        restaurantRecyclerView.setAdapter(adapter);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        restaurantRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user-restaurants").document(currentUser.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                RestaurantList list = documentSnapshot.toObject(RestaurantList.class);
                if (list != null)
                {
                    for(Restaurant rest : list.getRestaurants()) {
                        restaurants.add(rest);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setContentView(R.layout.activity_main);
            }
        });

    }

    //when back pressed, returns to main activity.
    public void onBackPressed() {
        Intent intent=new Intent(SavedRestaurant.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
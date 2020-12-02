package com.example.quikpik;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
    private TextView title, reviewCount, addressText;
    private ImageView restaurantImage;
    private Button saveButton;
    private String restaurantUrl;
    private FirebaseAuth mAuth;
    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        title = itemView.findViewById(R.id.restaurant_name);
        reviewCount = itemView.findViewById(R.id.textReview);
        restaurantImage = itemView.findViewById(R.id.imageView);
        addressText = itemView.findViewById(R.id.textAddress);
        saveButton = itemView.findViewById(R.id.edit);
        //alias = itemView.findViewById(R.id.fragment_alias);
        //boxArt = itemView.findViewById(R.id.fragment_restaurant_image);
    }

    public void bind(YelpRestaurant restaurant) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();   //retrieves an instance of the firestore database
        title.setText(restaurant.name);
        reviewCount.setText(restaurant.rating + "/5 ★ (" + restaurant.numReviews + " Reviews)");
        addressText.setText(restaurant.address);
        Glide.with(itemView).load(restaurant.imageURL).into(restaurantImage);
        restaurantUrl = restaurant.url;
        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        final DocumentReference ref = db.collection("user-restaurants").document(currentUser.getEmail());   //initialize a reference to the database of the current user
        Restaurant rest = new Restaurant(restaurant.name, restaurant.rating, restaurant.imageURL, restaurant.address, restaurant.numReviews);
        RestaurantList list = new RestaurantList();
        list.addRestaurants(rest);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Added " + restaurant.name + " to Saved Restaurants", Toast.LENGTH_LONG).show();
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()) {
                                ref.update("restaurants", FieldValue.arrayUnion(rest));
                            } else {
                                ref.set(list);
                            }
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantUrl));
        view.getContext().startActivity(intent);
    }


}

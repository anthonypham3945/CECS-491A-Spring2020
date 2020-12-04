package com.example.quikpik;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Restaurant> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, reviewCount, addressText;
        private ImageView restaurantImage;
        private Button deleteButton;
        private String restaurantUrl;
        private FirebaseAuth mAuth;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = itemView.findViewById(R.id.restaurant_name);
            reviewCount = itemView.findViewById(R.id.textReview);
            restaurantImage = itemView.findViewById(R.id.imageView);
            addressText = itemView.findViewById(R.id.textAddress);
            deleteButton = itemView.findViewById(R.id.edit);
        }
        public void bind(Restaurant restaurant) {
            title.setText(restaurant.getName());
            reviewCount.setText(restaurant.getRating() + "/5 ★ (" + restaurant.getReviewCount()+ " Reviews)");
            addressText.setText(restaurant.getLocation());
            Glide.with(itemView).load(restaurant.getImageURL()).into(restaurantImage);

        }
    }
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<Restaurant> dataSet) {
        localDataSet = dataSet;
    }
    // Create new views (invoked by the layout manager)
    private FirebaseAuth mAuth;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_restaurant, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Restaurant rest = localDataSet.get(position);
        viewHolder.bind(rest);
        viewHolder.            deleteButton.setText("Delete");
        mAuth = FirebaseAuth.getInstance();//access firebase for current user
        final FirebaseFirestore db = FirebaseFirestore.getInstance();   //retrieves an instance of the firestore database
        final FirebaseUser currentUser = mAuth.getCurrentUser(); //initializes current logged in user
        final DocumentReference ref = db.collection("user-restaurants").document(currentUser.getEmail());   //initialize a reference to the database of the current user
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Deleted " + rest.getName() + " from Saved Restaurants", Toast.LENGTH_LONG).show();
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()) {
                                ref.update("restaurants", FieldValue.arrayRemove(rest));
                                removeAt(position);
                            } else {
                                Toast.makeText(v.getContext(), "DOES NOT EXIST", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
    private void removeAt(int position) {
        localDataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, localDataSet.size());
    }
}

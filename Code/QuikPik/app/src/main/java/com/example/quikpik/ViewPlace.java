package com.example.quikpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.quikpik.Model.Photos;
import com.example.quikpik.Model.PlaceDetail;
import com.example.quikpik.Remote.IGoogleAPIService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlace extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    TextView place_hours,place_address, place_name;
    Button btn_directions;


    IGoogleAPIService mService;
    PlaceDetail mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        mService = Common.getGoogleAPIService();

        photo = (ImageView) findViewById(R.id.photo);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar1);
        place_address = (TextView) findViewById(R.id.place_address);
        place_name = (TextView) findViewById(R.id.place_name);
        place_hours = (TextView) findViewById(R.id.place_hour);
        btn_directions = (Button) findViewById(R.id.btn_show_on_map);


        place_name.setText("");
        place_hours.setText("");
        btn_directions.setText("");

        btn_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });


        //PHOTO
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length >0){
            Picasso.with(this).load(getPhotosOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000)).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.error_24).into(photo);
        }

        //RATING
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating())){
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else{
            ratingBar.setVisibility(View.GONE);
        }

        //HOURS
        if(Common.currentResult.getOpening_hours() !=null){
            place_hours.setText("Open Now: "+Common.currentResult.getOpening_hours().getOpen_now());
        }
        else{
            place_hours.setVisibility(View.GONE);
        }

        //ADDRESS & NAME
        mService.getPlaceDetail(getPlaceDetailUrl(Common.currentResult.getPlace_id())).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                mPlace = response.body();
                place_address.setText(mPlace.getResult().getFormatted_address());
                place_name.setText(mPlace.getResult().getName());

            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

            }
        });

    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?place_id="+place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getPhotosOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

}
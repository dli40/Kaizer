package com.dli40.kaizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    public static final String LOAD_METHOD_ID = "load_method_id";
    public static final int LOAD_METHOD_CODE = 100; //for reviews
    static final String API_KEY = "1peIHcT_Jl8waGCfb2AS3IeUoJKgq8u8lpYz-CNa3ZDMKrS2biLwvDMiXdXmIksuZ1hb6SgifKL4XlUVxURRequl3jiUu_dj2jyyRzFFGqZefU1-yRRR6daBJGlOW3Yx";

    private ImageView imageView, ratingImageView;
    private TextView nameTextView, priceTextView, reviewCountTextView, categoriesTextView, questionText, reviewText;
    private Button getReviewsButton, yesButton, noButton;

    private ReviewClass reviewClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imageView = findViewById(R.id.imageView); //initialize all components here
        ratingImageView = findViewById(R.id.ratingImageView);

        nameTextView = findViewById(R.id.nameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        reviewCountTextView = findViewById(R.id.reviewCountTextView);
        categoriesTextView = findViewById(R.id.categoriesTextView);

        questionText = findViewById(R.id.questionText);
        reviewText = findViewById(R.id.reviewText);

        getReviewsButton = findViewById(R.id.getReviewsButton);
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);

        questionText.setText("Do you want to eat here?");

        //Log.i("stuff","made it to result activit, justbefore intentrs");

        //get components from intent

        Intent intent = getIntent();

        final String busId = intent.getStringExtra("busId");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        Double distance = intent.getDoubleExtra("distance", 0.0);
        Double rating = intent.getDoubleExtra("rating", 0.0);
        Integer reviewCount = intent.getIntExtra("reviewCount", 0);
        ArrayList hours = intent.getStringArrayListExtra("hours"); //ASSUMES IS STRING LMAO IDK
        ArrayList categories = intent.getStringArrayListExtra("categories");
        String photoUrl = intent.getStringExtra("photoUrl");

        //Log.i("stuff","made it to result activity, just after intnets");


        updateUI(busId, name, price, distance, rating, reviewCount, hours, photoUrl, categories); //UPDATE UI

        getReviewsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getReviews(busId);//probably need to create anothe factory???? how do itranser this over
            }
        }); //for end of listener setting


        //Log.i("stuff","Updeated UI");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateUI(String busId, String name, String price, Double distance, Double rating,
                         Integer reviewCount, ArrayList<String> hours, String photoUrl, ArrayList<String> categories) {
        nameTextView.setText(name);
        priceTextView.setText(price);
        reviewCountTextView.setText(String.valueOf(reviewCount));
        reviewText.setText("reviews");
        //ratingTextView.setText(rating+"");

        //DOING NOTHING WITH DISTANCE
        //NOTHING WITH HOURS
        //NJOTHING WITH BUISD, but that will be for getting reviews

        //   setCategories(categories, categoriesTextView);
//        Log.i("stuff","about to update catgories");
//        updateCategories(categoriesTextView,categories);
//        Log.i("stuff","successfully updated categories, about to set rating");
        setRating(ratingImageView, rating);
        updatePhoto(photoUrl, imageView);

    }

//    private void updateCategories(TextView categoriesTextView, ArrayList<String> categories)
//    { // hopefully categories is string
//        int size = categories.size();
//        if(size==0)
//        {
//            categoriesTextView.setText("No category");
//            return;
//        }
//        String x = categories.get(0);
//        if (size>1)
//        {
//            x += ", "+categories.get(1);
//        }
//        categoriesTextView.setText(x);
//    }

    private void setRating(ImageView ratingImageView, Double rating) {
        //TO IMPLEMENT: Different sizes for drawable folders, adding right characters basically
        String stringRating = rating + "";
        switch (stringRating) {
            case "1.0":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_1));
                break;
            case "1.5":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_1_half));
                break;
            case "2.0":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_2));
                break;
            case "2.5":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_2_half));
                break;
            case "3.0":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_3));
                break;
            case "3.5":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_3_half));
                break;
            case "4.0":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_4));
                break;
            case "4.5":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_4_half));
                break;
            case "5.0":
                ratingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stars_small_5));
                break;
        }

    }

    private void updatePhoto(String photoUrl, ImageView imageView) {
        Picasso.get().load(photoUrl).into(imageView);
    }


    public void getReviews(String busId) {
        YelpFusionApiFactory factory = new YelpFusionApiFactory();
        Log.i("stuff", "created new yelp api factory");
        try {
            Log.i("stuff", "about to create new feusion pai, same key");
            YelpFusionApi yelpFusionApi = factory.createAPI(API_KEY);
            Log.i("stuff", "created mew fusion api");
            makeReviewsCall(yelpFusionApi, busId);

            updateReviews(reviewClass);
            Log.i("stuff", "finished updating review actibity");
        } catch (IOException e) {
            Log.i("stuff", "bad! io error REVIEW");
        }
    }

    public void makeReviewsCall(YelpFusionApi yelpFusionApi, String busId) {
        Callback<Reviews> callback = new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                int rating;
                String name, imgUrl, url, text, timeCreated;
                // ArrayList<ReviewClass> reviewList = new ArrayList<>();

                Reviews reviews = response.body();
                ArrayList<Review> revs = reviews.getReviews(); //GET MORE REVIEWS LATER! ONE FOR NOW
                Review r = revs.get(0);
                rating = r.getRating();
                text = r.getText();
                name = r.getUser().getName();
                imgUrl = r.getUser().getImageUrl();
                url = r.getUrl();
                timeCreated = r.getTimeCreated();

                reviewClass = new ReviewClass(rating, name, imgUrl, text, timeCreated, url);
                Log.i("stuff", "reviewObject created: " + reviewClass.toString());
                //IS HIS ALLOWED?????
//                for (int i=0;i<revs.size();i++)
//                {
//                    Review review = revs.get(i);
//                    rating = review.getRating();
//
//                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.i("stuff", "O thats bad, failed to make async call to review objects");
            }
        };
        Log.i("stuff", "busId: " + busId);
        Call<Reviews> call = yelpFusionApi.getBusinessReviews(busId, "en");
        Log.i("stuff", "call object crerated, about to adddcallback");
        call.enqueue(callback);
    }

    public void updateReviews(ReviewClass reviewClass) {
        int rating = reviewClass.getRating();
        String name = reviewClass.getName();
        String imgUrl = reviewClass.getImgUrl();
        String text = reviewClass.getText();
        String timeCreated = reviewClass.getTimeCreated();
        String url = reviewClass.getUrl();

        Intent intent = new Intent(ResultActivity.this, ReviewActivity.class);

        intent.putExtra("name", name);
        intent.putExtra("rating", rating);
        intent.putExtra("text", text);
        intent.putExtra("timeCreated", timeCreated);
        intent.putExtra("url", url);
        intent.putExtra("imgUrl", imgUrl);

        startActivity(intent);
    }
}

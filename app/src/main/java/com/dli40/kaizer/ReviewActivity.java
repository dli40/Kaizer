package com.dli40.kaizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ReviewActivity extends AppCompatActivity {


    private ImageView imgUrlView, avatarView;
    private TextView reviewView, nameView, timeCreatedView, ratingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        String name, timeCreated, imgUrl, url, text;
        int rating;

        imgUrlView = findViewById(R.id.imgUrlView);
        avatarView = findViewById(R.id.avatarView);
        reviewView = findViewById(R.id.reviewView);
        nameView = findViewById(R.id.nameView);
        timeCreatedView = findViewById(R.id.timeCreated);
        ratingView = findViewById(R.id.ratingView);

        Intent intent = getIntent();
        rating = intent.getIntExtra("rating", 0);
        name = intent.getStringExtra("name");
        timeCreated = intent.getStringExtra("timeCreated");
        imgUrl = intent.getStringExtra("imgUrl");
        text = intent.getStringExtra("text");
        url = intent.getStringExtra("url");

        setDisplay(rating, name, timeCreated, imgUrl, url, text);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDisplay(int rating, String name, String timeCreated, String imgUrl, String url, String text) {
        ratingView.setText(rating);
        nameView.setText(name);
        timeCreatedView.setText(timeCreated);
        reviewView.setText(text);
        Picasso.get().load(imgUrl).into(imgUrlView);
    }
}

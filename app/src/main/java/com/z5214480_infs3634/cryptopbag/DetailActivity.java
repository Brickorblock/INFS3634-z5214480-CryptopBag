package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set empty layout to house fragment...
        setContentView(R.layout.detail_activity);

        //bind fragment to layout
        DetailFragment fragment = new DetailFragment();

        FragmentTransaction initTransaction = getSupportFragmentManager().beginTransaction();
        initTransaction.add(R.id.fragment_container, fragment);
        initTransaction.commit();

        //get intent from MainActivity
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.KEY);

        //then pass it to fragment as a bundle
        Bundle intentBundle = new Bundle();
        intentBundle.putString(MainActivity.KEY,message);
        fragment.setArguments(intentBundle);

        //? Note: I tried to use parent.getIntent() in DetailFragment.onActivityCreated()
        //  why didn't this work ?
    }


}

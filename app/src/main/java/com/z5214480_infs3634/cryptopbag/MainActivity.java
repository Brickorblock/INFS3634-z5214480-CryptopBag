package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.LaunchListener {
    public static final String KEY = "ActivityMain";

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity.java", "onCreate: hello world");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = findViewById(R.id.myRecyclerView);

        myRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        // using Coin arraylist as dataset
        ArrayList<Coin> myCoins = Coin.getCoins();

        mAdapter = new MyAdapter(myCoins, this);
        myRecyclerView.setAdapter(mAdapter);

        Log.d("MainActivity.java", "onCreate: onCreate successful");

    }

    public void launchActivity(int position){
        Log.d("MainActivity", "launchActivity: clicked");
        Intent intent = new Intent(this, DetailActivity.class);
        String message = getSymbol(position);

        intent.putExtra(KEY, message);
        startActivity(intent);
    }

    // method to get the symbol of the coin based on which position was clicked (e.g. 0 = BTC)
    public String getSymbol(int position) {
        String symbol = "";

        if (position == 0){
            symbol = "BTC";
        } else if (position == 1){
            symbol = "ETH";
        } else if (position == 2){
            symbol = "XRP";
        } else if (position == 3){
            symbol = "BCH";
        } else if (position == 4){
            symbol = "BCHSV";
        } else if (position == 5){
            symbol = "USDT";
        } else if (position == 6){
            symbol = "LTC";
        } else if (position == 7){
            symbol = "EOS";
        } else if (position == 8){
            symbol = "BNB";
        } else if (position == 9){
            symbol = "XLM";
        }

        return symbol;
    }



}
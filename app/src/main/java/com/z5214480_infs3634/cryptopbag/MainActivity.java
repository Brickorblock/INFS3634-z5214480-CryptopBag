package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.LaunchListener {
    public static final String KEY = "ActivityMain";

    private boolean mIsDualPane = false;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity.java", "onCreate: hello world");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create recycler view
        myRecyclerView = findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);

        // create an adapter
        // using Coin arraylist as dataset
        ArrayList<Coin> myCoins = Coin.getCoins();
        mAdapter = new MyAdapter(myCoins, this);
        myRecyclerView.setAdapter(mAdapter);

        Log.d("MainActivity.java", "onCreate: onCreate successful");

        //check if dual pane mode (tablet view)
        View detail_scrollview = findViewById(R.id.fragment_scrollview);
        if (detail_scrollview != null && detail_scrollview.getVisibility() == View.VISIBLE) {
            mIsDualPane = true;
        }
        Log.d("MainActivity", "onCreate: mIsDualPane = " + mIsDualPane);
    }

    public void launch(int position){
        Log.d("MainActivity", "launchActivity: clicked");
        String symbol = getSymbol(position);

        if (mIsDualPane == false){
            launchActivity(symbol);
        } else {
            attachDetailFragment(symbol);
        }
    }

    // launches detail in a separate activity
    public void launchActivity(String symbol){
        Intent intent = new Intent(this, DetailActivity.class);
        String message = symbol;

        intent.putExtra(KEY, message);
        startActivity(intent);
        Log.d("MainActivity", "launchActivity: Activity started");
    }

    //binds detail fragment to dual pane layout
    public void attachDetailFragment(String symbol){
        //bind fragment to layout
        DetailFragment fragment = new DetailFragment();

        // if fragment doesn't have content yet, initialise it
        // else, replace existing content
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            transaction.add(R.id.fragment_scrollview, fragment);
        } else {
            transaction.replace(R.id.fragment_scrollview, fragment);
        }
        transaction.commit();

        //send coin info to fragment as bundle
        Bundle intentBundle = new Bundle();
        intentBundle.putString(KEY, symbol);
        fragment.setArguments(intentBundle);

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
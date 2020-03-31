package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.z5214480_infs3634.cryptopbag.entities.Coin;
import com.z5214480_infs3634.cryptopbag.entities.CoinLoreResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.LaunchListener {
    public static final String KEY = "ActivityMain";
    private static List<Coin> coinList;
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


        // call CoinLore API for dataset of coins
        CoinLoreResponse coinLoreList = new Gson().fromJson(CoinLoreResponse.queryResult, CoinLoreResponse.class);
        List<Coin> myCoins = coinLoreList.getData();
        this.coinList = myCoins;

        // create an adapter
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

        if (mIsDualPane == false){
            launchActivity(position);
        } else {
            attachDetailFragment(position);
        }
    }

    // launches detail in a separate activity
    public void launchActivity(int position){
        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra(KEY, position);
        startActivity(intent);
        Log.d("MainActivity", "launchActivity: Activity started");
    }

    //binds detail fragment to dual pane layout
    public void attachDetailFragment(int position){
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
        intentBundle.putInt(KEY, position);
        fragment.setArguments(intentBundle);

    }

    // method to get the symbol of the coin based on which position was clicked (e.g. 0 = BTC)
    public String getSymbol(int position) {
        String targetSymbol = coinList.get(position).getSymbol();

        return targetSymbol;
    }

    // searches the list of coins and returns a specific coin, based on position clicked
    public static Coin coinSearch(int position){
        Coin targetCoin = coinList.get(position);

        return targetCoin;
    }

}
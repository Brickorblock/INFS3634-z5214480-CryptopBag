package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z5214480_infs3634.cryptopbag.entities.Coin;
import com.z5214480_infs3634.cryptopbag.entities.CoinLoreResponse;
import com.z5214480_infs3634.cryptopbag.entities.CoinService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        // prepare Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.coinlore.net/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        CoinService service = retrofit.create(CoinService.class);
        Call<CoinLoreResponse> call = service.get100Coins();

        // create an adapter
        mAdapter = new MyAdapter(coinList, this);
        myRecyclerView.setAdapter(mAdapter);

        //execute call asynchronously using enqueue

        call.enqueue(new Callback<CoinLoreResponse>() {
            @Override
            public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                // create CoinLoreResponse to capture api call response
                CoinLoreResponse coinResponse = response.body();

                List<Coin> myCoins = coinResponse.getData();
                setCoins(myCoins);
            }

            @Override
            public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
                //shows a toast message of failure
                String failMsg = "Could not connect to CoinLore API";
                Toast.makeText(MainActivity.this, failMsg, Toast.LENGTH_SHORT).show();
            }
        });

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

    public void setCoins(List<Coin> newCoins){
        //show the loading text
        findViewById(R.id.fetchingText).setVisibility(View.VISIBLE);

        coinList = newCoins;
        mAdapter = new MyAdapter(newCoins, this);
        myRecyclerView.setAdapter(mAdapter);

        String msg = "Coin list successfully updated.";
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

        //remove the loading text
        findViewById(R.id.fetchingText).setVisibility(View.INVISIBLE);
    }
}
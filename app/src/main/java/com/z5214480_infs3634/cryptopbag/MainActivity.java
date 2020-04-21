package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z5214480_infs3634.cryptopbag.entities.Coin;
import com.z5214480_infs3634.cryptopbag.entities.CoinDatabase;
import com.z5214480_infs3634.cryptopbag.entities.CoinLoreResponse;
import com.z5214480_infs3634.cryptopbag.entities.CoinService;

import java.io.IOException;
import java.util.ArrayList;
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

    public static CoinDatabase coinDb;
    private List<Coin> currCoins = new ArrayList<>();

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

        Log.d("MainActivity.java", "onCreate: onCreate successful");

        //check if dual pane mode (tablet view)
        View detail_scrollview = findViewById(R.id.fragment_scrollview);
        if (detail_scrollview != null && detail_scrollview.getVisibility() == View.VISIBLE) {
            mIsDualPane = true;
        }
        Log.d("MainActivity", "onCreate: mIsDualPane = " + mIsDualPane);

        //build db
        coinDb = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coinDb")
                .build();

        new NetworkTask().execute();
        new UpdateAdapterTask().execute();
    }

    //removes existing rows in CoinDatabase, calls API then fills CoinDatabase with response data
    public class NetworkTask extends AsyncTask<Void, Void, CoinLoreResponse>{

        @Override
        protected CoinLoreResponse doInBackground(Void... voids) {
            Log.d("TAG", "doInBackground: NetworkTask executing...");
            // drop all existing tables
            coinDb.coinDao().deleteAllCoins();

            // prepare Retrofit
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://api.coinlore.net/api/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            //execute API call
            CoinService service = retrofit.create(CoinService.class);
            Call<CoinLoreResponse> call = service.get100Coins();

            CoinLoreResponse coinLoreList = null;

            try {
                Response<CoinLoreResponse> coinResponse = call.execute();
                coinLoreList = coinResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // fill db with response data
            Coin[] coinArray = coinLoreList.getData().
                    toArray(new Coin[coinLoreList.getData().size()]);

            coinDb.coinDao().insertCoins(coinArray);

            return coinLoreList;
        }

        @Override
        protected void onPostExecute(CoinLoreResponse coinLoreResponse) {
            // simply show a toast & error UI if something went wrong during API call
            super.onPostExecute(coinLoreResponse);

            if (coinLoreResponse != null) {
                findViewById(R.id.errorConstraint).setVisibility(View.INVISIBLE);
                Log.d("TAG", "onPostExecute: API Call successful");
            } else {
                //shows an error on the UI if failed to get a response
                findViewById(R.id.errorConstraint).setVisibility(View.VISIBLE);
                findViewById(R.id.fetchingText).setVisibility(View.INVISIBLE);
                String msg = "Failed to retrieve coins from API";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class UpdateAdapterTask extends AsyncTask<Void, Void, List<Coin>>{
        @Override
        protected List<Coin> doInBackground(Void... voids) {
            //query DB for all coins
            List<Coin> allCoins = coinDb.coinDao().getCoins();
            Log.d("TAG", "doInBackground: Coins retrieved");

            currCoins = allCoins;

            return allCoins;
        }

        @Override
        protected void onPostExecute(List<Coin> coins) {
            // shows error message on toast/UI if database retrieval failed
            // updates coins adapter if succeeded
            super.onPostExecute(coins);

            if (coins != null) {
                findViewById(R.id.errorConstraint).setVisibility(View.INVISIBLE);
                setCoins(coins);
                Log.d("TAG", "onPostExecute: Execution Sucessful, setCoins called");
            } else {
                //shows an error on the UI if failed to get a response
                findViewById(R.id.errorConstraint).setVisibility(View.VISIBLE);
                findViewById(R.id.fetchingText).setVisibility(View.INVISIBLE);
                String msg = "Failed to retrieve coins from API";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void launch(int position){
        Log.d("MainActivity", "launchActivity: clicked");

        //convert position to coin ID
        String id = currCoins.get(position).getId();
        Log.d("TAG", "launch: id = " + id);


        if (mIsDualPane == false){
            launchActivity(id);
        } else {
            attachDetailFragment(id);
        }
    }

    // launches detail in a separate activity
    public void launchActivity(String id){
        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra(KEY, id);
        startActivity(intent);
        Log.d("MainActivity", "launchActivity: Activity started");
    }

    //binds detail fragment to dual pane layout
    public void attachDetailFragment(String id){
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
        intentBundle.putString(KEY, id);
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

    //sets the adapter using a dataset of List<Coin> and rebuilds the recycler view to show this
    // data set
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
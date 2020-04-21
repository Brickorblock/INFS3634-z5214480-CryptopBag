package com.z5214480_infs3634.cryptopbag;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.z5214480_infs3634.cryptopbag.entities.Coin;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    Activity parent = getActivity();

    TextView loadingText;
    ConstraintLayout contentConstraint;
    TextView value_text;
    TextView change1H_text;
    TextView change24H_text;
    TextView change7D_text;
    TextView marketcap_text;
    TextView volume24H_text;
    TextView name_text;
    TextView symbol_text;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //find views from the layout
        // note: getView() is required since findViewById() is not natively compatible w fragments
        value_text = getView().findViewById(R.id.value_text);
        change1H_text = getView().findViewById(R.id.change1H_text);
        change24H_text = getView().findViewById(R.id.change24H_text);
        change7D_text = getView().findViewById(R.id.change7D_text);
        marketcap_text = getView().findViewById(R.id.marketcap_text);
        volume24H_text = getView().findViewById(R.id.volume24H_text);
        loadingText = getView().findViewById(R.id.loadingText);
        contentConstraint = getView().findViewById(R.id.contentConstraint);

        name_text = getView().findViewById(R.id.name_text);
        symbol_text = getView().findViewById(R.id.symbol_text);

        // Old method: using coinSearch
//        //retrieve bundle
//        int message = getArguments().getInt(MainActivity.KEY);
//        Coin myCoin = MainActivity.coinSearch(message);

        //Use AsyncTask to execute db query for a single coin's details
        new CoinQueryTask().execute();

    }

    public class CoinQueryTask extends AsyncTask<Void, Void, Coin> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show some loading text and hide detail contents for now
            loadingText.setVisibility(View.VISIBLE);
            contentConstraint.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Coin doInBackground(Void... voids) {
            Log.d("TAG", "doInBackground: CoinQueryTask executing...");
            //retrieve bundle
            String id = getArguments().getString(MainActivity.KEY);

            //execute query
            Coin targetCoin = MainActivity.coinDb.coinDao().getCoinById(id);
            Log.d("TAG", "doInBackground: fetching coin with ID = " + id);

            return targetCoin;
        }

        @Override
        protected void onPostExecute(Coin myCoin) {
            super.onPostExecute(myCoin);
            if (myCoin != null) {
                //update UI contents
                name_text.setText(myCoin.getName());
                symbol_text.setText(myCoin.getSymbol());

                value_text.setText(myCoin.getPriceUsd());
                change1H_text.setText(myCoin.getPercentChange1h());
                change24H_text.setText(myCoin.getPercentChange24h());
                change7D_text.setText(myCoin.getPercentChange7d());
                marketcap_text.setText(myCoin.getMarketCapUsd());
                volume24H_text.setText(Double.toString(myCoin.getVolume24()));

                // reset loading UI
                loadingText.setVisibility(View.INVISIBLE);
                contentConstraint.setVisibility(View.VISIBLE);

                Log.d("TAG", "onPostExecute: CoinQueryTask success");
            }  else {
                //shows an error on the UI if failed to get a response
                loadingText.setText("Something went wrong...");
                String msg = "Failed get Coin details from database";
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }


}

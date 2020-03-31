package com.z5214480_infs3634.cryptopbag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z5214480_infs3634.cryptopbag.entities.Coin;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    Activity parent = getActivity();

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //retrieve bundle
        int message = getArguments().getInt(MainActivity.KEY);

        //find views from the layout
        // note: getView() is required since findViewById() is not natively compatible w fragments
        TextView value_text = getView().findViewById(R.id.value_text);
        TextView change1H_text = getView().findViewById(R.id.change1H_text);
        TextView change24H_text = getView().findViewById(R.id.change24H_text);
        TextView change7D_text = getView().findViewById(R.id.change7D_text);
        TextView marketcap_text = getView().findViewById(R.id.marketcap_text);
        TextView volume24H_text = getView().findViewById(R.id.volume24H_text);

        TextView name_text = getView().findViewById(R.id.name_text);
        TextView symbol_text = getView().findViewById(R.id.symbol_text);

        Coin myCoin = MainActivity.coinSearch(message);

        name_text.setText(myCoin.getName());
        symbol_text.setText(myCoin.getSymbol());

        value_text.setText(myCoin.getPriceUsd());
        change1H_text.setText(myCoin.getPercentChange1h());
        change24H_text.setText(myCoin.getPercentChange24h());
        change7D_text.setText(myCoin.getPercentChange7d());
        marketcap_text.setText(myCoin.getMarketCapUsd());
        volume24H_text.setText(Double.toString(myCoin.getVolume24()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }


}

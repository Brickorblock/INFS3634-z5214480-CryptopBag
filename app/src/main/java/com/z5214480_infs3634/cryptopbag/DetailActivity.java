package com.z5214480_infs3634.cryptopbag;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.detail_activity);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.KEY);

        TextView value_text = findViewById(R.id.value_text);
        TextView change1H_text = findViewById(R.id.change1H_text);
        TextView change24H_text = findViewById(R.id.change24H_text);
        TextView change7D_text = findViewById(R.id.change7D_text);
        TextView marketcap_text = findViewById(R.id.marketcap_text);
        TextView volume24H_text = findViewById(R.id.volume24H_text);

        TextView name_text = findViewById(R.id.name_text);
        TextView symbol_text = findViewById(R.id.symbol_text);

        Coin myCoin = new Coin();
        myCoin = myCoin.coinSearch(message);

        name_text.setText(myCoin.getName());
        symbol_text.setText(myCoin.getSymbol());

        value_text.setText(Double.toString(myCoin.getValue()));
        change1H_text.setText(Double.toString(myCoin.getChange1h()));
        change24H_text.setText(Double.toString(myCoin.getChange24h()));
        change7D_text.setText(Double.toString(myCoin.getChange7d()));
        marketcap_text.setText(Double.toString(myCoin.getMarketcap()));
        volume24H_text.setText(Double.toString(myCoin.getVolume()));

    }


}

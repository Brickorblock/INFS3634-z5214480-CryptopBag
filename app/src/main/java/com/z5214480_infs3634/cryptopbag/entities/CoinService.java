package com.z5214480_infs3634.cryptopbag.entities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinService {
    @GET("tickers")
    Call<CoinLoreResponse> get100Coins();
}

package com.z5214480_infs3634.cryptopbag.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoinDao {
    @Query("DELETE FROM coin")
    void deleteAllCoins();

    @Insert()
    void insertCoins(Coin... coins);

    @Query("SELECT * FROM coin")
    List<Coin> getCoins();

    @Query("SELECT * FROM coin WHERE id = :id")
    Coin getCoinById(String id);

}

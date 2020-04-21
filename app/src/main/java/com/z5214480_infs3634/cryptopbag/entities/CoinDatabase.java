package com.z5214480_infs3634.cryptopbag.entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Coin.class, version = 1)
public abstract class CoinDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();

}

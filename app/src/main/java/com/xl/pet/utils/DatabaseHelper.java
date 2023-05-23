package com.xl.pet.utils;

import android.content.Context;

import androidx.room.Room;

import com.xl.pet.database.AppDatabase;
import com.xl.pet.database.dao.MenstruationDao;

public class DatabaseHelper {

    public static AppDatabase appDatabase;

    public static void createDatabase(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "xl-pet").build();
    }

    public static MenstruationDao menstruationDao() {
        return appDatabase.menstruationDao();
    }

}

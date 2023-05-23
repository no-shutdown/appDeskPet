package com.xl.pet.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xl.pet.database.dao.MenstruationDao;
import com.xl.pet.database.entity.MenstruationDO;

@Database(entities = {MenstruationDO.class}, version = 1)
public abstract  class AppDatabase extends RoomDatabase {

    public abstract MenstruationDao menstruationDao();

}

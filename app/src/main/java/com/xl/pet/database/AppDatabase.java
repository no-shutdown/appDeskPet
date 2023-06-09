package com.xl.pet.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xl.pet.database.dao.ForestDao;
import com.xl.pet.database.dao.MenstruationDao;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.database.entity.MenstruationDO;

@Database(entities = {MenstruationDO.class, ForestDO.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MenstruationDao menstruationDao();

    public abstract ForestDao forestDao();

}

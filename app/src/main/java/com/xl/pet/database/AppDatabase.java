package com.xl.pet.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xl.pet.database.dao.ForestDao;
import com.xl.pet.database.dao.ForestFlagDao;
import com.xl.pet.database.dao.LastForestDao;
import com.xl.pet.database.dao.MenstruationDao;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.database.entity.ForestFlagDO;
import com.xl.pet.database.entity.LastForestDO;
import com.xl.pet.database.entity.MenstruationDO;

@Database(entities = {MenstruationDO.class, ForestDO.class, ForestFlagDO.class, LastForestDO.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MenstruationDao menstruationDao();

    public abstract ForestDao forestDao();

    public abstract ForestFlagDao forestFlagDao();

    public abstract LastForestDao lastForestDao();
}

package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.LastForestDO;

@Dao
public interface LastForestDao {

    @Query("SELECT * FROM last_forest LIMIT 1")
    LastForestDO findFirst();

    @Query("DELETE FROM last_forest")
    void del();

    @Insert
    void insert(LastForestDO lastForestDO);

}

package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.ForestFlagDO;

import java.util.List;

@Dao
public interface ForestFlagDao {
    @Query("SELECT * FROM forest_flag")
    List<ForestFlagDO> findAll();

    @Query("SELECT * FROM forest_flag LIMIT 1")
    ForestFlagDO findFirst();

    @Insert
    void insert(ForestFlagDO forest);
}

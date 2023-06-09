package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.ForestDO;

import java.util.List;

@Dao
public interface ForestDao {

    @Query("SELECT * FROM forest WHERE startTime >= :start AND startTime <= :end")
    List<ForestDO> findByRange(long start, long end);

    @Insert
    void insertEntities(List<ForestDO> forestList);

}

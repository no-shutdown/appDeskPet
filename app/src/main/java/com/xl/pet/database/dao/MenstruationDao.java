package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.MenstruationDO;

import java.util.List;

@Dao
public interface MenstruationDao {
    @Query("SELECT * FROM menstruation ORDER BY id ASC")
    List<MenstruationDO> findAll();

    @Query("SELECT * FROM menstruation WHERE id = :id")
    MenstruationDO find(int id);

    @Insert
    void insert(MenstruationDO menstruationDO);

    @Query("DELETE FROM menstruation WHERE id = :id")
    void delete(int id);

    @Insert
    void insertEntities(List<MenstruationDO> menstruationList);

    @Delete
    void deleteEntities(List<MenstruationDO> menstruationList);

}

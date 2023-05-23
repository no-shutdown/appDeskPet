package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.MenstruationDO;

import java.util.List;

@Dao
public interface MenstruationDao {
    @Query("SELECT * FROM menstruation")
    List<MenstruationDO> findAll();

    @Query("SELECT * FROM menstruation WHERE id = :id")
    MenstruationDO findById(int id);

    @Query("DELETE FROM menstruation WHERE year = :year AND month = :month AND day = :day")
    void deleteTag(int year, int month, int day);

    @Insert
    void insert(MenstruationDO menstruationDO);

    @Insert
    void insertList(List<MenstruationDO> menstruationList);

}

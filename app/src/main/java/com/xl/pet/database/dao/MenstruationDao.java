package com.xl.pet.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xl.pet.database.entity.MenstruationDO;

import java.util.List;

@Dao
public interface MenstruationDao {
    @Query("SELECT * FROM menstruation ORDER BY year,month,day ASC")
    List<MenstruationDO> findAll();

    @Query("SELECT * FROM menstruation WHERE id = :id")
    MenstruationDO find(int id);

    @Query("DELETE FROM menstruation WHERE year = :year AND month = :month AND day = :day")
    void delete(int year, int month, int day);

    @Query("DELETE FROM menstruation WHERE id = :id")
    void delete(int id);

    @Insert
    void insert(MenstruationDO menstruationDO);

    @Insert
    void insertList(List<MenstruationDO> menstruationList);

}

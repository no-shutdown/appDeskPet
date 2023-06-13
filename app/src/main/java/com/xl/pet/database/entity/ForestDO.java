package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forest")
public class ForestDO {
    @PrimaryKey
    public int id;

    public long startTime;
    public long endTime;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int week;
    public Integer resId;

}

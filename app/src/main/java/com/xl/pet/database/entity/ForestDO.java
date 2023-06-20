package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forest")
public class ForestDO {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long startTime;
    public long endTime;
    public Integer resId;
    public String flag;

}

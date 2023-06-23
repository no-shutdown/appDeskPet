package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "last_forest")
public class LastForestDO {
    @PrimaryKey
    public int id;
    public long startTime;
    public long baseTime;
    public int flagId;
    public int resPosition;
}

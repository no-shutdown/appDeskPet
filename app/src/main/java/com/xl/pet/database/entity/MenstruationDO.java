package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menstruation")
public class MenstruationDO {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int year;
    public int month;
    public int day;
}

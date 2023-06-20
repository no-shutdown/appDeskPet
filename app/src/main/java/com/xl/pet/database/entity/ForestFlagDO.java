package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forest_flag")
public class ForestFlagDO {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String flag;

    public ForestFlagDO(String flag) {
        this.flag = flag;
    }
}

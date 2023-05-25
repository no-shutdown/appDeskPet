package com.xl.pet.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menstruation")
public class MenstruationDO {
    @PrimaryKey
    public int id;

    public int year;

    public int month;

    public int day;

    public MenstruationDO(int id) {
        this.id = id;
        String idStr = String.valueOf(id);
        year = Integer.parseInt(idStr.substring(0, 4));
        month = Integer.parseInt(idStr.substring(4, 6));
        day = Integer.parseInt(idStr.substring(6, 8));
    }

}

package com.freshliver.ashistant.models.db.setting;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AppSettings")
public class AppSetting {
    @PrimaryKey
    @NonNull
    public String key;
    public String value;


    public AppSetting(@NonNull String key, String value) {
        this.key = key;
        this.value = value;
    }
}

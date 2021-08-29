package com.freshliver.ashistant.models.db.setting;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AppSettingDao {

    @Query("SELECT * FROM AppSettings WHERE `key` is :key")
    AppSetting getAppSettingByKey(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setAppSetting(AppSetting appSetting);
}

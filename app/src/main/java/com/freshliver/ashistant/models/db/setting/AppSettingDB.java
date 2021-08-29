package com.freshliver.ashistant.models.db.setting;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AppSetting.class}, version = 1)
public abstract class AppSettingDB extends RoomDatabase {

    protected abstract AppSettingDao getAppSettingDao();


    public AppSetting getAppSettingByKey(String key) {
        return this.getAppSettingDao().getAppSettingByKey(key);
    }


    public void setAppSetting(AppSetting appSetting) {
        this.getAppSettingDao().setAppSetting(appSetting);
    }

}

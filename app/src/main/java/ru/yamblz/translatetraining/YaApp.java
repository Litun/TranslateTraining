package ru.yamblz.translatetraining;

import android.app.Application;

/**
 * Created by Litun on 23.07.2016.
 */

public class YaApp extends Application {

    private static DataManager dataManager;

    public static DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDataManager();

    }

    private void initDataManager() {
        dataManager = new DataManager(this);
        dataManager.setWordsJson(getResources().openRawResource(R.raw.words));
        dataManager.loadData();
    }
}

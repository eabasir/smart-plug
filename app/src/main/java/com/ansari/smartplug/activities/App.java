package com.ansari.smartplug.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.animation.Animation;



public class App extends android.app.Application {


    private static App instance;

    public static String PACKAGE_NAME;
    public static Activity CurrentActivity;
    public static Context context;
    public static PackageManager packageManager;
    public static final Handler HANDLER = new Handler();

    private Map<Class<?>, Collection<?>> uiListeners;

    private final Handler handler;


    public static App getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public App ()
    {
        instance = this;
        uiListeners = new HashMap<>();
        handler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            context = getApplicationContext();

            try {
                PACKAGE_NAME = context.getPackageName();
                packageManager = getPackageManager();

            } catch (Exception e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Register new listener.
     * <p/>
     * Should be called from {@link Activity#onResume()}.
     */
    public <T> void addUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }
    public <T> Collection<T> getUIListeners(Class<T> cls) {
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    @SuppressWarnings("unchecked")
    private <T> Collection<T> getOrCreateUIListeners(Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    /**
     * Unregister listener.
     * <p/>
     * Should be called from {@link Activity#onPause()}.
     */
    public <T> void removeUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }
}
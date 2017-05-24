package hackerman.notebookmushroom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPreferences {
    private String APP_SHARED_PREFS = getClass().getName();
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public AppPreferences(Context context) {
        this(context, null);
    }

    public AppPreferences(Context context, String prefName) {
        try {
            if (prefName != null) {
                APP_SHARED_PREFS = prefName;
            }
            this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
            this.prefsEditor = appSharedPrefs.edit();
        } catch (NullPointerException e) {
            Log.d("NullPointerException", " context = " + context.getClass().getName());
            e.printStackTrace();
        }

    }

    public void setSharedPrefs(SharedPreferences sharedPrefs) {
        this.appSharedPrefs = sharedPrefs;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return appSharedPrefs.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return appSharedPrefs.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return appSharedPrefs.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return appSharedPrefs.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return appSharedPrefs.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void putInt(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void putFloat(String key, float value) {
        prefsEditor.putFloat(key, value);
        prefsEditor.commit();
    }

    public void putLong(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public String putString(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
        return key;
    }

    public void remove(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public boolean contains(String key) {
        return appSharedPrefs.contains(key);
    }

}
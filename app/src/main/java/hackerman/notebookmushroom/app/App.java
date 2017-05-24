package hackerman.notebookmushroom.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.gfx.android.orma.AccessThreadConstraint;

import hackerman.notebookmushroom.db.OrmaDatabase;
import hackerman.notebookmushroom.utils.AppPreferences;


public class App extends Application {
    private static OrmaDatabase ORMA;
    private static AppPreferences preferences;
private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        preferences = new AppPreferences(getApplicationContext());
        ORMA = OrmaDatabase.builder(getApplicationContext())
                .trace(true)
                .readOnMainThread(AccessThreadConstraint.WARNING)
                .writeOnMainThread(AccessThreadConstraint.WARNING)
                .build();
        MultiDex.install(this);
    }

    public static OrmaDatabase orma() {
        return ORMA;
    }

    public static AppPreferences getAppPreferences() {
        return preferences;
    }

    public static Context getAppContext() {
        return context;
    }
}

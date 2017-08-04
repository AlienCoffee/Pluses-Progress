package tk.pluses.plusesprogress.user;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 04.08.2017.
 */

public class UserEntity {

    public static final String CONFIG_FILE = "user.dat";
    private static SharedPreferences preferences = null;

    public static void setPreferences (SharedPreferences preferences) {
        if (UserEntity.preferences == null) { UserEntity.preferences = preferences; }
    }

    public static SharedPreferences getPreferences () {
        return preferences;
    }

    private static Map <String, String> data;

    static {
        data = new HashMap <> ();
        _restoreDefault ();
    }

    private static void _restoreDefault () {
        data.clear ();
        data.put ("token", "def");
    }

    public static String getProperty (String key) {
        if (key == null || key.length () == 0) {
            throw new IllegalArgumentException ("String `key` was expected");
        }

        return data.get (key);
    }

    public static void setProperty (String key, String value) {
        if (key == null || key.length () == 0) {
            throw new IllegalArgumentException ("String `key` was expected");
        }

        data.put (key, value);
    }

}

package tws.foodforlife.app.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class LangSharedPref {
    public static final String LANG_BANGLA = "bn";
    public static final String LANG_ENGLISH = "en";

    public static final String DB_LANG = "db_lang";
    public static final String CURRENT_LANG = "lang";

    private SharedPreferences langPreferences;

    public LangSharedPref(Context context) {
        langPreferences = context.getSharedPreferences(DB_LANG, Context.MODE_PRIVATE);
    }

    public void setLang(String lang) {
        SharedPreferences.Editor editor = langPreferences.edit();
        editor.putString(CURRENT_LANG, lang);
        editor.apply();
    }

    public void clearLang() {
        if (langPreferences.contains(CURRENT_LANG)) {
            SharedPreferences.Editor editor = langPreferences.edit();
            editor.remove(CURRENT_LANG);
            editor.apply();
        }
    }

    public String getLang() {
        if (langPreferences.contains(CURRENT_LANG)) {
            return langPreferences.getString(CURRENT_LANG, LANG_ENGLISH);
        }
        return LANG_ENGLISH;
    }

}

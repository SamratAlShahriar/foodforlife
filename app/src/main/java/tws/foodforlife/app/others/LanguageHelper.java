package tws.foodforlife.app.others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import tws.foodforlife.app.R;
import tws.foodforlife.app.sharedpref.LangSharedPref;

public class LanguageHelper {
    private LangSharedPref langSharedPref;

    public void reloadLang(Activity activity) {
        langSharedPref = new LangSharedPref(activity.getApplicationContext());
        String lang = langSharedPref.getLang();
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        activity.setTheme(R.style.NoAnimTheme);
        Intent refresh = new Intent(activity, activity.getClass());
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //activity.overridePendingTransition(0, 0);
        activity.startActivity(refresh);

    }


    public void loadLanguage(Context context) {
        String lang = new LangSharedPref(context).getLang();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}

package tws.foodforlife.app.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tws.foodforlife.app.R;
import tws.foodforlife.app.others.LanguageHelper;
import tws.foodforlife.app.sharedpref.LangSharedPref;

public class HomeActivity extends AppCompatActivity {

    private LangSharedPref langSharedPref;
    private LanguageHelper languageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        languageHelper = new LanguageHelper();
        languageHelper.loadLanguage(getBaseContext());
    }
}
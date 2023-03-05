package tws.foodforlife.app.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tws.foodforlife.app.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fr_auth, new GetStartedFragment()).commit();
    }
}
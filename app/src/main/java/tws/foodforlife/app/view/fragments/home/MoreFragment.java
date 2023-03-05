package tws.foodforlife.app.view.fragments.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.others.LanguageHelper;
import tws.foodforlife.app.sharedpref.LangSharedPref;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.view.AuthActivity;


public class MoreFragment extends Fragment implements View.OnClickListener {
    private Button btnProfile, btnActivity, btnSettings, btnAbout, btnContact, btnDonateTo, btnLogout;
    private TextView tvUserName, tvUserType;

    private NavController navController;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private UserSharedPref userSharedPref;

    private LangSharedPref langSharedPref;
    private LanguageHelper languageHelper;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userSharedPref = new UserSharedPref(getContext());
        langSharedPref = new LangSharedPref(requireContext());
        languageHelper = new LanguageHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        btnProfile = view.findViewById(R.id.fm_btn_profile);
        btnActivity = view.findViewById(R.id.fm_btn_activity);
        btnSettings = view.findViewById(R.id.fm_btn_settings);
        btnAbout = view.findViewById(R.id.fm_btn_about);
        btnContact = view.findViewById(R.id.fm_btn_contact);
        btnDonateTo = view.findViewById(R.id.fm_btn_donate_to);
        btnLogout = view.findViewById(R.id.fm_btn_logout);
        tvUserName = view.findViewById(R.id.fm_tv_user_name);
        tvUserType = view.findViewById(R.id.fm_tv_user_type);

        btnProfile.setOnClickListener(this::onClick);
        btnActivity.setOnClickListener(this::onClick);
        btnSettings.setOnClickListener(this::onClick);
        btnAbout.setOnClickListener(this::onClick);
        btnContact.setOnClickListener(this::onClick);
        btnDonateTo.setOnClickListener(this::onClick);
        btnLogout.setOnClickListener(this::onClick);

        String userName = userSharedPref.getU_NAME();
        String userType = userSharedPref.getU_TYPE();

        if (userName != null || !userName.isEmpty()) {
            tvUserName.setText(userName);
        }


        if (userType != null || !userType.isEmpty()) {
            tvUserType.setText(userType);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_btn_profile:
                if(userSharedPref.getU_TYPE().equals(USERCONS.U_TYPE_DONOR)) {
                    navController.navigate(R.id.action_moreFragment_to_profileFragment);
                } else {
                    navController.navigate(R.id.action_moreFragment_to_profileOrgFragment);
                }
                break;

            case R.id.fm_btn_activity:
                navController.navigate(R.id.action_moreFragment_to_yourActivityFragment);
                break;

            case R.id.fm_btn_settings:
                CharSequence[] languages = new CharSequence[]{
                        getString(R.string.english),
                        getString(R.string.bangla)};

                String currLang = langSharedPref.getLang();
                int checkedLang;
                if (currLang.equals(LangSharedPref.LANG_BANGLA)) {
                    checkedLang = 1;
                } else {
                    checkedLang = 0;
                }
                MaterialAlertDialogBuilder builder =
                        new MaterialAlertDialogBuilder(requireContext());
                builder.setTitle(getString(R.string.select_lang))
                        .setSingleChoiceItems(languages, checkedLang, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                switch (which) {
                                    case 0:
                                        langSharedPref.setLang(LangSharedPref.LANG_ENGLISH);

                                        break;
                                    case 1:
                                        langSharedPref.setLang(LangSharedPref.LANG_BANGLA);
                                        break;
                                }
                                dialogInterface.dismiss();
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        String afterLang = langSharedPref.getLang();
                        if(!currLang.equals(afterLang)){
                            languageHelper.reloadLang(requireActivity());
                        }
                        Log.d("lang", "onClick: " + langSharedPref.getLang());
                    }
                }).create().show();


                break;

            case R.id.fm_btn_about:
                navController.navigate(R.id.action_moreFragment_to_aboutFragment);
                break;

            case R.id.fm_btn_contact:
                navController.navigate(R.id.action_moreFragment_to_contactFragment);
                break;

            case R.id.fm_btn_donate_to:
                navController.navigate(R.id.action_moreFragment_to_donateToFflFragment);
                break;

            case R.id.fm_btn_logout:
                firebaseAuth.signOut();
                userSharedPref.clearAll();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
                break;
        }
    }
}
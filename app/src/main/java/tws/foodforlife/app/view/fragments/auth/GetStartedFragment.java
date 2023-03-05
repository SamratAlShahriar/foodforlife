package tws.foodforlife.app.view.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.SliderView;

import tws.foodforlife.app.R;
import tws.foodforlife.app.adapters.ImageSliderAdapter;
import tws.foodforlife.app.others.LanguageHelper;
import tws.foodforlife.app.sharedpref.LangSharedPref;


public class GetStartedFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "GetStartedFragment";
    private static boolean launchFirst = true;
    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private TextView tvBn, tvEn;
    private Switch swLang;

    private LangSharedPref langSharedPref;
    private LanguageHelper languageHelper;

    private Button btnGetStarted;

    public GetStartedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageHelper = new LanguageHelper();
        langSharedPref = new LangSharedPref(requireContext());
        languageHelper.loadLanguage(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_started, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //slide image
        sliderImage(view);

        swLang = view.findViewById(R.id.fr_gs_sw_lang);
        tvBn = view.findViewById(R.id.fr_gs_tv_bn);
        tvEn = view.findViewById(R.id.fr_gs_tv_en);

        if (langSharedPref.getLang().equals(LangSharedPref.LANG_ENGLISH)) {
            swLang.setChecked(false);
            tvEn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tvBn.setTextColor(getResources().getColor(R.color.black));
        } else {
            swLang.setChecked(true);
            tvBn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tvEn.setTextColor(getResources().getColor(R.color.black));
        }

        swLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                if (status) {
                    langSharedPref.setLang(LangSharedPref.LANG_BANGLA);
                    languageHelper.reloadLang(requireActivity());
                } else {
                    langSharedPref.setLang(LangSharedPref.LANG_ENGLISH);
                    languageHelper.reloadLang(requireActivity());
                }
            }
        });

        navController = Navigation.findNavController(view);
        btnGetStarted = view.findViewById(R.id.fr_gs_btn_get_started);
        btnGetStarted.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view != null) {
            if (view.getId() == R.id.fr_gs_btn_get_started) {
                navController.navigate(R.id.action_getStartedFragment_to_loginEmailFragment);
            }
        }

    }

    private void sliderImage(View view) {
        int[] images = {R.drawable.fd1, R.drawable.fd3, R.drawable.fd4};
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(images);
        SliderView sliderView = view.findViewById(R.id.sv_slider);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        sliderView.setSliderAdapter(sliderAdapter);
    }


}
package tws.foodforlife.app.view.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tws.foodforlife.app.R;


public class SignupVerificationFragment extends Fragment {
    private static String TAG = "SignupVerificationFragment";

    private NavController navController;

    private Button btnGotoLogin;



    public SignupVerificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        btnGotoLogin = view.findViewById(R.id.fsvl_btn_create_acc);
        String tempMail = "";
        if (getArguments() != null) {
            SignupVerificationFragmentArgs args = SignupVerificationFragmentArgs.fromBundle(getArguments());
            tempMail = args.getEmail();
            Log.d(TAG, tempMail);
        }

        final String email = tempMail;
        btnGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupVerificationFragmentDirections.ActionSignupVerificationFragmentToLoginEmailFragment action
                        = SignupVerificationFragmentDirections.actionSignupVerificationFragmentToLoginEmailFragment();
                action.setEmail(email);
                navController.navigate(action);

            }
        });
    }
}
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
import android.widget.TextView;

import tws.foodforlife.app.R;

public class SignupNoAccFragment extends Fragment {
    private static final String TAG = "SignupNoAccFragment";
    private NavController navController;

    private Button btnCreateAcc;
    private TextView tvTextWarn;

    private String email = "";


    public SignupNoAccFragment() {
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
        return inflater.inflate(R.layout.fragment_signup_no_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnCreateAcc = view.findViewById(R.id.fsna_btn_create_acc);
        tvTextWarn = view.findViewById(R.id.fsna_tv_show_warn);

        if (getArguments() != null) {
            SignupNoAccFragmentArgs args = SignupNoAccFragmentArgs.fromBundle(getArguments());
            email = args.getEmail();
            Log.d(TAG, email);
        }

        tvTextWarn.setText("No account associated with the email \n" + email);

        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupNoAccFragmentDirections.ActionSignupNoAccFragmentToSignupInfoFragment action
                        = SignupNoAccFragmentDirections.actionSignupNoAccFragmentToSignupInfoFragment();
                action.setEmail(email);
                navController.navigate(action);
            }
        });

    }
}
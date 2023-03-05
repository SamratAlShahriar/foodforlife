package tws.foodforlife.app.view.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.SignInMethodQueryResult;

import tws.foodforlife.app.R;
import tws.foodforlife.app.network.NetworkUtils;
import tws.foodforlife.app.repository.firebase.UserRepository;


public class LoginEmailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginEmailFragment";

    private Activity activity;
    private Context context;

    private NavController navController;

    private UserRepository userRepository;

    private EditText etEmail;
    private Button btnNext;
    private ProgressBar progressBar;

    public LoginEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        activity = getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        etEmail = view.findViewById(R.id.fle_login_et_email);
        btnNext = view.findViewById(R.id.fle_btn_next);
        btnNext.setOnClickListener(this);

        //custom progress bar
        progressBar = view.findViewById(R.id.fle_skv_progress_bar);


        //Sprite sprite = new WanderingCubes();
        //progressBar.setIndeterminateDrawable(sprite);

        if (getArguments() != null) {
            SignupNoAccFragmentArgs args = SignupNoAccFragmentArgs.fromBundle(getArguments());
            String email = args.getEmail();
            if (email != null) {
                etEmail.setText(email);
            }
            Log.d(TAG, email);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fle_btn_next:
                // hide keyboard layout
                hideKeyboardLayout();

                if (NetworkUtils.isConnected(context)) {
                    String email = etEmail.getText().toString().trim();

                    if (isEmailValid(email)) {
                        fetchFirebase(email);
                    } else {
                        etEmail.setError("Invalid Email Address!");
                    }
                } else {
                    Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void fetchFirebase(String email) {
        progressBar.setVisibility(View.VISIBLE);

        //check email in firebase authentication
        userRepository.fetchSigninMethodForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean haveAccount = !task.getResult().getSignInMethods().isEmpty();

                if (haveAccount) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "have acc");
                    LoginEmailFragmentDirections.ActionLoginEmailFragmentToLoginPasswordFragment action
                            = LoginEmailFragmentDirections.actionLoginEmailFragmentToLoginPasswordFragment();
                    action.setEmail(email);
                    navController.navigate(action);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "no acc");
                    LoginEmailFragmentDirections.ActionLoginEmailFragmentToSignupNoAccFragment action
                            = LoginEmailFragmentDirections.actionLoginEmailFragmentToSignupNoAccFragment();
                    action.setEmail(email);
                    navController.navigate(action);
                }

            }
        });
    }

    private boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        return false;
    }

    private void hideKeyboardLayout() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}

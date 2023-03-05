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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.model.User;
import tws.foodforlife.app.network.NetworkUtils;
import tws.foodforlife.app.repository.firebase.UserRepository;


public class SignupInfoFragment extends Fragment {
    private static String TAG = "SignupInfoFragment";
    private Activity activity;
    private Context context;

    private NavController navController;

    private UserRepository userRepository;


    private EditText etEmail, etPass, etRepPass;
    private TextView tvTerms;
    private CheckBox cbAsORG, cbAcTerms;
    private Button btnSignup;

    private ProgressBar progressBar;


    public SignupInfoFragment() {
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
        return inflater.inflate(R.layout.fragment_signup_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        etEmail = view.findViewById(R.id.fsep_et_email);
        etPass = view.findViewById(R.id.fsep_et_pass);
        etRepPass = view.findViewById(R.id.fsep_et_rep_pass);
        btnSignup = view.findViewById(R.id.fsep_btn_submit);
        cbAsORG = view.findViewById(R.id.fsi_cb_signup_org);
        cbAcTerms = view.findViewById(R.id.fsi_cb_signup_terms);
        tvTerms = view.findViewById(R.id.fsi_tv_terms);

        //custom progress bar
        progressBar = view.findViewById(R.id.sif_skv_progress_bar);

        String email = "";
        if (getArguments() != null) {
            SignupInfoFragmentArgs args = SignupInfoFragmentArgs.fromBundle(getArguments());
            email = args.getEmail();
            etEmail.setText(email);
            Log.d(TAG, email);
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardLayout();

                String email = etEmail.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String repPass = etRepPass.getText().toString().trim();

                String userType = USERCONS.U_TYPE_DONOR;
                if (cbAsORG.isChecked()) {
                    userType = USERCONS.U_TYPE_ORGANIZATION;
                }

                if (!isPasswordValid(email)) {
                    etEmail.setError("Invalid Email Address!");
                    return;
                }

                if (!isPasswordValid(pass)) {
                    etPass.setError("Password Length Must Be 6 or More!");
                    return;
                }

                if (!pass.equals(repPass)) {
                    etRepPass.setError("Not Matched with Password!");
                    return;
                }

                if (cbAcTerms.isChecked()) {
                    if (NetworkUtils.isConnected(context)) {
                        fetchFirebase(email, pass, userType);
                    } else {
                        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Accept Terms and Conditions!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchFirebase(String email, String pass, String userType) {
        progressBar.setVisibility(View.VISIBLE);

        userRepository.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String uid = userRepository.getCurrentUser().getUid();
                            String email = userRepository.getCurrentUser().getEmail();

                            User user = new User();
                            user.setEmail(email);
                            user.setUid(uid);
                            user.setUserType(userType);
                            user.setEmailVerified(false);

                            //save info to firestore
                            userRepository.setUserDetails(uid, user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        sentVerification(email);
                                    } else {
                                        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sentVerification(String email) {
        userRepository.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        userRepository.signout();
                        SignupInfoFragmentDirections.ActionSignupInfoFragmentToSignupVerificationFragment action =
                                SignupInfoFragmentDirections.actionSignupInfoFragmentToSignupVerificationFragment();
                        action.setEmail(email);
                        navController.navigate(
                                action);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userRepository.signout();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        userRepository.signout();
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void hideKeyboardLayout() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
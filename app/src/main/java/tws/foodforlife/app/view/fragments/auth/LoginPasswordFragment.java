package tws.foodforlife.app.view.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.model.User;
import tws.foodforlife.app.network.NetworkUtils;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.view.HomeActivity;

public class LoginPasswordFragment extends Fragment {
    private final static String TAG = "LoginPasswordFragment";

    private Activity activity;
    private Context context;

    private NavController navController;

    private EditText etPassword;
    private Button btnLogin;

    private ProgressBar progressBar;

    private UserRepository userRepository;

    private String userType;

    private UserSharedPref userSharedPref;



    public LoginPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        activity = getActivity();
        context = getContext();

        userSharedPref = new UserSharedPref(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        etPassword = view.findViewById(R.id.flp_et_password);
        btnLogin = view.findViewById(R.id.flp_btn_login);

        progressBar = view.findViewById(R.id.flp_skv_progress_bar);

        String tempMail = "";
        if (getArguments() != null) {
            LoginPasswordFragmentArgs args = LoginPasswordFragmentArgs.fromBundle(getArguments());
            tempMail = args.getEmail();
            Log.d(TAG, tempMail);
        }

        final String email = tempMail;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardLayout();

                String pass = etPassword.getText().toString().trim();
                if (NetworkUtils.isConnected(context)) {
                    if (isPasswordValid(pass)) {
                        fetchFirebase(email, pass);
                    } else {
                        etPassword.setError("Password Length Must Be 6 or More!");
                    }
                } else {
                    Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void fetchFirebase(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);

        userRepository.signInWithEmailAndPass(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (userRepository.getCurrentUser().isEmailVerified()) {

                                //check for the first time of a user
                                userRepository.getUserDetails(userRepository.getCurrentUser().getUid())
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            if (snapshot.exists()) {
                                                User user = snapshot.toObject(User.class);
                                                if (user.getUserType() != null) {
                                                    userType = user.getUserType();
                                                }

                                                if (user.getName() == null || user.getName().isEmpty()) {
                                                    Log.d("UNAME", "NULL");

                                                    if (userType != null || !userType.isEmpty()) {
                                                        if (userType.equals(USERCONS.U_TYPE_DONOR)) {
                                                            navController.navigate(R.id.action_loginPasswordFragment_to_userInfoFirstTimeFragment);
                                                        } else if (userType.equals(USERCONS.U_TYPE_ORGANIZATION)) {
                                                            navController.navigate(R.id.action_loginPasswordFragment_to_orgInfoFirstTimeFragment);
                                                        }
                                                        progressBar.setVisibility(View.GONE);
                                                    }

                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    Log.d("UNAME", user.getName());
                                                    userSharedPref.clearAll();
                                                    userSharedPref.setU_NAME(user.getName());
                                                    userSharedPref.setU_ID(user.getUid());
                                                    userSharedPref.setU_EMAIL(user.getEmail());
                                                    userSharedPref.setU_PHONE(user.getPhone());
                                                    userSharedPref.setU_TYPE(user.getUserType());
                                                    userSharedPref.setU_ADDRESS(user.getAddress());


                                                    String imgUrl = user.getUserImageUrl();
                                                    if(imgUrl != null && !imgUrl.isEmpty()){
                                                        userSharedPref.setU_IMAGE_URL(imgUrl);
                                                    }

                                                    
                                                    if(user.getUserType().equals(USERCONS.U_TYPE_DONOR)){
                                                        userSharedPref.setU_DOB(user.getDateOfBirth());
                                                    } else if(user.getUserType().equals(USERCONS.U_TYPE_ORGANIZATION)) {
                                                        userSharedPref.setU_REG_NO(user.getRegistrationNo());
                                                    }

                                                    Intent intent = new Intent(getContext(), HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }

                                            } else {
                                                Log.d("EXIST", "NO DOC");
                                            }
                                        }
                                    }
                                });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "not verified");
                                LoginPasswordFragmentDirections.ActionLoginPasswordFragmentToEmailNotVerifiedFragment action =
                                        LoginPasswordFragmentDirections.actionLoginPasswordFragmentToEmailNotVerifiedFragment();
                                action.setEmail(email);
                                navController.navigate(action);
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Wrong Email/Password", Toast.LENGTH_SHORT).show();
                Log.d("LPF-onFailure", e.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void hideKeyboardLayout() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
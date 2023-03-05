package tws.foodforlife.app.view.fragments.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import tws.foodforlife.app.R;
import tws.foodforlife.app.network.NetworkUtils;
import tws.foodforlife.app.repository.firebase.UserRepository;

public class EmailNotVerifiedFragment extends Fragment {
    private static final String TAG = "EmailNotVerifiedFrag";
    private NavController navController;

    private Button btnVerifyAgain;

    private ProgressBar progressBar;

    //private FirebaseAuth firebaseAuth;
    //private FirebaseUser firebaseUser;

    private UserRepository userRepository;


    public EmailNotVerifiedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_not_verified, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        btnVerifyAgain = view.findViewById(R.id.fenv_btn_send_verify);

        progressBar = view.findViewById(R.id.fenv_skv_progress_bar);

        String tempMail = "";
        if (getArguments() != null) {
            EmailNotVerifiedFragmentArgs args = EmailNotVerifiedFragmentArgs.fromBundle(getArguments());
            tempMail = args.getEmail();
            Log.d(TAG, tempMail);
        }

        final String email = tempMail;
        btnVerifyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRepository.getCurrentUser() != null && !userRepository.getCurrentUser().isEmailVerified() && !email.isEmpty()) {
                    if(NetworkUtils.isConnected(getContext())){
                        fetchFirebase(email);
                    } else {
                        Toast.makeText(getContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void fetchFirebase(String email) {
        progressBar.setVisibility(View.VISIBLE);
        userRepository.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    EmailNotVerifiedFragmentDirections.ActionEmailNotVerifiedFragmentToSignupVerificationFragment action =
                            EmailNotVerifiedFragmentDirections.actionEmailNotVerifiedFragmentToSignupVerificationFragment();
                    action.setEmail(email);
                    navController.navigate(action);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
        userRepository.signout();
    }
}
package tws.foodforlife.app.view.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.view.HomeActivity;

public class OrgInfoFirstTimeFragment extends Fragment {
    private TextInputEditText etOrgName, etOrgPhone, etOrgReg, etOrgAddress;
    private Button btnSave;
    private ProgressBar progressBar;

    private UserRepository userRepository;

    private UserSharedPref userSharedPref;


    public OrgInfoFirstTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        userSharedPref = new UserSharedPref(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_org_info_first_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etOrgName = view.findViewById(R.id.foift_et_org_name);
        etOrgPhone = view.findViewById(R.id.foift_et_org_phone);
        etOrgReg = view.findViewById(R.id.foift_et_org_reg);
        etOrgAddress = view.findViewById(R.id.foift_et_org_address);
        btnSave = view.findViewById(R.id.foift_btn_save);
        progressBar = view.findViewById(R.id.foift_skv_progress_bar);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;

                String name = etOrgName.getText().toString().trim();
                if (name == null || name.isEmpty()) {
                    etOrgName.setError("Name can't be empty!");
                    error = true;
                }

                String phone = etOrgPhone.getText().toString().trim();
                if (phone == null || phone.isEmpty()) {
                    etOrgPhone.setError("Phone no. can't be empty!");
                    error = true;
                } else if (phone.length() < 6) {
                    etOrgPhone.setError("Phone no should be more than 6 digit.");
                    error = true;
                }

                String regNo = etOrgReg.getText().toString().trim();
                if (regNo == null || regNo.isEmpty()) {
                    etOrgReg.setError("Invalid Reg No.");
                    error = true;
                }

                String address = etOrgAddress.getText().toString().trim();
                if (address == null || address.isEmpty()) {
                    etOrgAddress.setError("Adress can't be empty!");
                    error = true;
                }

                if (!error) {
                    progressBar.setVisibility(View.VISIBLE);

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(USERCONS.NAME, name);
                    updateMap.put(USERCONS.PHONE, phone);
                    updateMap.put(USERCONS.IS_EMAIL_VERIFIED, true);
                    updateMap.put(USERCONS.ORG_REG_NO, regNo);
                    updateMap.put(USERCONS.ADDRESS, address);

                    userRepository.updateUserMultiData(userRepository.getCurrentUser().getUid(), updateMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        userSharedPref.clearAll();
                                        userSharedPref.setU_NAME(name);
                                        userSharedPref.setU_ID(userRepository.getCurrentUser().getUid());
                                        userSharedPref.setU_EMAIL(userRepository.getCurrentUser().getEmail());
                                        userSharedPref.setU_PHONE(phone);
                                        userSharedPref.setU_TYPE(USERCONS.U_TYPE_ORGANIZATION);
                                        userSharedPref.setU_REG_NO(regNo);
                                        userSharedPref.setU_ADDRESS(address);

                                        Intent intent = new Intent(getContext(), HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                }

            }
        });

    }
}
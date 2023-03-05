package tws.foodforlife.app.view.fragments.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.view.HomeActivity;

public class UserInfoFirstTimeFragment extends Fragment {
    private TextInputEditText etName, etPhone, etDob, etAddress;
    private Button btnSave;
    private ProgressBar progressBar;

    private UserRepository userRepository;

    private UserSharedPref userSharedPref;


    public UserInfoFirstTimeFragment() {
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
        return inflater.inflate(R.layout.fragment_user_info_first_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.fuift_et_name);
        etPhone = view.findViewById(R.id.fuift_et_phone);
        etDob = view.findViewById(R.id.fuift_et_dob);
        etAddress = view.findViewById(R.id.fuift_et_address);
        btnSave = view.findViewById(R.id.fuift_btn_save);
        progressBar = view.findViewById(R.id.fuift_skv_progress_bar);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getContext(), date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;

                String name = etName.getText().toString().trim();
                if (name == null || name.isEmpty()) {
                    etName.setError("Name can't be empty!");
                    error = true;
                }

                String phone = etPhone.getText().toString().trim();
                if (phone == null || phone.isEmpty()) {
                    etPhone.setError("Phone no. can't be empty!");
                    error = true;
                } else if (phone.length() < 6) {
                    etPhone.setError("Phone no should be more than 6 digit.");
                    error = true;
                }

                String dob = etDob.getText().toString().trim();
                if (dob == null || dob.isEmpty()) {
                    etDob.setError("Invalid Date of birth");
                    error = true;
                }

                String address = etAddress.getText().toString().trim();
                if (address == null || address.isEmpty()) {
                    etAddress.setError("Adress can't be empty!");
                    error = true;
                }

                if (!error) {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(USERCONS.NAME, name);
                    updateMap.put(USERCONS.PHONE, phone);
                    updateMap.put(USERCONS.IS_EMAIL_VERIFIED, true);
                    updateMap.put(USERCONS.DOB, dob);
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
                                        userSharedPref.setU_ADDRESS(address);
                                        userSharedPref.setU_TYPE(USERCONS.U_TYPE_DONOR);
                                        userSharedPref.setU_DOB(dob);

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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar myCalendar = Calendar.getInstance();
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar);
        }

    };

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd-MMM-YYYY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        etDob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
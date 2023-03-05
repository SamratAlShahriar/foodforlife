package tws.foodforlife.app.view.fragments.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import karpuzoglu.enes.com.fastdialog.FastDialog;
import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.others.ImageResizer;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.utils.StoragePermissionUtils;


public class ProfileDonorFragment extends Fragment {
    private static final int RC_CROP_IMAGE = 301;

    private TextInputEditText etName, etPhone, etDob, etAddress;
    private Button btnUpdate;
    private CircleImageView civProImage;

    private UserRepository userRepository;

    private UserSharedPref userSharedPref;

    private FastDialog progressDialog;

    private Uri imageUri;

    public ProfileDonorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        userSharedPref = new UserSharedPref(getContext());

        progressDialog = FastDialog.p(requireContext()).progressText("Please Wait!").create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        civProImage = view.findViewById(R.id.fdp_civ_pro_image);
        etName = view.findViewById(R.id.fdp_et_name);
        etPhone = view.findViewById(R.id.fdp_et_phone);
        etDob = view.findViewById(R.id.fdp_et_dob);
        etAddress = view.findViewById(R.id.fdp_et_address);
        btnUpdate = view.findViewById(R.id.fdp_btn_update);

        Log.d("TAG", "onComplete: " + userSharedPref.getU_ADDRESS());

        populateViewData();

        civProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (StoragePermissionUtils.isPermitted(requireContext())) {
                        pickImage();
                    } else {
                        StoragePermissionUtils.requestPermissions(requireActivity());
                    }
                } else {
                    pickImage();
                }
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getContext(), date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                } else if (phone.length() < 11) {
                    etPhone.setError("Phone no should be more than 11 digit.");
                    error = true;
                }

                String dob = etDob.getText().toString().trim();
                if (dob == null || dob.isEmpty()) {
                    etDob.setError("Invalid Date of birth");
                    error = true;
                }

                String address = etAddress.getText().toString().trim();
                if (address == null || address.isEmpty()) {
                    etAddress.setError("Address can't be empty!");
                    error = true;
                }

                if (!error) {
                    progressDialog.show();
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(USERCONS.NAME, name);
                    updateMap.put(USERCONS.PHONE, phone);
                    updateMap.put(USERCONS.DOB, dob);
                    updateMap.put(USERCONS.ADDRESS, address);


                    userRepository.updateUserMultiData(userRepository.getCurrentUser().getUid(), updateMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        //userSharedPref.clearAll();
                                        userSharedPref.setU_NAME(name);
                                        userSharedPref.setU_ID(userRepository.getCurrentUser().getUid());
                                        userSharedPref.setU_EMAIL(userRepository.getCurrentUser().getEmail());
                                        userSharedPref.setU_PHONE(phone);
                                        userSharedPref.setU_ADDRESS(address);
                                        userSharedPref.setU_TYPE(USERCONS.U_TYPE_DONOR);
                                        userSharedPref.setU_DOB(dob);
                                    } else {
                                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void pickImage() {
        // for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                imageUri = result.getUri();
                civProImage.setImageURI(imageUri);
                uploadImage(imageUri);
                //Log.d("TAG", "onActivityResult: "+imageUri.toString());
            }
        }
    }

    private void uploadImage(Uri mUri) {
        Uri convertedUri = null;
        if (mUri != null) {
            convertedUri = ImageResizer.uriToReducedUri(requireContext(), mUri);
        }

        if (convertedUri != null) {
            userRepository.uploadUserProImage(userRepository.getCurrentUser().getUid(), convertedUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            userRepository.getDownloadUrl(userRepository.getCurrentUser().getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            userRepository.updateUserSingleData(userRepository.getCurrentUser().getUid(),
                                                    USERCONS.USER_IMAGE_URL, url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(requireContext(), "Image Updated Successfully.", Toast.LENGTH_SHORT).show();
                                                    userSharedPref.setU_IMAGE_URL(url);
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Image Upload Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void populateViewData() {
        String name = userSharedPref.getU_NAME();
        etName.setText(name);

        String phone = userSharedPref.getU_PHONE();
        etPhone.setText(phone);

        String dob = userSharedPref.getU_DOB();
        etDob.setText(dob);

        String address = userSharedPref.getU_ADDRESS();
        etAddress.setText(address);

        String imgUrl = userSharedPref.getU_IMAGE_URL();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                Picasso.get().load(imgUrl).placeholder(R.drawable.ic_baseline_person).into(civProImage);
            } catch (Exception e) {

            }

        }
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
}
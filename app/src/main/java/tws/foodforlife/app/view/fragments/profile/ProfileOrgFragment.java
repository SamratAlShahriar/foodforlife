package tws.foodforlife.app.view.fragments.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import karpuzoglu.enes.com.fastdialog.FastDialog;
import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.others.ImageResizer;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.utils.StoragePermissionUtils;


public class ProfileOrgFragment extends Fragment {
    private TextInputEditText etOrgName, etOrgPhone, etOrgReg, etOrgAddress;
    private Button btnUpdate;
    private CircleImageView civProImage;

    private UserRepository userRepository;

    private UserSharedPref userSharedPref;

    private FastDialog progressDialog;

    private Uri imageUri;


    public ProfileOrgFragment() {
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
        return inflater.inflate(R.layout.fragment_org_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        civProImage = view.findViewById(R.id.fop_civ_pro_image);
        etOrgName = view.findViewById(R.id.fop_et_org_name);
        etOrgPhone = view.findViewById(R.id.fop_et_org_phone);
        etOrgReg = view.findViewById(R.id.fop_et_org_reg);
        etOrgAddress = view.findViewById(R.id.fop_et_org_address);
        btnUpdate = view.findViewById(R.id.fop_btn_update);

        populateViewData();

        civProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!StoragePermissionUtils.isPermitted(requireContext())) {
                        StoragePermissionUtils.requestPermissions(requireActivity());
                    } else {
                        pickImage();
                    }
                } else {
                    pickImage();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                } else if (phone.length() < 11) {
                    etOrgPhone.setError("Phone no should be more than 11 digit.");
                    error = true;
                }

                String regNo = etOrgReg.getText().toString().trim();
                if (regNo == null || regNo.isEmpty()) {
                    etOrgReg.setError("Invalid Reg No.");
                    error = true;
                }

                String address = etOrgAddress.getText().toString().trim();
                if (address == null || address.isEmpty()) {
                    etOrgAddress.setError("Address can't be empty!");
                    error = true;
                }

                if (!error) {
                    progressDialog.show();

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(USERCONS.NAME, name);
                    updateMap.put(USERCONS.PHONE, phone);
                    updateMap.put(USERCONS.ORG_REG_NO, regNo);
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
                                        userSharedPref.setU_TYPE(USERCONS.U_TYPE_ORGANIZATION);
                                        userSharedPref.setU_REG_NO(regNo);
                                    } else {
                                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
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


    private void pickImage() {
        // for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    private void populateViewData() {
        String name = userSharedPref.getU_NAME();
        etOrgName.setText(name);

        String phone = userSharedPref.getU_PHONE();
        etOrgPhone.setText(phone);

        String reg = userSharedPref.getU_REG_NO();
        etOrgReg.setText(reg);

        String address = userSharedPref.getU_ADDRESS();
        etOrgAddress.setText(address);

        String imgUrl = userSharedPref.getU_IMAGE_URL();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                Picasso.get().load(imgUrl).placeholder(R.drawable.ic_baseline_person).into(civProImage);
            } catch (Exception e) {

            }

        }
    }
}
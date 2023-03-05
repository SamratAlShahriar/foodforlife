package tws.foodforlife.app.view.fragments.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import karpuzoglu.enes.com.fastdialog.FastDialog;
import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.model.Post;
import tws.foodforlife.app.others.ImageResizer;
import tws.foodforlife.app.repository.firebase.PostRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.utils.LocationPermissionUtils;
import tws.foodforlife.app.utils.StoragePermissionUtils;


public class NewPostFragment extends Fragment implements View.OnClickListener {
    private static final int RC_RES = 1;

    private NavController navController;

    private Button btnClose, btnPost;
    private TextView tvName, tvPhone, tvAddLocation, tvAddPhoto, tvMore, tvPostType;
    private ImageView ivProImage;
    private EditText etPostText;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private PostRepository postRepository;


    private FastDialog progressDialog;

    private UserSharedPref userSharedPref;

    private Uri imageUri;

    private String name;
    private String phoneNum;
    private GeoPoint geoPoint;
    private String locationName;
    private String userType;

    public NewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postRepository = new PostRepository();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userSharedPref = new UserSharedPref(getContext());

        Log.d("new post", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onpause", "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onresue", "onResume: ");
        progressDialog = FastDialog.p(requireContext()).progressText("Please Wait!").cancelable(false).create();

        Bundle bundle = getArguments();
        if (bundle != null) {
            double lat = 0, lan = 0;
            if (bundle.containsKey(SelectLocationFragment.KEY_LOC_NAME)) {
                locationName = bundle.getString(SelectLocationFragment.KEY_LOC_NAME, "Unnamed");
                tvAddLocation.setText(locationName);
            }
            if (bundle.containsKey(SelectLocationFragment.KEY_LOC_LAT)) {
                lat = bundle.getDouble(SelectLocationFragment.KEY_LOC_LAT, 0);
            }
            if (bundle.containsKey(SelectLocationFragment.KEY_LOC_LON)) {
                lan = bundle.getDouble(SelectLocationFragment.KEY_LOC_LON, 0);
            }
            geoPoint = new GeoPoint(lat, lan);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        btnClose = view.findViewById(R.id.fnp_btn_close);
        btnPost = view.findViewById(R.id.fnp_btn_post);
        tvPhone = view.findViewById(R.id.fnp_tv_post_phone_num);
        tvName = view.findViewById(R.id.fnp_tv_profile_name);
        tvAddLocation = view.findViewById(R.id.fnp_tv_post_loc);
        tvAddPhoto = view.findViewById(R.id.fnp_tv_add_post_img);
        tvMore = view.findViewById(R.id.fnp_tv_more);
        tvPostType = view.findViewById(R.id.fnp_tv_post_type);
        ivProImage = view.findViewById(R.id.fnp_iv_profile);
        etPostText = view.findViewById(R.id.fnp_et_post_text);

        btnClose.setOnClickListener(this::onClick);
        btnPost.setOnClickListener(this::onClick);
        tvPhone.setOnClickListener(this::onClick);
        tvAddLocation.setOnClickListener(this::onClick);
        tvAddPhoto.setOnClickListener(this::onClick);
        tvMore.setOnClickListener(this::onClick);

        //to populate view
        populateView();

        etPostText.addTextChangedListener(textWatcher);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence != null && !charSequence.toString().trim().equals("")) {
                btnPost.setEnabled(true);
                btnPost.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnPost.setTextColor(getResources().getColor(R.color.white));
            } else {
                btnPost.setEnabled(false);
                btnPost.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                btnPost.setTextColor(getResources().getColor(R.color.colorGrey));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void populateView() {
        if (userSharedPref.getU_PHONE() != null) {
            phoneNum = userSharedPref.getU_PHONE();
            tvPhone.setText("+88 " + phoneNum + "(default)\nclick to change");
        }

        if (userSharedPref.getU_NAME() != null) {
            name = userSharedPref.getU_NAME();
            tvName.setText(name);
        }

        if (userSharedPref.getU_TYPE() != null) {
            userType = userSharedPref.getU_TYPE();
            if (userType.equals(USERCONS.U_TYPE_DONOR)) {
                tvPostType.setText(R.string.share_post);
            } else {
                tvPostType.setText(R.string.request_post);
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();

            switch (id) {
                case R.id.fnp_btn_close:
                    getActivity().onBackPressed();
                    break;

                case R.id.fnp_btn_post:
                    save();
                    break;

                case R.id.fnp_tv_post_phone_num:
                    changePostNum();
                    break;

                case R.id.fnp_tv_post_loc:
                    if (LocationPermissionUtils.isPermitted(requireContext())) {
                        if (LocationPermissionUtils.isGpsEnabled(requireContext())) {
                            navController.navigate(R.id.action_newPostFragment_to_selectLocationFragment);
                        } else {
                            LocationPermissionUtils.enableGps(requireContext());
                        }
                    } else {
                        LocationPermissionUtils.requestPermissions(getActivity());
                    }

                    break;

                case R.id.fnp_tv_add_post_img:
                    selectImage();
                    break;

            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private void selectImage() {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                imageUri = result.getUri();
                tvAddPhoto.setText("1 Photo Selected (click to change)");
            } else {
                tvAddPhoto.setText("Add Photo");
            }
        }
    }


    private void pickImage() {
        // for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 9)
                .start(getContext(), this);
    }


/*    public Address getAddress(Location loc) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        Address address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }*/

    private void changePostNum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Set up the input
        builder.setTitle(getString(R.string.change_contact_number));
        final EditText input = new EditText(getContext());
        input.setHint(getString(R.string.enter_contact_number));
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tempPhone = input.getText().toString().trim();
                if (tempPhone != null && !tempPhone.isEmpty()) {
                    phoneNum = tempPhone;
                    tvPhone.setText("+88 " + phoneNum + "(click to change)");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void save() {
        String postText = etPostText.getText().toString().trim();

        boolean error = false;
        if (postText == null || postText.isEmpty()) {
            error = true;
        }

        if (phoneNum == null || phoneNum.isEmpty()) {
            error = true;
        }

        if (geoPoint == null) {
            error = true;
        }

        if (imageUri == null) {
            error = true;
        }

        if (locationName == null) {
            error = true;
        }

        String name = userSharedPref.getU_NAME();
        String uid = userSharedPref.getU_ID();
        String imageUrl = userSharedPref.getU_IMAGE_URL();


        if (!error) {
            Post post = new Post();
            post.setMainPost(postText);
            post.setPhoneNo(phoneNum);
            post.setStatus(POSTCONS.P_STATUS_AVAILABLE);
            post.setPostedById(uid);
            post.setPostedByName(name);
            post.setPostedByImageUrl(imageUrl);

            post.setpGeoPoint(geoPoint);
            post.setpLocationName(locationName);

            if (userType.equals(USERCONS.U_TYPE_ORGANIZATION)) {
                post.setpType(POSTCONS.P_TYPE_REQUEST);
            } else {
                post.setpType(POSTCONS.P_TYPE_SHARE);
            }

            addToFirestore(post);
        }
    }

    private void addToFirestore(Post post) {
        progressDialog.show();
        postRepository.addPost(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String postID = documentReference.getId();

                        Uri convertedUri = ImageResizer.uriToReducedUri(getContext(), imageUri);
                        if (convertedUri != null) {
                            postRepository.uploadPostImage(postID, convertedUri)
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            long uploaded = snapshot.getBytesTransferred();
                                            long totalsize = snapshot.getTotalByteCount();
                                            if (totalsize > 0) {
                                                double progress = (100.0 * uploaded) / totalsize;
                                            }
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            postRepository.getDownloadUrl(postID)
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            postRepository.updatePostSingleData(postID, POSTCONS.P_IMAGE_URL, uri.toString())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(getContext(), "Post Shared Successfully!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(requireContext(), "Post Success!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }


}
package tws.foodforlife.app.view.fragments.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.model.Post;
import tws.foodforlife.app.others.ImageColorParser;
import tws.foodforlife.app.repository.firebase.PostRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.utils.MyGsonParser;

public class PostDetailsFragment extends Fragment implements OnMapReadyCallback {
    private TextView tvName, tvTime, tvPostText, tvStatus;
    private ImageView ivBack, ivPostImage;
    private CheckBox cbMark;
    private ImageButton ibCall, ibComment, ibShare;
    private Button btnResponse, btnShowResponse;

    private Post postObject;

    private LinearLayout llLayoutPostDetails;
    private View viewMapClick;

    private PostRepository postRepository;

    private SupportMapFragment supportMapFragment;
    private static Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoPoint geoPoint;
    private GoogleMap mMap;

    private UserSharedPref userSharedPref;


    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        userSharedPref = new UserSharedPref(requireContext());
        postRepository = new PostRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fpd_fr_map_fragment);
        supportMapFragment.getMapAsync(this::onMapReady);


        tvName = view.findViewById(R.id.fpd_tv_user_name);
        tvTime = view.findViewById(R.id.fpd_tv_post_time);
        tvPostText = view.findViewById(R.id.fpd_tv_post_text);
        tvStatus = view.findViewById(R.id.fpd_tv_status);
        ivBack = view.findViewById(R.id.fpd_iv_back);
        btnResponse = view.findViewById(R.id.fpd_btn_response);
        btnShowResponse = view.findViewById(R.id.fpd_btn_show_response);
        ivPostImage = view.findViewById(R.id.fpd_iv_post_image);
        cbMark = view.findViewById(R.id.fpd_cb_mark);
        ibCall = view.findViewById(R.id.fpd_ib_call);
        ibComment = view.findViewById(R.id.fpd_ib_comment);
        ibShare = view.findViewById(R.id.fpd_ib_share);
        llLayoutPostDetails = view.findViewById(R.id.fpd_ll_post_info);
        viewMapClick = view.findViewById(R.id.fpd_view_map_click);

        setupViews();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Response Sent!", Toast.LENGTH_SHORT).show();
                if (postObject.getStatus().equals(POSTCONS.P_STATUS_AVAILABLE)) {
                    if (userSharedPref.getU_TYPE().equals(USERCONS.U_TYPE_DONOR)
                            && postObject.getpType().equals(POSTCONS.P_TYPE_REQUEST)) {

                    } else if (userSharedPref.getU_TYPE().equals(USERCONS.ORG_REG_NO)
                            && postObject.getpType().equals(POSTCONS.P_TYPE_SHARE)) {
                        //postRepository
                    }
                }
            }
        });

        btnShowResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postObject != null) {
                    String phone = postObject.getPhoneNo().trim();
                    Uri uri = Uri.parse("tel:" + phone);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.food_for_life));
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ffl_link));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        cbMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean mark) {
                if (mark) {
                    MaterialAlertDialogBuilder builder
                            = new MaterialAlertDialogBuilder(requireContext());
                    builder.setTitle(R.string.confirmation)
                            .setMessage(R.string.are_you_sure)
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (postObject != null && postObject.getpId() != null && !postObject.getpId().isEmpty()) {
                                        if (userSharedPref.getU_TYPE().equals(USERCONS.U_TYPE_DONOR)) {
                                            if (postObject.getStatus().equals(POSTCONS.P_STATUS_AVAILABLE)) {
                                                postRepository.updatePostSingleData(postObject.getpId(),
                                                        POSTCONS.P_STATUS, POSTCONS.P_STATUS_DONATED)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                cbMark.setEnabled(false);
                                                            }
                                                        });
                                            }
                                        } else {
                                            if (postObject.getStatus().equals(POSTCONS.P_STATUS_DONATED)) {
                                                postRepository.updatePostSingleData(postObject.getpId(),
                                                        POSTCONS.P_STATUS, POSTCONS.P_STATUS_DISTRIBUTED)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                cbMark.setEnabled(false);
                                                            }
                                                        });
                                            }
                                        }

                                    }
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cbMark.setChecked(false);
                        }
                    }).create().show();
                }
            }
        });

        viewMapClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clmaplayout", "onClick: onmapclick");
                if (geoPoint == null) {
                    return;
                }
                double lat = geoPoint.getLatitude();
                double lon = geoPoint.getLongitude();
                String locUri = "geo:0,0?q=" + lat + "," + lon;
                Uri gmmIntentUri = Uri.parse(locUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    private void setupViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(POSTCONS.P_KEY)) {
                String data = bundle.getString(POSTCONS.P_KEY);
                postObject = MyGsonParser.getGsonParser().fromJson(data, Post.class);


                //getcurrent info
                String currentUid = userSharedPref.getU_ID();
                String currentUserType = userSharedPref.getU_TYPE();

                tvName.setText(postObject.getPostedByName());

                SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                String timestamp = sfd.format((postObject.getTimestamp().toDate()));
                tvTime.setText(timestamp);

                tvStatus.setText(postObject.getStatus());

                tvPostText.setText(postObject.getMainPost());

                if (postObject.getpImageUrl() != null) {
                    try {
                        Picasso.get()
                                .load(postObject.getpImageUrl())
                                .resizeDimen(R.dimen.post_img_width, R.dimen.post_img_height)
                                .onlyScaleDown().into(ivPostImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //start of color swatch
                if (ivPostImage != null) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPostImage.getDrawable();
                    if (bitmapDrawable != null) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Palette.Swatch swatch = ImageColorParser.generateColor(bitmap);
                        if (swatch != null) {
                            int colArgb = ImageColorParser.colorArgb(swatch.getRgb());
                            llLayoutPostDetails.setBackgroundColor(colArgb);
                        }
                    }

                }
                //end of color swatch

                geoPoint = postObject.getpGeoPoint();


                if (currentUid != null) {
                    if (currentUid.equals(postObject.getPostedById())) {
                        cbMark.setVisibility(View.VISIBLE);
                        String status = postObject.getStatus();
                        String postType = postObject.getpType();

                        if (currentUserType.equals(USERCONS.U_TYPE_DONOR)) {
                            if (status.equals(POSTCONS.P_STATUS_AVAILABLE)) {
                                cbMark.setChecked(false);
                                cbMark.setEnabled(true);
                            } else if (status.equals(POSTCONS.P_STATUS_DONATED)) {
                                cbMark.setChecked(false);
                                cbMark.setEnabled(true);
                            } else {
                                cbMark.setChecked(true);
                                cbMark.setEnabled(false);
                            }
                        } else {
                            if (status.equals(POSTCONS.P_STATUS_AVAILABLE)) {
                                cbMark.setChecked(false);
                                cbMark.setEnabled(true);
                            } else if (status.equals(POSTCONS.P_STATUS_DONATED)) {
                                cbMark.setChecked(false);
                                cbMark.setEnabled(true);
                            } else {
                                cbMark.setChecked(true);
                                cbMark.setEnabled(false);
                            }
                        }

                        if (postType.equals(POSTCONS.P_TYPE_SHARE)) {
                            cbMark.setText(R.string.mark_as_donated);
                        } else {
                            cbMark.setText(R.string.mark_as_distributed);
                        }
                    } else {
                        cbMark.setEnabled(false);
                        cbMark.setVisibility(View.INVISIBLE);
                    }
                }

                //for rsponse button
                if (currentUid.equals(postObject.getPostedById())) {
                    btnShowResponse.setVisibility(View.VISIBLE);
                    btnResponse.setVisibility(View.GONE);
                } else {
                    btnShowResponse.setVisibility(View.GONE);


                    if (currentUserType.equals(USERCONS.U_TYPE_DONOR)
                            && postObject.getpType().equals(POSTCONS.P_TYPE_REQUEST)
                            && postObject.getStatus().equals(POSTCONS.P_STATUS_AVAILABLE)) {
                        btnResponse.setVisibility(View.VISIBLE);
                    } else if (currentUserType.equals(USERCONS.U_TYPE_ORGANIZATION)
                            && postObject.getpType().equals(POSTCONS.P_TYPE_SHARE)
                            && postObject.getStatus().equals(POSTCONS.P_STATUS_AVAILABLE)) {
                        btnResponse.setVisibility(View.VISIBLE);
                    } else {
                        btnResponse.setVisibility(View.GONE);
                    }
                }

            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        if (geoPoint == null) {
            return;
        }
        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location loc = locationResult.getLastLocation();
            geoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
            Log.d("onLocationResult", " callback");
        }
    };

    @Override
    public void onStop() {
        super.onStop();

    }
}
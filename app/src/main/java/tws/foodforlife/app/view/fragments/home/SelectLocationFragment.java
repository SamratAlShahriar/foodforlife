package tws.foodforlife.app.view.fragments.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tws.foodforlife.app.R;
import tws.foodforlife.app.network.NetworkUtils;
import tws.foodforlife.app.utils.LocationPermissionUtils;


public class SelectLocationFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    public static final String KEY_LOC_NAME = "locationName";
    public static final String KEY_LOC_LAT = "lat";
    public static final String KEY_LOC_LON = "lon";

    private NavController navController;
    private SupportMapFragment supportMapFragment;
    private ImageButton ibMyLocation;
    private MaterialButton btnConfirmLocation;

    private TextInputEditText etSearch;

    private static Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double currentLat = 0.0;
    private double currentLong = 0.0;
    private GeoPoint geoPoint;
    private GoogleMap mMap;

    public SelectLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fsl_fr_map_fragment);
        supportMapFragment.getMapAsync(this::onMapReady);

        ibMyLocation = view.findViewById(R.id.fsl_ibtn_my_location);
        btnConfirmLocation = view.findViewById(R.id.fsl_mat_btn_confirm_location);
        etSearch = view.findViewById(R.id.fsl_tiet_search);

        ibMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocationPermissionUtils.isPermitted(requireContext())) {
                    if (LocationPermissionUtils.isGpsEnabled(requireContext())) {
                        if (NetworkUtils.isConnected(requireContext())) {
                            gotoMyLocation();
                        } else {
                            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        LocationPermissionUtils.enableGps(requireContext());
                    }
                } else {
                    LocationPermissionUtils.requestPermissions(requireActivity());
                }

            }
        });

        btnConfirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Mylocation", "onClick: " + getCity(lastLocation));
                Bundle bundle = new Bundle();
                String city = getCity(lastLocation);
                bundle.putString(KEY_LOC_NAME, city);
                bundle.putDouble(KEY_LOC_LAT, lastLocation.getLatitude());
                bundle.putDouble(KEY_LOC_LON, lastLocation.getLongitude());
                navController.navigate(R.id.action_selectLocationFragment_to_newPostFragment, bundle);
            }
        });
    }


    private void gotoMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location loc = task.getResult();

                    if (loc != null) {
                        lastLocation = loc;
                        currentLat = loc.getLatitude();
                        currentLong = loc.getLongitude();
                        LatLng latLng = new LatLng(currentLat, currentLong);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
                        Address address = getAddress(lastLocation);
                        if (address != null) {
                            etSearch.setText(address.getAddressLine(0));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        mMap.setOnCameraIdleListener(this::onCameraIdle);
        setMap();
    }

    private void setMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            LocationPermissionUtils.requestPermissions(requireActivity());
            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location loc = task.getResult();

                    if (loc != null) {
                        lastLocation = loc;
                        currentLat = loc.getLatitude();
                        currentLong = loc.getLongitude();
                        LatLng latLng = new LatLng(currentLat, currentLong);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
                    }
                }
            }
        });
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
        //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onCameraIdle() {
        if (NetworkUtils.isConnected(requireContext())) {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            Location loc = new Location(LocationManager.GPS_PROVIDER);
            loc.setLatitude(cameraPosition.target.latitude);
            loc.setLongitude(cameraPosition.target.longitude);
            currentLat = loc.getLatitude();
            currentLong = loc.getLongitude();

            lastLocation = loc;
            Address address = getAddress(loc);
            if (address != null) {
                etSearch.setText(address.getAddressLine(0));
            }

            mMap.setOnCameraMoveListener(this);
        } else {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int reason) {


        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mMap.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        }
    }

    public Address getAddress(Location loc) {
        if (loc == null) {
            return null;
        }
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
    }

    public String getCity(Location loc) {
        Address address = getAddress(loc);
        if (address != null) {
            String districtWithName = address.getSubAdminArea();
            if (districtWithName != null && !districtWithName.isEmpty()) {
                if (districtWithName.contains(" ")) {
                    String district = districtWithName.split(" ")[0];
                    return district;
                } else {
                    return districtWithName;
                }
            }
        }
        return "";

    }

    /*public String generateAddress(Location loc) {
        StringBuilder addressBuilder = new StringBuilder("");
        Address address = getAddress(loc);
        if (address == null) {
            return "";
        }

        String area = address.getLocality();
        if (area != null && !area.isEmpty()) {
            addressBuilder.append(area);
        }

        String districtWithName = address.getSubAdminArea();
        if (districtWithName != null && !districtWithName.isEmpty()) {
            addressBuilder.append(", ");
            if (districtWithName.contains(" ")) {
                String district = districtWithName.split(" ")[0];
                addressBuilder.append(district);
            } else {
                addressBuilder.append(districtWithName);
            }
        }

        return addressBuilder.toString().trim();
    }*/

}
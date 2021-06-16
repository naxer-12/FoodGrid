package com.example.foodgrid;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.foodgrid.R;
import com.example.foodgrid.databinding.ActivityMapsBinding;
import com.example.foodgrid.helper.LocationHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationHelper locationHelper;
    private LocationCallback locationCallback;
    private LatLng currentLocation;
    private LatLng restaurantLocation;

    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.currentLocation = new LatLng(this.getIntent().getDoubleExtra("CURRENT_LAT", 0.0),
                this.getIntent().getDoubleExtra("CURRENT_LNG", 0.0));
        this.restaurantLocation = new LatLng(this.getIntent().getDoubleExtra("RESTAURANT_LAT", 0.0),
                this.getIntent().getDoubleExtra("RESTAURANT_LNG", 0.0));

        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);

        if (this.locationHelper.locationPermissionGranted) {
            this.initiateLocationListener();
        }
    }

    private void initiateLocationListener() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location loc : locationResult.getLocations()) {
                    currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));


                    Log.e(TAG, "onLocationResult: update location " + loc.toString());
                }
            }
        };

        this.locationHelper.requestLocationUpdates(this, this.locationCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationHelper.stopLocationUpdates(this, this.locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.locationHelper.requestLocationUpdates(this, this.locationCallback);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {

            mMap = googleMap;
            this.setupMapAppearance(googleMap);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));
            mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("restaurant location"));

        }
    }

    private void setupMapAppearance(GoogleMap googleMap) {
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(false);
        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        UiSettings myUiSettings = googleMap.getUiSettings();
        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);
        myUiSettings.setRotateGesturesEnabled(true);
        myUiSettings.setMyLocationButtonEnabled(true);

    }
}
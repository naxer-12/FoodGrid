package com.example.foodgrid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.foodgrid.databinding.ActivityUserOrderBinding;
import com.example.foodgrid.helper.LocationHelper;
import com.example.foodgrid.model.UserOrderModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTimeComparator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserOrder extends AppCompatActivity {

    private static final String TAG = "USER ORDER";

    ActivityUserOrderBinding activityUserOrderBinding;
    private final Calendar calendar = Calendar.getInstance();
    private final String dateFormat = "MM/dd/yy";
    private final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LocationCallback locationCallback;

    private LatLng currentLocation;

    private String currentAddressLine;
    UserOrderModel userOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserOrderBinding = ActivityUserOrderBinding.inflate(getLayoutInflater());
        setContentView(activityUserOrderBinding.getRoot());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserOrder.this, R.layout.support_simple_spinner_dropdown_item, getQuantity());
        activityUserOrderBinding.foodQuantitySpinnerLayout.setAdapter(adapter);
        Intent i = getIntent();
        userOrderModel = (UserOrderModel) i.getSerializableExtra("USER_ORDER");
        if (userOrderModel != null) {
            activityUserOrderBinding.dateOfOrder.setText(sdf.format(userOrderModel.getDate()));
            activityUserOrderBinding.foodItemText.setText(userOrderModel.getFoodItem());
            activityUserOrderBinding.foodQuantitySpinnerLayout.setText(userOrderModel.getFoodQuantity());
            activityUserOrderBinding.notesText.setText(userOrderModel.getAdditionalInstructions());
            activityUserOrderBinding.addressText.setText(userOrderModel.getCurrentLocation());

            activityUserOrderBinding.dateOfOrderTextInputLayout.setEnabled(false);
            activityUserOrderBinding.addOrder.setText("Update Data");

        }


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateOrderField();
            }


        };
        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);


        if (this.locationHelper.locationPermissionGranted) {
            Log.d(TAG, "onCreate: Location Permission Granted");

            //get the device location
//            this.lastLocation = this.locationHelper.getLastLocation(this);

//            keep getting locattion updates
            this.initiateLocationListener();

            this.locationHelper.getLastLocation(this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(Location location) {

                    if (location != null) {

                        lastLocation = location;
//                        binding.tvLocationInfo.setText(lastLocation.toString());
                        String obtainedAddress = locationHelper.getAddress(getApplicationContext(), lastLocation);
//                        binding.tvLocationInfo.setText(obtainedAddress);
                        Log.d(TAG, "onCreate: Last Location obtained " + lastLocation.toString());
                    }
                }
            });

//            if (this.lastLocation != null) {
//                this.binding.tvLocationInfo.setText(this.lastLocation.toString());
//                Log.d(TAG, "onCreate: Last Location obtained " + this.lastLocation.toString());
//            }else{
//                Log.e(TAG, "onCreate: No last location received so far");
//            }
        }
        activityUserOrderBinding.dateOfOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasWindowFocus()) {
                    new DatePickerDialog(UserOrder.this, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });


        activityUserOrderBinding.currentLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activityUserOrderBinding.address.setVisibility(View.GONE);

                } else {

                    activityUserOrderBinding.address.setVisibility(View.VISIBLE);
                }

            }
        });

        activityUserOrderBinding.addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkOrder = checkValidation();
                if (checkOrder) {
                    clearError();
//                    Log.d(TAG, "ORDER ID 1" + userOrderModel.getOrder_id());
                    try {

                        if (userOrderModel != null) {
                            long order_Id = userOrderModel.getOrder_id();
                            userOrderModel = new UserOrderModel(

                                    activityUserOrderBinding.foodItemTextInputLayout.getEditText().getText().toString(),
                                    activityUserOrderBinding.foodQuantitySpinnerLayout.getText().toString(),
                                    activityUserOrderBinding.notesTextInputLayout.getEditText().getText().toString(),
                                    sdf.parse(activityUserOrderBinding.dateOfOrderTextInputLayout.getEditText().getText().toString()),
                                    getCurrentLocation());
                            userOrderModel.setOrder_id(order_Id);
                        } else {
                            userOrderModel = new UserOrderModel(

                                    activityUserOrderBinding.foodItemTextInputLayout.getEditText().getText().toString(),
                                    activityUserOrderBinding.foodQuantitySpinnerLayout.getText().toString(),
                                    activityUserOrderBinding.notesTextInputLayout.getEditText().getText().toString(),
                                    sdf.parse(activityUserOrderBinding.dateOfOrderTextInputLayout.getEditText().getText().toString()),
                                    getCurrentLocation());
                        }


                        Log.d(TAG, "ORDER ID 2" + userOrderModel.getOrder_id());

                        Intent intent = new Intent();
                        intent.putExtra("USER_ORDER_DATA", (Serializable) userOrderModel);

                        Log.d(TAG, "ITEM ADDED TO ORDER INSIDE ACTIVITYY");

                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        activityUserOrderBinding.viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activityUserOrderBinding.address.getEditText().getText().toString().isEmpty()) {
                    Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geoCoder.getFromLocationName(activityUserOrderBinding.address.getEditText().getText().toString(), 1);
                        if (addresses.size() > 0) {

                            currentLocation = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                            Log.d("Latitude", "" + addresses.get(0).getLatitude());
                            Log.d("Longitude", "" + addresses.get(0).getLongitude());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                }


                Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                mapIntent.putExtra("CURRENT_LAT", currentLocation.latitude);
                mapIntent.putExtra("CURRENT_LNG", currentLocation.longitude);
                mapIntent.putExtra("RESTAURANT_LAT", 22.998729);
                mapIntent.putExtra("RESTAURANT_LNG", 72.533706);
                startActivity(mapIntent);
            }
        });

    }

    private String getCurrentLocation() {
        if (activityUserOrderBinding.currentLocationSwitch.isChecked()) {
            return currentAddressLine;
        } else {
            return activityUserOrderBinding.address.getEditText().getText().toString();
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
                    lastLocation = loc;
                    currentAddressLine = locationHelper.getAddress(getApplicationContext(), loc);
                    Log.e(TAG, "onLocationResult: update location " + currentAddressLine);
                }
            }
        };

        this.locationHelper.requestLocationUpdates(this, this.locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.locationHelper.REQUEST_CODE_LOCATION) {
            this.locationHelper.locationPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if (this.locationHelper.locationPermissionGranted) {
                //get the device location
                Log.d(TAG, "onCreate: Location Permission Granted - " + this.locationHelper.locationPermissionGranted);
            }
        }
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

    private void clearError() {
        activityUserOrderBinding.foodItemTextInputLayout.setError(null);
        activityUserOrderBinding.foodQuantityLayout.setError(null);
        activityUserOrderBinding.foodQuantityLayout.setError(null);
        activityUserOrderBinding.dateOfOrderTextInputLayout.setError(null);
        activityUserOrderBinding.dateOfOrderTextInputLayout.setError(null);
        activityUserOrderBinding.address.setError(null);
    }

    private boolean checkValidation() {
        boolean validate = true;
        if (activityUserOrderBinding.foodItemTextInputLayout.getEditText().getText().toString().isEmpty()) {
            activityUserOrderBinding.foodItemTextInputLayout.setError("Food Item cannot be empty");
            validate = false;
        }
        if (activityUserOrderBinding.foodQuantitySpinnerLayout.getText().toString().isEmpty()) {
            activityUserOrderBinding.foodQuantityLayout.setError("please select food quantity");
            validate = false;
        }
        if (activityUserOrderBinding.notesTextInputLayout.getEditText().getText().toString().isEmpty()) {
            activityUserOrderBinding.foodQuantityLayout.setError("please enter notes");
            validate = false;
        }
        if (activityUserOrderBinding.dateOfOrderTextInputLayout.getEditText().getText().toString().isEmpty()) {
            activityUserOrderBinding.dateOfOrderTextInputLayout.setError("Please enter date");
            validate = false;
        }

        String userDate = activityUserOrderBinding.dateOfOrderTextInputLayout.getEditText().getText().toString();
        try {
            Date strDate = sdf.parse(userDate);
            DateTimeComparator compareDates = DateTimeComparator.getDateOnlyInstance();

            int getValue = compareDates.compare(strDate, new Date());


            if (getValue < 0) {
                activityUserOrderBinding.dateOfOrderTextInputLayout.setError("Date cannot be older");
                validate = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!activityUserOrderBinding.currentLocationSwitch.isChecked() && activityUserOrderBinding.address.getEditText().getText().toString().isEmpty()) {
            //TODO select current location

            activityUserOrderBinding.address.setError("Please enter address");
            validate = false;
        }

        return validate;
    }

    private void updateOrderField() {
        activityUserOrderBinding.dateOfOrder.setText(sdf.format(calendar.getTime()));

    }

    private ArrayList<String> getQuantity() {
        ArrayList<String> quantity = new ArrayList<>();
        quantity.add("5 kg");
        quantity.add("20 kg");
        quantity.add("30 kg");
        return quantity;
    }
}
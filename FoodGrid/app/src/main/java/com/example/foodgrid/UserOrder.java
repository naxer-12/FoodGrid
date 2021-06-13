package com.example.foodgrid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.example.foodgrid.databinding.ActivityUserOrderBinding;
import com.example.foodgrid.model.UserOrderModel;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserOrder extends AppCompatActivity {

    private static final String TAG = "USER ORDER";
    ActivityUserOrderBinding activityUserOrderBinding;
    private final Calendar calendar = Calendar.getInstance();
    private final String dateFormat = "MM/dd/yy";
    private final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserOrderBinding = ActivityUserOrderBinding.inflate(getLayoutInflater());
        setContentView(activityUserOrderBinding.getRoot());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserOrder.this, R.layout.support_simple_spinner_dropdown_item, getQuantity());
        activityUserOrderBinding.foodQuantitySpinnerLayout.setAdapter(adapter);
        Intent i = getIntent();
        UserOrderModel userOrderModel = (UserOrderModel) i.getSerializableExtra("USER_ORDER");
        if (userOrderModel != null) {
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
                    //TODO add order
                }
            }
        });

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

        if (activityUserOrderBinding.currentLocationSwitch.isChecked()) {
            //TODO select current location
        } else if (activityUserOrderBinding.address.getEditText().getText().toString().isEmpty()) {
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
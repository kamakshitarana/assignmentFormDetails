package com.example.android_assignmrnt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String formDetailsMessage;
    private EditText editTextFirstName, editTextLastName, editTextMobileNo, editTextDOB,
            editTextBloodGroup, editTextAddress, editTextCity, editTextState, editTextCountry;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextBloodGroup = findViewById(R.id.editTextBloodGroup);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextCity = findViewById(R.id.editTextCity);
        editTextState = findViewById(R.id.editTextState);
        editTextCountry = findViewById(R.id.editTextCountry);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    if (isFormValid()) {
                        saveFormDetailsToDatabase();
                        showToast("Data submitted");
                        displayFormDetailsToast();
                    }
                } else {
                    if (isFormValid()) {
                        saveFormDetailsToDatabase();
                        showToast("Form details saved locally");
                    }
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveFormDetailsToDatabase() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String bloodGroup = editTextBloodGroup.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = editTextState.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();

        FormDetails formDetails = new FormDetails();
        formDetails.setFirstName(firstName);
        formDetails.setLastName(lastName);
        formDetails.setMobileNo(mobileNo);
        formDetails.setDob(dob);
        formDetails.setBloodGroup(bloodGroup);
        formDetails.setAddress(address);
        formDetails.setCity(city);
        formDetails.setState(state);
        formDetails.setCountry(country);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                db.formDetailsDao().insert(formDetails);
            }
        });

        formDetailsMessage = ""; // Clear the formDetailsMessage when saving new form details
    }

    private void displayFormDetailsToast() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String bloodGroup = editTextBloodGroup.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = editTextState.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();

        formDetailsMessage = "First Name: " + firstName
                + "\nLast Name: " + lastName
                + "\nMobile No: " + mobileNo
                + "\nDOB: " + dob
                + "\nBlood Group: " + bloodGroup
                + "\nAddress: " + address
                + "\nCity: " + city
                + "\nState: " + state
                + "\nCountry: " + country;

        showToast(formDetailsMessage);
    }

    private boolean isFormValid() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String bloodGroup = editTextBloodGroup.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = editTextState.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();

        if (firstName.isEmpty()) {
            showToast("Please enter First Name");
            return false;
        } else if (lastName.isEmpty()) {
            showToast("Please enter Last Name");
            return false;
        } else if (mobileNo.isEmpty()) {
            showToast("Please enter Mobile No");
            return false;
        } else if (dob.isEmpty()) {
            showToast("Please enter Date of Birth");
            return false;
        } else if (bloodGroup.isEmpty()) {
            showToast("Please enter Blood Group");
            return false;
        } else if (address.isEmpty()) {
            showToast("Please enter Address");
            return false;
        } else if (city.isEmpty()) {
            showToast("Please enter City");
            return false;
        } else if (state.isEmpty()) {
            showToast("Please enter State");
            return false;
        } else if (country.isEmpty()) {
            showToast("Please enter Country");
            return false;
        }

        return true;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline()) {
            getSavedFormDetails();
        }
    }

    private void getSavedFormDetails() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                List<FormDetails> savedFormDetails = db.formDetailsDao().getAllFormDetails();
                boolean isOnline = isOnline(); // Check the network state outside of runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isOnline) {
                            displayFormDetails(savedFormDetails);
                        } else {
                            if (savedFormDetails.isEmpty()) {
                                showToast("Fill the form details");
                            } else {
                                showToast("No internet connection. Form details saved locally.");
                            }
                        }
                    }
                });
            }
        });
    }



    private void displayFormDetails(List<FormDetails> formDetailsList) {
        if (formDetailsList.isEmpty()) {
            showToast("No saved form details found");
            return;
        }

        for (FormDetails formDetails : formDetailsList) {
            String message = "First Name: " + formDetails.getFirstName()
                    + "\nLast Name: " + formDetails.getLastName()
                    + "\nMobile No: " + formDetails.getMobileNo()
                    + "\nDOB: " + formDetails.getDob()
                    + "\nBlood Group: " + formDetails.getBloodGroup()
                    + "\nAddress: " + formDetails.getAddress()
                    + "\nCity: " + formDetails.getCity()
                    + "\nState: " + formDetails.getState()
                    + "\nCountry: " + formDetails.getCountry();
            showToast(message);
        }
    }
}

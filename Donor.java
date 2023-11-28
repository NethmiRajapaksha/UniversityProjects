package com.example.smilingheart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.hardware.Sensor.TYPE_LIGHT;

import com.example.smilingheart.databinding.ActivityDonorBinding;

import java.util.regex.Pattern;

public class Donor extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                            "(?=.*[0-9])"+
                            "(?=.*[a-z])"+
                            "(?=.*[A-Z])"+
                            "(?=.*[@#$%^&*+=])"+
                            "(?=\\S+$)"+
                            ".{6,}" +
                            "$");

    ActivityDonorBinding binding;
    DatabaseHelper databaseHelper;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View nethmi;
    private float maxVlue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        nethmi = findViewById(R.id.nethmi);

        //Connect this Donor.java with DatabaseHelper.java
        databaseHelper = new DatabaseHelper(this);

        //Validation of Donor registration
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String address = binding.address.getText().toString();
                String phone_no = binding.phone.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                if (name.equals("") || address.equals("") || phone_no.equals("") || email.equals("") || password.equals(""))
                    Toast.makeText(Donor.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();

                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    binding.password.setError("Password need atleast one lowercase letter, one uppercase letter, one number and one special character.");
                } else {
                    if (email.equals(email)) {
                        Boolean checkuserEmail = databaseHelper.checkEmail(email);
                        if (checkuserEmail == false) {
                            Boolean insert = databaseHelper.insertData(name, address, phone_no, email, password);

                            if (insert == true) {
                                Toast.makeText(Donor.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Donor.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Donor.this, "User Already Exists. Please Login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Donor.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        //Implementation of light sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "This device has no light sensor :(", Toast.LENGTH_SHORT).show();
            finish();
        }
        maxVlue = lightSensor.getMaximumRange();
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
                int newValue = (int) (255f * value / maxVlue);
                nethmi.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }

    public void lginText(View view){
        Intent intent = new Intent(this,LogIn.class);
        startActivity(intent);
    }
}
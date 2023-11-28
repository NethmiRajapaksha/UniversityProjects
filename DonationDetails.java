package com.example.smilingheart;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.hardware.Sensor.TYPE_LIGHT;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DonationDetails extends AppCompatActivity {

    private EditText etFromLocation;
    private EditText etToLocation;
    private Button btnMap;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View nethmi;
    private float maxVlue;
    private TextView d_name;
    private TextView d_food;
    private TextView d_number;
    private TextView d_description;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        nethmi = findViewById(R.id.nethmi);

        //Getting data from donate page
        d_name=(TextView)findViewById(R.id.tvDonorName);
        d_food=(TextView)findViewById(R.id.tvFoodItems);
        d_number=(TextView)findViewById(R.id.tvNumber);
        d_description=(TextView)findViewById(R.id.tvDesc);

        Intent intent=getIntent();
        String name=intent.getStringExtra("Name");
        String food=intent.getStringExtra("Food");
        String number=intent.getStringExtra("Number");
        String desc=intent.getStringExtra("Description");

        d_name.setText(name);
        d_food.setText(food);
        d_number.setText(number);
        d_description.setText(desc);

        //Adding a bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), DonorProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        //Map implementation
        etFromLocation = findViewById(R.id.etFromLocation);
        etToLocation = findViewById(R.id.etToLocation);
        btnMap = findViewById(R.id.btnMap);

        btnMap.setOnClickListener(view -> {
            String userLocation = etFromLocation.getText().toString();
            String userDestination = etToLocation.getText().toString();

            if (userLocation.equals("") || userDestination.equals("")) {
                Toast.makeText(this,"Please enter your location and destination", Toast.LENGTH_SHORT).show();
            }else {
                getDirections(userLocation, userDestination);
            }
        });

        //Light sensor implementation
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
    private void getDirections(String userLocation, String userDestination) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+ userLocation +"/" + userDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException exception){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
    public void btnMap(View view){
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }
}
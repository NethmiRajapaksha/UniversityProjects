package com.example.smilingheart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.hardware.Sensor.TYPE_LIGHT;

public class DonorProfile extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View nethmi;
    private float maxVlue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        nethmi = findViewById(R.id.nethmi);

        TextView profileName = findViewById(R.id.profileName);
        TextView profilePnumber = findViewById(R.id.profilePnumber);
        TextView profileEmail = findViewById(R.id.profileEmail);
        ImageView profileImg = findViewById(R.id.profileImg);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getUser();

        if (cursor.getCount() == 0){
            Toast.makeText(this, "No profile details", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                profileName.setText(""+cursor.getString(0));
                profileEmail.setText(""+cursor.getString(1));
                profilePnumber.setText(""+cursor.getString(2));
                byte[] imageByte = cursor.getBlob(3);

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0, imageByte.length);
                profileImg.setImageBitmap(bitmap);
            }
        }

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

    public void editButton(View view){
        Intent intent = new Intent(this,EditProfile.class);
        startActivity(intent);
    }

    public void arrow(View view){
        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
    }
}
package com.example.smilingheart;

import static android.hardware.Sensor.TYPE_LIGHT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Organizations extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;
    SearchView searchView;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View nethmi;
    private float maxVlue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations);
        nethmi = findViewById(R.id.nethmi);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList (newText);
                return false;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Organizations.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        androidData = new DataClass("Zam Zam Foundation", R.string.zamzam, "Dehiwala", R.drawable.zamzamf);
        dataList.add(androidData);

        androidData = new DataClass("Volunteer Lanka", R.string.lanka, "Colombo", R.drawable.vl);
        dataList.add(androidData);

        androidData = new DataClass("Voice of Voiceless ", R.string.vfv, "Rajagiriya", R.drawable.vfv);
        dataList.add(androidData);

        androidData = new DataClass("Serve ", R.string.serve, "Moratuwa", R.drawable.serves);
        dataList.add(androidData);

        adapter = new MyAdapter(Organizations.this, dataList);
        recyclerView.setAdapter(adapter);

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

    private void searchList (String text) {
        List<DataClass> dataSearchList = new ArrayList<>();
        for (DataClass data : dataList) {
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setSearchList(dataSearchList);
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
}
package com.example.smilingheart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.hardware.Sensor.TYPE_LIGHT;

public class Community extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName, editAddress, editPhone_No, updatePhone_No;
    Button btnAddData, btnViewAll, btnUpdateData, btnDeleteData;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View nethmi;
    private float maxVlue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.CommunityName);
        editAddress = findViewById(R.id.CommunityAddress);
        editPhone_No = findViewById(R.id.Community_Phone);
        updatePhone_No = findViewById(R.id.CommunityUpdate);
        btnAddData = findViewById(R.id.btnInsert);
        btnViewAll = findViewById(R.id.btnView);
        btnUpdateData = findViewById(R.id.btnUpdate);
        btnDeleteData = findViewById(R.id.btnDelete);

        addData();
        viewAll();
        updateData();
        deleteData();

        nethmi = findViewById(R.id.nethmi);

        //light sensor implementation
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
    //Implement onClickListener method to handle INSERT DATA button functionality.
    public void addData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),editAddress.getText().toString(),
                                editPhone_No.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(Community.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Community.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //Implement onClickListener method to handle VIEW DATA button functionality.
    public void viewAll(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor results = myDb.getAllData();
                        if (results.getCount()==0){
                            showMessage("Error Message :", "No data found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(results.moveToNext()){
                            buffer.append("ID :" +results.getString(0) + "\n");
                            buffer.append("Name :" +results.getString(1) + "\n");
                            buffer.append("Address :" +results.getString(2) + "\n");
                            buffer.append("Phone_No :" +results.getString(3) + "\n\n");

                            showMessage("List of Data :", buffer.toString());
                        }
                    }
                }
        );
    }
    //Show message using alert dialog box
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //Implement onClickListener method to handle UPDATE DATA button functionality.
    public void updateData() {
        btnUpdateData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isUpdate = myDb.updateData(updatePhone_No.getText().toString(), editName.getText().toString(),
                                editAddress.getText().toString(), editPhone_No.getText().toString());
                        if (isUpdate == true)
                            Toast.makeText(Community.this, "Data Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Community.this, "Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //Implement onClickListener method to handle DELETE DATA button functionality.
    public void deleteData(){
        btnDeleteData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deletedatarows = myDb.deleteData(updatePhone_No.getText().toString());
                        if (deletedatarows>0)
                            Toast.makeText(Community.this, "Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Community.this, "Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
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
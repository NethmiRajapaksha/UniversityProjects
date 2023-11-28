package com.example.smilingheart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.smilingheart.databinding.ActivityDetailedBinding;

public class Detailed extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            int address = intent.getIntExtra("address", R.string.sjAddress);
            int number = intent.getIntExtra("number", R.string.sjNumber);
            int image = intent.getIntExtra("image", R.drawable.sjlogo);

            binding.orphanageName.setText(name);
            binding.orphanageAddress.setText(address);
            binding.orphanageNumber.setText(number);
            binding.detailImage.setImageResource(image);
        }
    }
}
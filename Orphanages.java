package com.example.smilingheart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.smilingheart.databinding.ActivityOrphanagesBinding;

import java.util.ArrayList;

public class Orphanages extends AppCompatActivity {

    ActivityOrphanagesBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.drawable.houseicon};
        int[] addressList = {R.string.sjAddress, R.string.vsAddress};
        int[] numberList = {R.string.sjNumber, R.string.vsNumber};
        String[] nameList = {"Sri Jinananda Children's Home", "Vajira Sri Children's Development Center"};

        for (int i = 0; i<imageList.length; i++){
            listData = new ListData(nameList[i], addressList[i], numberList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(Orphanages.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Orphanages.this, Detailed.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("address", addressList[i]);
                intent.putExtra("number", numberList[i]);
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });
    }
}
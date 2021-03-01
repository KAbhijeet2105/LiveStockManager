package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityStatisticsBinding;

import java.util.ArrayList;
import java.util.Collections;

public class Statistics extends AppCompatActivity {

    ActivityStatisticsBinding binding;

    FirebaseDatabase rootnode;
    DatabaseReference statRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        rootnode = FirebaseDatabase.getInstance();

        statRef = rootnode.getReference().child("AreaStats");

//        statRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(5,44,"aak"));
        barEntries.add(new BarEntry(11,88,"ddd"));
        barEntries.add(new BarEntry(25,62,"ccc"));
        barEntries.add(new BarEntry(10,33,"zzz"));
        barEntries.add(new BarEntry(33,20,"aaa"));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Animals");





        ArrayList<String> regions = new ArrayList<String>();


        regions.add("Kop");
        regions.add("sangali");
        regions.add("satara");
        regions.add("pune");
        regions.add("mahad");
        regions.add("dhule");
        regions.add("amaravati");
        regions.add("khed");

        BarData data = new BarData(barDataSet);


        binding.barchartStats.setData(data);
        binding.barchartStats.setTouchEnabled(true);
        binding.barchartStats.setDragEnabled(true);
        binding.barchartStats.setScaleEnabled(true);




    }


}
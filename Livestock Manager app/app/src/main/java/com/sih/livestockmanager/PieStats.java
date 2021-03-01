package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityPieStatsBinding;
import com.sih.livestockmanager.databinding.ActivityStatisticsBinding;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PieStats extends AppCompatActivity {

    ActivityPieStatsBinding binding;

    FirebaseDatabase rootnode;
    DatabaseReference statRef;

    ArrayList<PieEntry> myPieEntrey = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityPieStatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        rootnode = FirebaseDatabase.getInstance();
        statRef = rootnode.getReference().child("AreaStats");

       // ArrayList<PieEntry> myPieEntrey = new ArrayList<>();


        statRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // showData(dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren() )
                {
//                    AreaHelper areaHelper = new AreaHelper();
//                    areaHelper.setAnimal_count();
//                    areaHelper.setRegionId();
//                    ds.child("Animal_count").getValue(String.class);
//                    ds.child("regionId").getValue(String.class);
                      try {
                          myPieEntrey.add(new PieEntry(Integer.parseInt( ds.child("Animal_count").getValue(String.class))+1,ds.child("regionId").getValue(String.class)));

                          binding.pieChartStats.notifyDataSetChanged();
                          binding.pieChartStats.invalidate();

                      }catch (Exception es)
                      {
                          Toasty.error(PieStats.this,"exception"+es,Toasty.LENGTH_SHORT,true).show();
                      }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        PieDataSet data = new PieDataSet(myPieEntrey, "Region-wise animal statistics");

        data.setSliceSpace(3f);
        data.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(25f);

        Description description = new Description();
        description.setText("Region-wise animal statistics");
        description.setEnabled(true);

        description.setTextSize(30f);


        PieData pieData = new PieData(data);
        binding.pieChartStats.setData(pieData);
        binding.pieChartStats.setDrawSlicesUnderHole(false);
        binding.pieChartStats.setDrawHoleEnabled(false);
        binding.pieChartStats.setDescription(description);
        binding.pieChartStats.animateXY(1500,1500);

    }
}
package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sih.livestockmanager.databinding.ActivityAdminDashboardBinding;
import com.sih.livestockmanager.databinding.ActivityUserDashboardBinding;

public class AdminDashboard extends AppCompatActivity {


    ActivityAdminDashboardBinding binding;

//    SharedPreferences sharedPreferences = getSharedPreferences("UIDDATA",MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

       // binding.txtVwUseDashTitle.setText(sharedPreferences.getString("AHOID","no data"));

    }


    public void GoRequests(View view)
    {
        Intent i = new Intent(AdminDashboard.this,AdminRequest.class);
         startActivity(i);
    }

    public void GoStatistics(View view)
    {
       Intent i = new Intent(AdminDashboard.this,PieStats.class);
       startActivity(i);

    }



    public void goScanAnimalQRADM(View v)
    {
        Intent i = new Intent(AdminDashboard.this,ScanAnimalQR.class);
        startActivity(i);
    }

    public void goQuickStats(View v)
    {
        Intent i = new Intent(AdminDashboard.this,QuickStats.class);
        startActivity(i);
    }


}
package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.sih.livestockmanager.databinding.ActivityRegisterUserBinding;
import com.sih.livestockmanager.databinding.ActivityUserDashboardBinding;

public class UserDashboard extends AppCompatActivity {


    ActivityUserDashboardBinding binding;

   // SharedPreferences sharedPreferences = getSharedPreferences("UIDDATA",MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

       // binding.txtVwUseDashTitle.setText(sharedPreferences.getString("PHN","no data"));


    }


    public void goAddAnimal(View v)
    {
        Intent i = new Intent(UserDashboard.this,AddAnimal.class);
        startActivity(i);
    }

    public void goMyAnimals(View v)
    {
        Intent i = new Intent(UserDashboard.this,MyAnimals.class);
        startActivity(i);
    }

    public void goMyQR(View v)
    {
        Intent i = new Intent(UserDashboard.this,ShowUserQR.class);
        startActivity(i);
    }

    public void goScanAnimalQR(View v)
    {
        Intent i = new Intent(UserDashboard.this,ScanAnimalQR.class);
        startActivity(i);
    }


}
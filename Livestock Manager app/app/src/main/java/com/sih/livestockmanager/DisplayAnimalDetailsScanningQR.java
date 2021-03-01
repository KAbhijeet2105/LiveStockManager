package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityDisplayAnimalDetailsScanningQRBinding;

import es.dmoral.toasty.Toasty;


public class DisplayAnimalDetailsScanningQR extends AppCompatActivity {
    String scanned_id="";
    private FirebaseDatabase rootnode;
    private DatabaseReference regUser;

    ActivityDisplayAnimalDetailsScanningQRBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_display_animal_details_scanning_q_r);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scanned_id = getIntent().getStringExtra("decoded_animal_id");
        rootnode = FirebaseDatabase.getInstance();
        regUser = rootnode.getReference().child("Animals");

        binding = ActivityDisplayAnimalDetailsScanningQRBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.txtVwQrDetailAnimalId.setText("Animal ID: "+scanned_id);
        //fetch req details

        regUser.child(scanned_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    binding.txtVwQrDetailBreed.setText("Breed: "+dataSnapshot.child("breed").getValue(String.class));
                    binding.txtVwQrDetailCategory.setText("Category: "+dataSnapshot.child("category").getValue(String.class));
                    binding.txtVwQrDetailDob.setText("D.O.B.: "+dataSnapshot.child("dob").getValue(String.class));
                    binding.txtVwQrDetailReqStatus.setText("Request Approved:  "+dataSnapshot.child("req_approve").getValue(String.class));

                    binding.txtVwQrDetailWeight.setText("Weight: "+dataSnapshot.child("weight").getValue(String.class));
                    binding.txtVwQrDetailUse.setText("Use: "+dataSnapshot.child("use").getValue(String.class));
                    binding.txtVwQrDetailUserId.setText("User ID:"+dataSnapshot.child("ownerid").getValue(String.class));
                    binding.txtVwQrDetailUserName.setText("Owner Name:"+dataSnapshot.child("ownernm").getValue(String.class));
                    //selectedUser=dataSnapshot.child("ownerid").getValue(String.class);

                    binding.txtVwQrDetailUserPhoneNo.setText("Phone Number"+dataSnapshot.child("ownerphn").getValue(String.class));
                    binding.txtVwQrDetailAnimalAge.setText("Animal Age: "+dataSnapshot.child("age").getValue(String.class));
                    binding.txtVwQrDetailUserRegion.setText("Region: "+dataSnapshot.child("regionId").getValue(String.class));
                    binding.txtVwQrDetailIsAlive.setText("Is Alive: "+dataSnapshot.child("isAlive").getValue(String.class));

                }
                else {

                    Toasty.error(DisplayAnimalDetailsScanningQR.this, "Error data not available!!", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }




}

package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sih.livestockmanager.databinding.ActivityScanAnimalQRBinding;
import com.sih.livestockmanager.databinding.ActivitySellScanUserIDBinding;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SellScanUserID extends AppCompatActivity {

    ActivitySellScanUserIDBinding binding;
    private CodeScanner scanner;
    private String decoded_id="";
    String my_user_id="",selling_animal_id="",decoded_UserName="";
    int owner_animal_count,customer_animal_count;

    String ownerid,ownernm,ownerphn,animalId,category,breed,gender,weight,dob,age,use,OwnerregionId,isAlive,req_approve,remark;

    String customernm,custPhoneno,custRegion;

    FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference customerRef,myRef,globalRef,Arearef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sell_scan_user_i_d);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        rootnode  = FirebaseDatabase.getInstance();




          //Current owners user id
        // my_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef = rootnode.getReference().child("root").child("Users").child(mAuth.getCurrentUser().getUid());


         // get the animal id to be sold
        selling_animal_id = getIntent().getStringExtra("selling_animal_id");
           my_user_id=getIntent().getStringExtra("owner_main_id");

           //collect all animal data from intent PS>Firebase not working..


        category= getIntent().getStringExtra("category");
        breed= getIntent().getStringExtra("breed");
        gender= getIntent().getStringExtra("gender");
        weight =getIntent().getStringExtra("weight");
        dob =getIntent().getStringExtra("dob");
        age =getIntent().getStringExtra("age");
        use =getIntent().getStringExtra("use");
        isAlive =getIntent().getStringExtra("isAlive");
        req_approve =getIntent().getStringExtra("req_approve");
        remark =getIntent().getStringExtra("remark");
        OwnerregionId =getIntent().getStringExtra("OwnerregionId");

           if (my_user_id.equals(null))
           {
               finish();
           }


         binding = ActivitySellScanUserIDBinding.inflate(getLayoutInflater());
       View v =binding.getRoot();
       setContentView(v);

        scanner = new CodeScanner(this,binding.sellAnimalQRCodeScannerView);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //get the customer id
                        decoded_id= result.getText().trim();

                        ///customer database neference
                        customerRef = rootnode.getReference().child("root").child("Users").child(decoded_id);
                        // get customer details

                        // and obtain final decision on sell
                        customerRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists())
                                {

                                    //get customer name from database
                                   // ownernm = dataSnapshot.child("username").getValue(String.class);


                                    customernm = dataSnapshot.child("username").getValue(String.class);
                                    custPhoneno= dataSnapshot.child("phoneno").getValue(String.class);
                                    custRegion=dataSnapshot.child("area").getValue(String.class);


                                          Intent i = new Intent(SellScanUserID.this,SellAnimal.class);
                                          i.putExtra("cust_id",decoded_id);
                                          i.putExtra("customernm",customernm);
                                          i.putExtra("custPhoneno",custPhoneno);
                                          i.putExtra("custRegion",custRegion);
                                          i.putExtra("selling_animal_id",selling_animal_id);

                                          //send animal data

                                         i.putExtra("category",category);
                                         i.putExtra("breed",breed);
                                         i.putExtra("gender",gender);
                                         i.putExtra("weight",weight);
                                         i.putExtra("dob",dob);
                                         i.putExtra("age",age);
                                         i.putExtra("use",use);
                                         i.putExtra("isAlive",isAlive);
                                         i.putExtra("req_approve",req_approve);
                                         i.putExtra("remark",remark);
                                    i.putExtra("OwnerregionId",OwnerregionId);
                                    startActivity(i);




//                                    AlertDialog.Builder builder = new AlertDialog.Builder(SellScanUserID.this);
//                                    builder.setTitle("Sell Animal ");
//                                    builder.setIcon(R.drawable.animal_icon);
//                                    builder.setMessage("Are you sure ? you want to sell your animal to Mr."+customernm+" ? ");
//
//                                    // Set up the buttons
//                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toasty.info(SellScanUserID.this,"Transaction in progress please wait!", Toast.LENGTH_SHORT,true).show();
//                                            //sellAnimal();
//
//
//
//                                        }
//                                    });
//                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                            dialog.cancel();
//
//                                            Toasty.info(SellScanUserID.this,"Transaction Canceled!", Toast.LENGTH_SHORT,true).show();
//                                            Intent i = new Intent(SellScanUserID.this,UserDashboard.class);
//                                            startActivity(i);
//                                            finish();
//
//                                        }
//                                    });
//                                    builder.show();

                                }
                                else{

                                    Toasty.error(SellScanUserID.this,"Error :User not found!",Toast.LENGTH_SHORT,true).show();
                                   Intent i = new Intent(SellScanUserID.this,UserDashboard.class);
                                   startActivity(i);
                                    SellScanUserID.this.finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });


        binding.sellAnimalQRCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanner.startPreview();
            }
        });

    }//On create end here


    @Override
    protected void onResume() {
        super.onResume();

        requestForCamera();

    }

    @Override
    protected void onPause() {
        scanner.releaseResources();
        super.onPause();
    }



//camera permission

    private void requestForCamera() {


        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Toasty.error(SellScanUserID.this,"Camera Permission Required!!", Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();


    }


// collect animal data


    // collect animal data

  /*  void collect_animal()
    {

       // myRef = rootnode.getReference().child("root").child("Users").child(mAuth.getCurrentUser().getUid());

        //TODO: get animal data

        globalRef = rootnode.getReference().child("Animals").child(selling_animal_id);

        //myRef.child("Animal").child(selling_animal_id);

        // getting animal data from database
        globalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    animalId = dataSnapshot.child("animalId").getValue(String.class);  //selling_animal_id;
                    category = dataSnapshot.child("category").getValue(String.class);
                    breed   = dataSnapshot.child("breed").getValue(String.class);
                    gender  = dataSnapshot.child("gender").getValue(String.class);
                    weight  = dataSnapshot.child("weight").getValue(String.class);
                    dob = dataSnapshot.child("dob").getValue(String.class);
                    age = dataSnapshot.child("age").getValue(String.class);
                    use = dataSnapshot.child("use").getValue(String.class);
                    isAlive = dataSnapshot.child("isAlive").getValue(String.class);
                    req_approve = dataSnapshot.child("req_approve").getValue(String.class);
                    remark  = dataSnapshot.child("Remark").getValue(String.class);
                    OwnerregionId = dataSnapshot.child("regionId").getValue(String.class);

                }
                else {
                    Toasty.error(SellScanUserID.this,"Animal data not found",Toast.LENGTH_SHORT,true).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   */



    void sellAnimal()
    {

        //collect animal data through intent

      //  collect_animal();

        //// updating area stats


        // Store animal in customer database

        AnimalHelper animalHelper = new AnimalHelper(decoded_id,customernm,custPhoneno,selling_animal_id,category,breed,gender,weight,dob,age,use,custRegion,isAlive,req_approve);

          customerRef = rootnode.getReference().child("root").child("Users").child(decoded_id).child("Animal");
          customerRef.child(selling_animal_id).setValue(animalHelper);
          customerRef.child(selling_animal_id).child("Remark").setValue(remark);

          //// update globally in requests and Animals

        // in Animals
        globalRef = rootnode.getReference().child("Animals").child(selling_animal_id);
        globalRef.setValue(animalHelper);
        globalRef.child("Remark").setValue(remark);

        Toasty.success(SellScanUserID.this,"Transaction med stage before global update",Toast.LENGTH_SHORT,true).show();

        // in Requests

        globalRef = rootnode.getReference().child("Requests").child(selling_animal_id);
        globalRef.setValue(animalHelper);
        globalRef.child("Remark").setValue(remark);


        Toasty.success(SellScanUserID.this,"Transaction success before del!",Toast.LENGTH_SHORT,true).show();


        // now delete the animal from owner database

       //  del_owner_animal();


     //done   //TODO: store animal in customer database
     //done   //TODO: add remark explicitely
     //done   //TODO: delete animal in owner database
     //done   //TODO: update everywhere
     //done   //TODO: update area wise

    }




    void del_owner_animal()
    {
        //selling_animal_id = animalId;

        myRef =rootnode.getReference().child("root").child("Users").child(mAuth.getCurrentUser().getUid()).child("Animal").child(selling_animal_id);
        myRef.setValue(null);
       // updateAreastats();
        Toasty.success(SellScanUserID.this,"Transaction successful!",Toast.LENGTH_SHORT,true).show();
                /*child().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                updateAreastats();
                Toasty.success(SellScanUserID.this,"Transaction successful!",Toast.LENGTH_SHORT,true).show();

                //Intent i = new Intent(SellScanUserID.this,UserDashboard.class);
                //startActivity(i);
               // SellScanUserID.this.finish();
            }
        });*/

    }




    void updateAreastats()
    {
        //1.decrement from owner area

        Arearef =  rootnode.getReference().child("AreaStats").child(OwnerregionId);

        Arearef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String owner_animal;

                owner_animal = dataSnapshot.child("Animal_count").getValue(String.class);

                if (owner_animal.equals(null))
                {
                    owner_animal_count =0;

                }
                else
                {
                    owner_animal_count = (Integer.parseInt(owner_animal));
                    owner_animal_count = 0;//owner_animal_count-1
                    // Arearef.child("Animal_count").setValue(""+animal_count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Arearef.child("Animal_count").setValue(""+owner_animal_count);

        //2.Now add animal in owners region

        Arearef =  rootnode.getReference().child("AreaStats").child(custRegion);

        Arearef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String cust_animal;

                cust_animal = dataSnapshot.child("Animal_count").getValue(String.class);

                customer_animal_count = (Integer.parseInt(cust_animal));
                customer_animal_count = customer_animal_count+1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Arearef.child("Animal_count").setValue(""+customer_animal_count);


    }





}
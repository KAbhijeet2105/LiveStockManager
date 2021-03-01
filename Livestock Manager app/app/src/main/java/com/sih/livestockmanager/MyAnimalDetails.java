package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sih.livestockmanager.databinding.ActivityMyAnimalDetailsBinding;
import com.sih.livestockmanager.databinding.ActivityMyAnimalsBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.dmoral.toasty.Toasty;

public class MyAnimalDetails extends AppCompatActivity {

    ActivityMyAnimalDetailsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser,reAnimalusr;

    Button savegalary;
    Dialog qrDialog;
    ImageView showQRAnimal;
    Bitmap globalQR;

    String category,breed,gender,weight,dob,age,use,isAlive,req_approve,remark,OwnerregionId;

    String selectedAnimal_id="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestForStorage();

        binding = ActivityMyAnimalDetailsBinding.inflate(getLayoutInflater());
        View v =binding.getRoot();
        setContentView(v);

        mAuth =FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        Userrroot =rootnode.getReference().child("root").child("Users");
        reAnimalusr = Userrroot.child(mAuth.getCurrentUser().getUid()).child("Animal");

       selectedAnimal_id = getIntent().getStringExtra("selected_animal_id");

        binding.txtVwAnimalDetailId.setText("ID: "+selectedAnimal_id);



        // fetch selected animal data

        reAnimalusr.child(selectedAnimal_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {

                            category=dataSnapshot.child("category").getValue(String.class);
                            breed=dataSnapshot.child("breed").getValue(String.class);
                            gender= dataSnapshot.child("gender").getValue(String.class);
                            weight=dataSnapshot.child("weight").getValue(String.class);
                            dob=dataSnapshot.child("dob").getValue(String.class);
                            age=dataSnapshot.child("age").getValue(String.class);
                            use=dataSnapshot.child("use").getValue(String.class);
                            isAlive=dataSnapshot.child("isAlive").getValue(String.class);
                            req_approve=dataSnapshot.child("req_approve").getValue(String.class);
                            remark=dataSnapshot.child("Remark").getValue(String.class);
                    OwnerregionId = dataSnapshot.child("regionId").getValue(String.class);


                    binding.txtVwAnimalDetailBreed.setText("Breed: "+breed);
                    binding.txtVwAnimalDetailCategory.setText("Category: "+category);
                    binding.txtVwAnimalDetailDob.setText("D.O.B.: "+dob);
                    binding.txtVwAnimalDetailReqStatus.setText("Request Approved: "+req_approve);
                    binding.txtVwAnimalDetailWeight.setText("Weight: "+weight);
                    binding.txtVwAnimalDetailUse.setText("Use: "+use);

                    binding.txtVwAnimalDetailIsAlive.setText("Is Alive: "+isAlive);

                    if ((dataSnapshot.child("isAlive").getValue(String.class).equals("No")))
                    {
                        binding.btnAnimalDetailRegisterDeath.setEnabled(false);
                    }
                    else {
                        binding.btnAnimalDetailRegisterDeath.setEnabled(true);
                    }


                }

                else {

                    Toasty.error(MyAnimalDetails.this,"Error data not available!!", Toast.LENGTH_SHORT,true).show();
                }



                binding.btnAnimalDetailRegisterDeath.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // register animal death
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyAnimalDetails.this);
                        builder.setTitle("Animal Death Registration");
                        builder.setIcon(R.drawable.animal_icon);
                        builder.setMessage("Are you sure ? you want to register animal death ?");

                        // Set up the buttons
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                regAnimalDeath();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // on click sell animal

    }


    public void sellingMyAnimal(View view)
    {
      //data to send  category,breed,gender,weight,dob,age,use,isAlive,req_approve,remark;

        String  owner_main_id= mAuth.getCurrentUser().getUid();
        if (owner_main_id.equals(""))
        {

            Toasty.error(MyAnimalDetails.this,"error null user",Toast.LENGTH_SHORT,true).show();
        }
        else {
            Intent i = new Intent(MyAnimalDetails.this, SellScanUserID.class);
            i.putExtra("selling_animal_id", selectedAnimal_id);
            i.putExtra("owner_main_id",owner_main_id);
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
            finish();
        }
    }


    void regAnimalDeath()
    {


        reAnimalusr.child(selectedAnimal_id).child("isAlive").setValue("No");//update in

        //update in Animals
        regUser = rootnode.getReference().child("Animals").child("isAlive");
        regUser.setValue("No");

        // update in Requests
        regUser = rootnode.getReference().child("Requests").child("isAlive");
        regUser.setValue("No");

        Toasty.success(this,"animal death registered!",Toast.LENGTH_SHORT,true).show();


        //TODO: update life states everywhere
    }



    public void AnimalQRDlg(View view)
    {

     //   LayoutInflater inflater = LayoutInflater.from(MyAnimalDetails.this);
        qrDialog = new Dialog(MyAnimalDetails.this);
        qrDialog.setContentView(R.layout.show_animal_qr_dialog);
        qrDialog.setTitle("ID: "+selectedAnimal_id);

    // setting(Showing) QR
        QRGEncoder qrgEncoder = new QRGEncoder(selectedAnimal_id, QRGContents.Type.TEXT,1000);

        savegalary = (Button)qrDialog.findViewById(R.id.btn_animal_save_animal_QR);

        showQRAnimal= (ImageView)qrDialog.findViewById(R.id.ImgVw_custom_dialog_show_animal_qr);

        savegalary.setEnabled(true);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            globalQR=qrBits;
           showQRAnimal.setImageBitmap(qrBits);
        }catch (Exception es)
        {
            Toasty.error(MyAnimalDetails.this,""+es, Toast.LENGTH_SHORT,true).show();
        }

      //  ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.show_animal_qr_dialog, null);
       //  showQRAnimal = (ImageView) vg.findViewById(R.id.ImgVw_custom_dialog_show_animal_qr);

         qrDialog.show();

        savegalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String savePath = saveToInternalStorage(globalQR);
                      storeImage(globalQR);

                      //Toasty.success(MyAnimalDetails.this,"QR code saved at: "+savePath, Toast.LENGTH_SHORT,true).show();
                      Toasty.success(MyAnimalDetails.this,"QR code saved ", Toast.LENGTH_SHORT,true).show();
            }
        });

    }


   /* private String saveToInternalStorage(Bitmap bitmapImage){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir",Context.MODE_PRIVATE);

        File mypath=new File(directory,"QR code of :"+selectedAnimal_id);

//        File picdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File mypath=new File(picdir,"QR code of :"+selectedAnimal_id);
//        Uri uri = Uri.fromFile(mypath);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

*/

    /// new store image

    private void storeImage(Bitmap image) {

        String TAG =  "TESTSAVE";

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
// To be safe, you should check that the SDCard is mounted
// using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new
                File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

// This location works best if you want the created images to be shared
// between applications and persist after your app has been uninstalled.

// Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
// Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName= "QR"+selectedAnimal_id+""+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }





    private void requestForStorage() {


        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Toasty.error(MyAnimalDetails.this,"Storage Permission Required!!", Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();


    }



}
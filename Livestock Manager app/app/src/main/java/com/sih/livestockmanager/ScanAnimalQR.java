package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sih.livestockmanager.databinding.ActivityScanAnimalQRBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class ScanAnimalQR extends AppCompatActivity {

    ActivityScanAnimalQRBinding binding;
    CodeScanner scanner;
    String decoded_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityScanAnimalQRBinding.inflate(getLayoutInflater());
        View v =  binding.getRoot();
        setContentView(v);

        scanner = new CodeScanner(this,binding.QRCodeScannerView);


        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        decoded_id= result.getText().trim();

                        Intent i =new Intent(ScanAnimalQR.this,DisplayAnimalDetailsScanningQR.class);
                        i.putExtra("decoded_animal_id",decoded_id);
                        startActivity(i);
                        ScanAnimalQR.this.finish();
                    }
                });

            }
        });


        binding.QRCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanner.startPreview();
            }
        });


    }


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

              Toasty.error(ScanAnimalQR.this,"Camera Permission Required!!", Toast.LENGTH_SHORT,true).show();
          }

          @Override
          public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

              permissionToken.continuePermissionRequest();
          }
      }).check();


    }






}


package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih.livestockmanager.databinding.ActivityShowUserQRBinding;
import com.sih.livestockmanager.databinding.ActivityUserDashboardBinding;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.dmoral.toasty.Toasty;

public class ShowUserQR extends AppCompatActivity {

    ActivityShowUserQRBinding binding;
    private FirebaseAuth mAuth;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityShowUserQRBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        userid  = mAuth.getCurrentUser().getUid();

        binding.txtVwGenerateQRUserId.setText("My User Id: "+userid);


        binding.btnGenerateUserQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QRGEncoder qrgEncoder = new QRGEncoder(userid, QRGContents.Type.TEXT,1000);

                try {

                    Bitmap qrBits = qrgEncoder.getBitmap();
                    binding.imgVwOwnerQRGenerated.setImageBitmap(qrBits);

                }catch (Exception es)
                {

                    Toasty.error(ShowUserQR.this,""+es, Toast.LENGTH_SHORT,true).show();
                }

            }
        });

    }



}
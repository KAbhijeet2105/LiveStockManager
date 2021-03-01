package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sih.livestockmanager.databinding.ActivityMainBinding;


//This is Splash Screen Activity!

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=3000;

    ActivityMainBinding binding;
    Animation toplogo,bottomtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
         View v = binding.getRoot();
        setContentView(v);


        toplogo = AnimationUtils.loadAnimation(this,R.anim.splashtop_anim);
        bottomtext = AnimationUtils.loadAnimation(this,R.anim.splash_bottom);


         binding.ImgVwSplashLogo.setAnimation(toplogo);
         binding.txtSplashBottomTxt.setAnimation(bottomtext);


         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 Intent intent =  new Intent(MainActivity.this,LoginUser.class);

                 Pair[] pairs = new Pair[2];

                 Object first;
                 Object second;
                 pairs[0]= new Pair<View,String>(binding.ImgVwSplashLogo,"splashmain_logo");
                 pairs[1]= new Pair<View,String>(binding.txtSplashBottomTxt,"splash_text");

                 ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);

                 startActivity(intent,options.toBundle());
                finish();
             }
         },SPLASH_SCREEN);



    }
}
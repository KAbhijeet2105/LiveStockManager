package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityLoginUserBinding;
import com.sih.livestockmanager.databinding.ActivityMainBinding;

import es.dmoral.toasty.Toasty;

public class LoginUser extends AppCompatActivity {

    ActivityLoginUserBinding loginBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser;


   // SharedPreferences sharedPreferences = getSharedPreferences("UIDDATA",MODE_PRIVATE);

    // String usrid= sharedPreferences.getString("PHN","");

    //TODO: make progressdlg non cancelable
 //TODO: Write a method for checking admin privilages
 //TODO:  optimize the code optimize the lifecycle

    ProgressDialog prb_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rootnode = FirebaseDatabase.getInstance();
        Userrroot =rootnode.getReference().child("root").child("Users");
        regUser = FirebaseDatabase.getInstance().getReference("root").child("Users");
        prb_login = new ProgressDialog(this);
        //view binding
        loginBinding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        View logv = loginBinding.getRoot();
        setContentView(logv);



        //TODO: if user already logged in then directly login inside the app

        //for validation with phno
//        if (sharedPreferences.getString("PHN","").equals(""))
//        {
//            Intent i = new Intent(LoginUser.this,RegisterUser.class);
//            startActivity(i);
//            finish();
//        }


        if (mUser!=null && mUser.isEmailVerified())
        {


            Log.d("LOGGED IN","Task is in TODO List!!");

            prb_login.setMessage("Logging in...");
            prb_login.show();
            prb_login.setCancelable(false);

            //new code
            try {

                regUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {

                            String isAdmin = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("isAdmin").getValue(String.class);
                             Toast.makeText(getApplicationContext(),"status:-"+isAdmin,Toast.LENGTH_SHORT).show();

                            if (isAdmin.equals("Yes")) {
                                prb_login.dismiss();
                                Intent i = new Intent(getApplicationContext(),AdminDashboard.class);
                                startActivity(i);
                                finish();

                            } else if (isAdmin.equals("No")) {
                                prb_login.dismiss();
                                Intent i = new Intent(getApplicationContext(),UserDashboard.class);
                                startActivity(i);
                               // startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                                finish();
                            }
                        }catch (Exception es)
                        {
                            Intent i = new Intent(getApplicationContext(),RegisterUser.class);
                            startActivity(i);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }catch (Exception es)
            {
                Intent i = new Intent(getApplicationContext(),RegisterUser.class);
                startActivity(i);
            }

        }

        loginBinding.btnLoginLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  !validateEmail() | !validatePassword() )
                {
                    return;
                }

                login_old_user();


            }
        });



         loginBinding.txtVwLoginNewUser.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent goRegister= new Intent(LoginUser.this,RegisterUser.class);

                 Pair pairs[] = new Pair[7];


                   pairs[0]= new Pair<View,String>(loginBinding.ImgVwLoginLogo,"splashmain_logo");
                   pairs[1]= new Pair<View,String>(loginBinding.txtLoginWlcome,"splash_text");
                   pairs[2]= new Pair<View,String>(loginBinding.txtLoginSlogan,"register_usrname");
                   pairs[3]= new Pair<View,String>(loginBinding.edtxtLoginPasswd,"register_password");
                   pairs[4]= new Pair<View,String>(loginBinding.txtVwLoginForgetPasswd,"register_isadmin");
                   pairs[5]= new Pair<View,String>(loginBinding.btnLoginLoginbtn,"register_btnreg");
                   pairs[6]= new Pair<View,String>(loginBinding.txtVwLoginNewUser,"register_oldusr");


                 ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginUser.this,pairs);
                  startActivity(goRegister,options.toBundle());
                // finish();
             }
         });


    }



    // validations

    Boolean validateEmail()
    {
        String nmVal = loginBinding.edtxtLoginEmail.getEditText().getText().toString().trim();
        String mailPatern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (nmVal.isEmpty())
        {
            loginBinding.edtxtLoginEmail.setError("Field cannot be empty!");
            return false;
        }
        else if(!nmVal.matches(mailPatern)){

            loginBinding.edtxtLoginEmail.setError("Invalid Email address!");
            return false;
        }
        else {

            loginBinding.edtxtLoginEmail.setError(null);
            loginBinding.edtxtLoginEmail.setErrorEnabled(false);
            return true;
        }

    }


    Boolean validatePassword()
    {
        String nmVal = loginBinding.edtxtLoginPasswd.getEditText().getText().toString().trim();
        String passVal= "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})";
        //"^" +
        //   "(?=.*[0-9])" +          // at least one digit
        //  "(?=.*[a-z])" +            //at least one lowercase letter
        //    "(?=.*[A-Z])" +             //at least one Uppercase letter
        //       "(?=.*[a-zA-Z])" +          //any letters
        //       "(?=.*[@#$%^&+=])" +           // at least one special character
        //        "\\A\\w{4,20}\\z" +              // no white spaces
        //     ".{4,}"+                   //at least 4 characters
        //    "$";

        if (nmVal.isEmpty())
        {
            loginBinding.edtxtLoginPasswd.setError("Field cannot be empty!");
            return false;
        }
        else if(!nmVal.matches(passVal)){

            loginBinding.edtxtLoginPasswd.setError("password is too weak!");
            return false;
        }
        else {

            loginBinding.edtxtLoginPasswd.setError(null);
            loginBinding.edtxtLoginPasswd.setErrorEnabled(false);
            return true;
        }

    }


    public void login_old_user()
    {

        //login existing user
        String mail,pass;
        mail=loginBinding.edtxtLoginEmail.getEditText().getText().toString().trim();
        pass= loginBinding.edtxtLoginPasswd.getEditText().getText().toString().trim();

        prb_login.setMessage("Logging in...");
        prb_login.show();

        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(LoginUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {

                            if (mAuth.getCurrentUser().isEmailVerified()) {

                                prb_login.cancel();
                                Toasty.success(LoginUser.this,"Login successful!",Toast.LENGTH_SHORT,true).show();

                             ///////check admin yes or no
                                try {

                                    regUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            try {

                                                String isAdmin = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("isAdmin").getValue(String.class);
                                                Toast.makeText(getApplicationContext(),"status:-"+isAdmin,Toast.LENGTH_SHORT).show();

                                                if (isAdmin.equals("Yes")) {
                                                    prb_login.dismiss();
                                                    Intent i = new Intent(getApplicationContext(),AdminDashboard.class);
                                                    startActivity(i);
                                                    finish();

                                                } else if (isAdmin.equals("No")) {
                                                    prb_login.dismiss();
                                                    Intent i = new Intent(getApplicationContext(),UserDashboard.class);
                                                    startActivity(i);
                                                    // startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                                                    finish();
                                                }
                                            }catch (Exception es)
                                            {
                                                Intent i = new Intent(getApplicationContext(),RegisterUser.class);
                                                startActivity(i);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }catch (Exception es)
                                {
                                    Intent i = new Intent(getApplicationContext(),RegisterUser.class);
                                    startActivity(i);
                                }

//                                if (loginBinding.loginChkbxIsadmin.isChecked()) {
//
//
//
//                                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
//                                    finish();
//
//                                } else {
//
//
//
//                                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
//                                    finish();
//                                }
                            }
                            else {
                                prb_login.dismiss();
                                Toasty.error(LoginUser.this,"An error occurred! Please verify your email!",Toast.LENGTH_SHORT,true).show();


                            }


                        }
                        else {

                            prb_login.dismiss();
                            Toasty.error(LoginUser.this,"An error occurred! Please try again",Toast.LENGTH_SHORT,true).show();

                        }
                    }
                });


    }





}
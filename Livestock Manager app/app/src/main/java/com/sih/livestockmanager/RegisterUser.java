package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.livestockmanager.databinding.ActivityMainBinding;
import com.sih.livestockmanager.databinding.ActivityRegisterUserBinding;

import es.dmoral.toasty.Toasty;

public class RegisterUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityRegisterUserBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser;
    String usrArea="";
    String usrGender="";
    RadioButton selectedGender;
   // private DatabaseReference Adminroot,adminUser;

    //TODO: register admin with its aho_id rather than Uid

    ProgressDialog prb_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //view binding
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //area spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.areas,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerSpinnerUserArea.setAdapter(adapter);
        binding.registerSpinnerUserArea.setOnItemSelectedListener(this);

        // admin chech change
//
//        binding.registerChkbxIsadmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (b)
//                {
//                    binding.registerLayoutNoAdmin.setVisibility(View.GONE);
//                }
//                else{
//                    binding.registerLayoutNoAdmin.setVisibility(View.VISIBLE);
//
//                }
//            }
//        });



        rootnode = FirebaseDatabase.getInstance();

      //  Adminroot = rootnode.getReference().child("root").child("Admin");

        Userrroot =rootnode.getReference().child("root").child("Users");

        mAuth = FirebaseAuth.getInstance();
        prb_reg = new ProgressDialog(this);

        binding.btnRegisterRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( !validateName() | !validateEmail() | !validatePhoneNo() | !validatePassword() | !validateArea() | !validateAhoid() | !validateAadharNo() | !validategender())
                {
                    return;
                }
                 regNewUser();

            }
        });

        binding.txtVwRegisterOldusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RegisterUser.this,LoginUser.class);
                startActivity(i);
                finish();

            }
        });

    }


    //validations
    Boolean validateName()
    {
        String nmVal = binding.edtxtRegisterUsrFullname.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
          binding.edtxtRegisterUsrFullname.setError("Field cannot be empty!");
          return false;
        }
        else {

            binding.edtxtRegisterUsrFullname.setError(null);
            binding.edtxtRegisterUsrFullname.setErrorEnabled(false);
            return true;
        }

    }


    Boolean validateEmail()
    {
        String nmVal = binding.edtxtRegisterUsrEmail.getEditText().getText().toString().trim();
        String mailPatern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (nmVal.isEmpty())
        {
            binding.edtxtRegisterUsrEmail.setError("Field cannot be empty!");
            return false;
        }
        else if(!nmVal.matches(mailPatern)){

            binding.edtxtRegisterUsrEmail.setError("Invalid Email address!");
            return false;
        }
        else {

            binding.edtxtRegisterUsrEmail.setError(null);
            binding.edtxtRegisterUsrEmail.setErrorEnabled(false);
            return true;
        }

    }


    Boolean validatePhoneNo()
    {
        String nmVal = binding.edtxtRegisterUsrPhoneNo.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtRegisterUsrPhoneNo.setError("Field cannot be empty!");
            return false;
        }
        else if (nmVal.length()<10)
        {
            binding.edtxtRegisterUsrPhoneNo.setError("Invalid phone number!");
            return false;

        }
        else {

            binding.edtxtRegisterUsrPhoneNo.setError(null);
            binding.edtxtRegisterUsrPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validatePassword()
    {
        String nmVal = binding.edtxtRegisterPasswd.getEditText().getText().toString().trim();
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
            binding.edtxtRegisterPasswd.setError("Field cannot be empty!");
            return false;
        }
        else if(!nmVal.matches(passVal)){

            binding.edtxtRegisterPasswd.setError("password is too weak!");
            return false;
        }
        else {

            binding.edtxtRegisterPasswd.setError(null);
            binding.edtxtRegisterPasswd.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateAadharNo()
    {
            String nmVal = binding.edtxtRegisterUsrAadhar.getEditText().getText().toString().trim();
            if (nmVal.isEmpty()) {
                binding.edtxtRegisterUsrAadhar.setError("Field cannot be empty!");
                return false;
            } else if (nmVal.length() != 12) {
                binding.edtxtRegisterUsrAadhar.setError("Invalid aadhar number!");
                return false;

            } else {

                binding.edtxtRegisterUsrAadhar.setError(null);
                binding.edtxtRegisterUsrAadhar.setErrorEnabled(false);
                return true;
            }


    }

    Boolean validateAhoid()
    {
        String nmVal = binding.edtxtRegisterUsrAhoId.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtRegisterUsrAhoId.setError("Field cannot be empty!");
            return false;
        }
        else {

            binding.edtxtRegisterUsrAhoId.setError(null);
            binding.edtxtRegisterUsrAhoId.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateArea()
    {
        int areaPos = binding.registerSpinnerUserArea.getSelectedItemPosition();
        if (areaPos==0)
        {
            Toasty.warning(getApplicationContext(),"Please select your Area!",Toast.LENGTH_SHORT,true).show();
            return false;
        }
        else {


            return true;
        }

    }

    Boolean validategender()
    {



            if (!binding.registerUserRdbBtnMale.isChecked() && !binding.registerUserRdbBtnFemale.isChecked()) {
                Toasty.warning(getApplicationContext(), "Please select your gender!", Toast.LENGTH_SHORT, true).show();
                return false;
            } else {

                return true;
            }


    }



    //registering new user

    public void regNewUser()
    {
        String mail,pass;
        mail=binding.edtxtRegisterUsrEmail.getEditText().getText().toString().trim();
        pass= binding.edtxtRegisterPasswd.getEditText().getText().toString().trim();

        prb_reg.setMessage("registering user...");
        prb_reg.show();
        prb_reg.setCancelable(false);

        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success

                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {

                                Toasty.success(RegisterUser.this,"user registered successfully! please verify your email",Toast.LENGTH_SHORT,true).show();
                                prb_reg.dismiss();

                                   storeRegUser();

                                //TODO: set intent to automatically ridirect at  login scrn

                                Intent i = new Intent(RegisterUser.this,LoginUser.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                prb_reg.dismiss();
                                Toasty.error(RegisterUser.this,"unable to send email!",Toast.LENGTH_SHORT,true).show();

                            }
                        }
                    });

                } else {
                    // If sign in fails, display a message to the user.
                    prb_reg.dismiss();
                    Toasty.error(RegisterUser.this,"user registration failed!",Toast.LENGTH_SHORT,true).show();
                }
            }
        });


    }


//  storing user data to the firebase database

    void storeRegUser()
    {

      String usernm,email,phoneNo,isAdmin,area,aho_id,aadhar_no,gender;;

     SharedPreferences sharedPreferences =  getSharedPreferences("UIDDATA",MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPreferences.edit();


                        usernm   =   binding.edtxtRegisterUsrFullname.getEditText().getText().toString();
                           email =    binding.edtxtRegisterUsrEmail.getEditText().getText().toString();
                           phoneNo = binding.edtxtRegisterUsrPhoneNo.getEditText().getText().toString();

                           area = usrArea;

                           aho_id = binding.edtxtRegisterUsrAhoId.getEditText().getText().toString();

                           aadhar_no = binding.edtxtRegisterUsrAadhar.getEditText().getText().toString();
                           gender=usrGender;


                             if (binding.registerChkbxIsadmin.isChecked())
                             {
                                 isAdmin ="Yes";

                             }
                             else {
                                 isAdmin ="No";
                             }


                              editor.putString("PHN",phoneNo);
                             editor.putString("AHOID",aho_id);


                             editor.apply();

                              UserHelper usrhelper = new UserHelper(mAuth.getCurrentUser().getUid(),usernm,email,phoneNo,isAdmin,area,aho_id,aadhar_no,gender);



//
//                       if (isAdmin=="Yes")
//                            {
//
//                                regUser = Userrroot.getRef().child(phoneNo);
//                                regUser.setValue(usrhelper);
//
//                           }
//                             else{
//
//                                 regUser = Userrroot.getRef().child(phoneNo);
//                                      regUser.setValue(usrhelper);
//
//                             }


        regUser = Userrroot.getRef().child(mAuth.getCurrentUser().getUid());
        regUser.setValue(usrhelper);




        // clear the fields
                             binding.edtxtRegisterUsrEmail.getEditText().setText("");
                              binding.edtxtRegisterPasswd.getEditText().setText("");
                               binding.edtxtRegisterUsrFullname.getEditText().setText("");
                             binding.edtxtRegisterUsrPhoneNo.getEditText().setText("");
                             binding.edtxtRegisterUsrAhoId.getEditText().setText("");
                            binding.edtxtRegisterUsrAadhar.getEditText().setText("");
                            binding.registerUserRdbBtnMale.setChecked(false);
                            binding.registerUserRdbBtnFemale.setChecked(false);
                            binding.registerSpinnerUserArea.setSelection(0);

    }


    public void genderSelect(View view)
    {
        int radioId = binding.registerUserRdbGrpSex.getCheckedRadioButtonId();

        selectedGender = findViewById(radioId);

        usrGender = selectedGender.getText().toString();

    //    Toast.makeText(this,"Gender :"+usrGender,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        usrArea=   adapterView.getItemAtPosition(i).toString().trim();

      //  Toast.makeText(getApplicationContext(),"area="+usrArea,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
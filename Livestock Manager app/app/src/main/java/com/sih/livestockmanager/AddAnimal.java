package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityAddAnimalBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class AddAnimal extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    ActivityAddAnimalBinding binding;
    int animal_count;

    String ownerid,ownernm,ownerphn,animalId,category,breed,gender,weight,dob,age,use,regionId,isAlive,req_approve;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser,regAnimal,reAnimalusr,reRequestAnimal,Arearef;
   // String cate="";
    String animalGender="";
    String animal_age;
    String animal_DOB;
    RadioButton selectedGender;
    ProgressDialog prb_reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityAddAnimalBinding.inflate(getLayoutInflater());
        View v =binding.getRoot();
        setContentView(v);

      //category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addAnimalSpinnerAnimalCategory.setAdapter(adapter);
        binding.addAnimalSpinnerAnimalCategory.setOnItemSelectedListener(this);

        // firebase references
        regUser = FirebaseDatabase.getInstance().getReference("root").child("Users");
        rootnode = FirebaseDatabase.getInstance();
        Userrroot =rootnode.getReference().child("root").child("Users");
        mAuth = FirebaseAuth.getInstance();
        prb_reg = new ProgressDialog(this);



        binding.btnAddAnimalAddanimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //!validateDOB() | is disabled for temp.

                if ( !validateCategory() | !validategender() | !validateBreed() |  !validateUse() | !validateWeight())
                {
                    return;
                }

                collectAnimaldata();

               // Toast.makeText(AddAnimal.this, "animal will added !", Toast.LENGTH_SHORT).show();

            }
        });

        binding.txtVwAddAnimalDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"Date Picker");

            }
        });

    }

//validations

    Boolean validateDOB()
    {

        if (!binding.txtVwAddAnimalDOB.getText().toString().trim().equals(R.string.add_animal_select_dob))
        {
            Toasty.warning(getApplicationContext(),"Please select animal date of birth!",Toast.LENGTH_SHORT,true).show();
            return false;
        }
        else {

            return true;
        }

    }

    Boolean validateUse()
    {
        String nmVal = binding.edtxtAddAnimalUse.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtAddAnimalUse.setError("Field cannot be empty!");
            return false;
        }
        else {

            binding.edtxtAddAnimalUse.setError(null);
            binding.edtxtAddAnimalUse.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateWeight()
    {
        String nmVal = binding.edtxtAddAnimalWeight.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtAddAnimalWeight.setError("Field cannot be empty!");
            return false;
        }
        else {

            binding.edtxtAddAnimalWeight.setError(null);
            binding.edtxtAddAnimalWeight.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateBreed()
    {
        String nmVal = binding.edtxtAddAnimalBreed.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtAddAnimalBreed.setError("Field cannot be empty!");
            return false;
        }
        else {

            binding.edtxtAddAnimalBreed.setError(null);
            binding.edtxtAddAnimalBreed.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateCategory()
    {
        int catPos = binding.addAnimalSpinnerAnimalCategory.getSelectedItemPosition();
        if (catPos==0)
        {
            Toasty.warning(getApplicationContext(),"Please select animal category!",Toast.LENGTH_SHORT,true).show();
            return false;
        }
        else {
            return true;
        }

    }

    Boolean validategender()
    {
        if (!binding.addAnimalRdbBtnMale.isChecked() && !binding.addAnimalRdbBtnFemale.isChecked()) {
            Toasty.warning(getApplicationContext(), "Please animal gender!", Toast.LENGTH_SHORT, true).show();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

       category =   adapterView.getItemAtPosition(i).toString().trim();

       Toast.makeText(getApplicationContext(),"category="+category,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void animalGenderSelect(View view)
    {
        int radioId = binding.addAnimalRdbGrpSex.getCheckedRadioButtonId();
        selectedGender = findViewById(radioId);
        animalGender = selectedGender.getText().toString();
        Toast.makeText(this,"Gender :"+animalGender,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);

        animal_age= getAge(year,month,day);
        month = month+1;
       // animal_DOB = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        animal_DOB = ""+day+"/"+month+"/"+year;


        Toasty.info(AddAnimal.this,"animal Age="+animal_age,Toast.LENGTH_SHORT,true).show();


        binding.txtVwAddAnimalDOB.setText(""+animal_DOB);

    }

//calculate age
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


//    public void genderSelect(View view)
//    {
//        int radioId = binding.addAnimalRdbGrpSex.getCheckedRadioButtonId();
//
//        selectedGender = findViewById(radioId);
//
//       gender = selectedGender.getText().toString();
//
//        //    Toast.makeText(this,"Gender :"+usrGender,Toast.LENGTH_SHORT).show();
//    }



    ///Collect all data

    void collectAnimaldata()
    {


        prb_reg.setMessage("registering Animal...");
        prb_reg.show();
        prb_reg.setCancelable(false);


                  ownerid = mAuth.getCurrentUser().getUid();
                  animalId= UUID.randomUUID().toString();
                  breed = binding.edtxtAddAnimalBreed.getEditText().getText().toString().trim();


                  weight=binding.edtxtAddAnimalWeight.getEditText().getText().toString().trim();
                  dob=animal_DOB;
                  age=animal_age;
                  use= binding.edtxtAddAnimalUse.getEditText().getText().toString().trim();

                  if (binding.registerChkbxIsalive.isChecked())
                  {
                      isAlive="Yes";
                  }
                  else {
                      isAlive="No";
                  }
                  req_approve="No";

        // gather users data


            regUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {

                        ownernm= dataSnapshot.child(ownerid).child("username").getValue(String.class);
                        ownerphn=dataSnapshot.child(ownerid).child("phoneno").getValue(String.class);
                          regionId= dataSnapshot.child(ownerid).child("area").getValue(String.class);

                          addAnimal();

                    }catch (Exception es)
                    {

                        Toast.makeText(getApplicationContext(),"Exception "+es,Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

        void addAnimal()
        {
            gender = animalGender;


            AnimalHelper animalHelper = new AnimalHelper(ownerid,ownernm,ownerphn,animalId,category,breed,gender,weight,dob,age,use,regionId,isAlive,req_approve);


            reAnimalusr= Userrroot.getRef().child(mAuth.getCurrentUser().getUid()).child("Animal").child(animalId);
            reAnimalusr.setValue(animalHelper);

            regAnimal = rootnode.getReference().child("Animals").child(animalId);
            regAnimal.setValue(animalHelper);

            reRequestAnimal = rootnode.getReference().child("Requests").child(animalId);
            reRequestAnimal.setValue(animalHelper);


            //adding data for statistics
            Arearef = rootnode.getReference().child("AreaStats").child(regionId);
            Arearef.child("regionId").setValue(regionId);

            Arearef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      String animal;

                        animal = dataSnapshot.child("Animal_count").getValue(String.class);

                        if (animal.equals(null))
                        {
                            animal_count =1;

                        }
                        else
                        {
                            animal_count = (Integer.parseInt(animal));
                            animal_count = animal_count+1;
                           // Arearef.child("Animal_count").setValue(""+animal_count);
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Arearef.child("Animal_count").setValue(""+animal_count);






            prb_reg.dismiss();

             Toasty.success(AddAnimal.this,"Animal data added!!",Toasty.LENGTH_SHORT,true).show();

             //clear prev data
             binding.addAnimalSpinnerAnimalCategory.setSelection(0);
             binding.edtxtAddAnimalBreed.getEditText().setText("");
             binding.addAnimalRdbBtnMale.setChecked(false);
             binding.addAnimalRdbBtnFemale.setChecked(false);
             binding.txtVwAddAnimalDOB.setText(R.string.add_animal_select_dob);
             binding.edtxtAddAnimalUse.getEditText().setText("");
             binding.edtxtAddAnimalWeight.getEditText().setText("");
             binding.registerChkbxIsalive.setChecked(true);





        }

}

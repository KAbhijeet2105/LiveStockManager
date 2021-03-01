package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivitySellAnimalBinding;

import es.dmoral.toasty.Toasty;

public class SellAnimal extends AppCompatActivity {

    ActivitySellAnimalBinding binding;

    String cust_id="",customernm,custPhoneno,custRegion,selling_animal_id;
    String ownerid,category,breed,gender,weight,dob,age,use,isAlive,req_approve,remark,OwnerregionId;
    int owner_animal_count,customer_animal_count;

    FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference customerRef,myRef,globalRef,Arearef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySellAnimalBinding.inflate(getLayoutInflater());
        View  v = binding.getRoot();
        setContentView(v);

        //customer id
        cust_id = getIntent().getStringExtra("cust_id");
        customernm =getIntent().getStringExtra("customernm");
        custPhoneno =getIntent().getStringExtra("custPhoneno");
        custRegion =getIntent().getStringExtra("custRegion");
        selling_animal_id= getIntent().getStringExtra("selling_animal_id");

        //collect animal data

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


        mAuth = FirebaseAuth.getInstance();
        ownerid= mAuth.getCurrentUser().getUid();


        rootnode  = FirebaseDatabase.getInstance();


        //Sell Animal
        binding.btnAnimalSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sellAnimal();
            }
        });



        // cancel the process
        binding.btnAnimalDontSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(SellAnimal.this,"Transaction Canceled!", Toast.LENGTH_SHORT,true).show();
                Intent i = new Intent(SellAnimal.this,UserDashboard.class);
                startActivity(i);
                finish();
            }
        });


    }


    void sellAnimal(){

        // Store animal in customer database

        AnimalHelper animalHelper = new AnimalHelper(cust_id,customernm,custPhoneno,selling_animal_id,category,breed,gender,weight,dob,age,use,custRegion,isAlive,req_approve);

        customerRef = rootnode.getReference().child("root").child("Users").child(cust_id).child("Animal");
        customerRef.child(selling_animal_id).setValue(animalHelper);
        customerRef.child(selling_animal_id).child("Remark").setValue(remark);

        //// update globally in requests and Animals

        // in Animals
        globalRef = rootnode.getReference().child("Animals").child(selling_animal_id);
        globalRef.setValue(animalHelper);
        globalRef.child("Remark").setValue(remark);

       // Toasty.success(SellAnimal.this,"Transaction med stage before global update",Toast.LENGTH_SHORT,true).show();

        // in Requests

        globalRef = rootnode.getReference().child("Requests").child(selling_animal_id);
        globalRef.setValue(animalHelper);
        globalRef.child("Remark").setValue(remark);


        //Toasty.success(SellAnimal.this,"Transaction success before del!",Toast.LENGTH_SHORT,true).show();


                 del_owner_animal();

    }


    void del_owner_animal()
    {
        //selling_animal_id = animalId;
try {
   // myRef = rootnode.getReference().child("root").child("Users").child(mAuth.getCurrentUser().getUid()).child("Animal").child(selling_animal_id);
   // myRef.setValue(null);
    // updateAreastats();
   // Toasty.success(SellAnimal.this, "Transaction successful!", Toast.LENGTH_SHORT, true).show();

    myRef = rootnode.getReference().child("root").child("Users").child(mAuth.getCurrentUser().getUid()).child("Animal").child(selling_animal_id);


    myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            updateAreastats();
            Toasty.success(SellAnimal.this,"Transaction successful!",Toast.LENGTH_SHORT,true).show();

            Intent i = new Intent(SellAnimal.this,UserDashboard.class);
            startActivity(i);
            SellAnimal.this.finish();
        }
    });



}catch (Exception es)
{

}finally {
    Intent i = new Intent(SellAnimal.this,UserDashboard.class);
    startActivity(i);
     SellAnimal.this.finish();
  }




    }

    //update area stats

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

        Arearef.child("regionId:").setValue(custRegion);
        Arearef.child("Animal_count").setValue(""+customer_animal_count);

    }

}
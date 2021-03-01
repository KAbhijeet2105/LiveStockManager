package com.sih.livestockmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.livestockmanager.databinding.ActivityAdminRequestDetailBinding;
import com.sih.livestockmanager.databinding.ActivityMyAnimalDetailsBinding;

import es.dmoral.toasty.Toasty;

public class AdminRequestDetail extends AppCompatActivity {

    ActivityAdminRequestDetailBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser,reAnimalusr;
    private DatabaseReference regAnimal,reRequestAnimal;

    RadioButton SelectedreqState;
    ProgressDialog prb_reg;

    String selectedReq_id="null",selectedReqState="",selectedUser="";

//TODO: complete layout , remark approve/reject request, update request status all over


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityAdminRequestDetailBinding.inflate(getLayoutInflater());
        View v =binding.getRoot();
        setContentView(v);

        mAuth =FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        Userrroot =rootnode.getReference().child("root").child("Users");

        reAnimalusr = rootnode.getReference().child("Requests");

        selectedReq_id = getIntent().getStringExtra("selected_req_id");

        binding.txtVwRequestAdminAnimalId.setText("Animal ID: "+selectedReq_id);
        prb_reg = new ProgressDialog(this);



        //fetch req details

        reAnimalusr.child(selectedReq_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    binding.txtVwAdminRequestBreed.setText("Breed: "+dataSnapshot.child("breed").getValue(String.class));
                    binding.txtVwAdminRequestCategory.setText("Category: "+dataSnapshot.child("category").getValue(String.class));
                    binding.txtVwAdminRequestDob.setText("D.O.B.: "+dataSnapshot.child("dob").getValue(String.class));
                    binding.txtVwAdminRequestReqStatus.setText("Request Approved:  "+dataSnapshot.child("req_approve").getValue(String.class));

                    binding.txtVwAdminRequestWeight.setText("Weight: "+dataSnapshot.child("weight").getValue(String.class));
                    binding.txtVwAdminRequestUse.setText("Use: "+dataSnapshot.child("use").getValue(String.class));
                    binding.txtVwRequestAdminUserId.setText("User ID:"+dataSnapshot.child("ownerid").getValue(String.class));
                    binding.txtVwRequestAdminUserName.setText("User Name:"+dataSnapshot.child("ownernm").getValue(String.class));
                    selectedUser=dataSnapshot.child("ownerid").getValue(String.class);

                    binding.txtVwRequestAdminUserPhoneNo.setText("Phone Number"+dataSnapshot.child("ownerphn").getValue(String.class));
                    binding.txtVwRequestAdminAnimalAge.setText("Animal Age: "+dataSnapshot.child("age").getValue(String.class));
                    binding.txtVwRequestAdminUserRegion.setText("Region: "+dataSnapshot.child("regionId").getValue(String.class));
                    binding.txtVwAdminRequestIsAlive.setText("Is Alive: "+dataSnapshot.child("isAlive").getValue(String.class));

                } else {

                    Toasty.error(AdminRequestDetail.this, "Error data not available!!", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //// update request status

        binding.btnAdminDetailReqReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateRemark() | !validateReqState())
                {
                    return;
                }

                // register animal death
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminRequestDetail.this);
                builder.setTitle("Request Review");
                builder.setIcon(R.drawable.animal_icon);
                builder.setMessage("Are you sure ? you want to submit evaluation?");

                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prb_reg.setMessage("submitting evaluation...");
                        prb_reg.show();
                        prb_reg.setCancelable(false);
                       addRemark();
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


            //validations

    Boolean validateRemark()
    {
        String nmVal = binding.edtxtAdminDetailRemark.getEditText().getText().toString().trim();
        if (nmVal.isEmpty())
        {
            binding.edtxtAdminDetailRemark.setError("Field cannot be empty!");
            return false;
        }
        else {

            binding.edtxtAdminDetailRemark.setError(null);
            binding.edtxtAdminDetailRemark.setErrorEnabled(false);
            return true;
        }

    }

    Boolean validateReqState()
    {
        if (!binding.adminReqDetailRdbBtnApprove.isChecked() && !binding.adminReqDetailRdbBtnReject.isChecked()) {
            Toasty.warning(getApplicationContext(), "Please approve or reject request!", Toast.LENGTH_SHORT, true).show();
            return false;
        } else {
            return true;
        }
    }


    public void ReqStateSelect(View view)
    {
        int radioId = binding.rdbtnGrpAdminDetailReqChk.getCheckedRadioButtonId();
        SelectedreqState = findViewById(radioId);

        if (SelectedreqState==binding.adminReqDetailRdbBtnApprove)
        {
            selectedReqState="Yes";

        }
        else if (SelectedreqState==binding.adminReqDetailRdbBtnReject)
        {
            selectedReqState="No";
        }

        //selectedReqState = SelectedreqState.getText().toString();
        Toasty.success(this,"Request state :"+selectedReqState,Toast.LENGTH_SHORT,true).show();
    }


            void addRemark()
            {

                reAnimalusr= rootnode.getReference().child("Requests").child(selectedReq_id).child("req_approve");  // Requests
                reAnimalusr.setValue(selectedReqState);// update req states in in requests
                // also add remark
                reAnimalusr= rootnode.getReference().child("Requests").child(selectedReq_id).child("Remark");
                reAnimalusr.setValue(binding.edtxtAdminDetailRemark.getEditText().getText().toString().trim());

                // Update in Animals
                regAnimal = rootnode.getReference().child("Animals").child(selectedReq_id).child("req_approve");
                regAnimal.setValue(selectedReqState);
                //add remark
                regAnimal = rootnode.getReference().child("Animals").child(selectedReq_id).child("Remark");
                regAnimal.setValue(binding.edtxtAdminDetailRemark.getEditText().getText().toString().trim());

                // Update in  user account

                regUser = rootnode.getReference().child("root").child("Users").child(selectedUser).child("Animal").child(selectedReq_id).child("req_approve");
                regUser.setValue(selectedReqState);
                //add remark
                regUser = rootnode.getReference().child("root").child("Users").child(selectedUser).child("Animal").child(selectedReq_id).child("Remark");
                regUser.setValue(binding.edtxtAdminDetailRemark.getEditText().getText().toString().trim());

                prb_reg.dismiss();

                Toasty.success(AdminRequestDetail.this,"Evaluation submitted!!",Toasty.LENGTH_SHORT,true).show();

                //TODO: clear the fields

            }

}
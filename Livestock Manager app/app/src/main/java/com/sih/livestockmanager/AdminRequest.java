package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sih.livestockmanager.databinding.ActivityAdminRequestBinding;
import com.sih.livestockmanager.databinding.ActivityMyAnimalsBinding;

public class AdminRequest extends AppCompatActivity implements OnItemSelectedListener {

    ActivityAdminRequestBinding binding;
    //RecyclerView animalList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference reRequests;
    private RequestListAdapter adapter;
    FirebaseRecyclerOptions<AnimalHelper> options;
    Query reqQuery;
    String reqFilter="";

   // AnimalListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityAdminRequestBinding.inflate(getLayoutInflater());
        View v =binding.getRoot();
        setContentView(v);


        mAuth =FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();


        ArrayAdapter<CharSequence> reqAdapter = ArrayAdapter.createFromResource(this,R.array.req_status,android.R.layout.simple_spinner_item);
        reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.adminReqSpinnerReqStatus.setAdapter(reqAdapter);
        binding.adminReqSpinnerReqStatus.setOnItemSelectedListener(this);

        binding.recyclerVwAdminRequestList.setHasFixedSize(true);
        binding.recyclerVwAdminRequestList.setLayoutManager(new LinearLayoutManager(this));


        reRequests = rootnode.getReference().child("Requests");

        reqQuery = reRequests;


        options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();


        adapter = new RequestListAdapter(options);
        //binding.recyclerVwAdminRequestList.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

       reqFilter  =   adapterView.getItemAtPosition(i).toString().trim();

       Toast.makeText(AdminRequest.this,"Filter: "+reqFilter,Toast.LENGTH_SHORT).show();
       setListData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void setListData()
    {

        if (reqFilter.equals("Approved"))
        {
            reqQuery = reRequests.orderByChild("req_approve").equalTo("Yes");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwAdminRequestList.setAdapter(adapter);

        }
        else if (reqFilter.equals("Not Approved"))
        {
            reqQuery = reRequests.orderByChild("req_approve").equalTo("No");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwAdminRequestList.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
        else {
            reqQuery= reRequests;
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwAdminRequestList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }



}
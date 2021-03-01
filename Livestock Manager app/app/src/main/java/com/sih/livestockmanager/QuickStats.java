package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sih.livestockmanager.databinding.ActivityQuickStatsBinding;

public class QuickStats extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityQuickStatsBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference reRequests;
    private RequestListAdapter adapter;
    FirebaseRecyclerOptions<AnimalHelper> options;
    Query reqQuery;
    String categoryFilter="",liveFilter="",genderFilter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding =  ActivityQuickStatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        mAuth =FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();


        ArrayAdapter<CharSequence> reqAdapter = ArrayAdapter.createFromResource(this,R.array.categories,android.R.layout.simple_spinner_item);
        reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.quickStatsSpinnerAnimalsCategories.setAdapter(reqAdapter);
        binding.quickStatsSpinnerAnimalsCategories.setOnItemSelectedListener(this);



        binding.recyclerVwQuickStatsList.setHasFixedSize(true);
        binding.recyclerVwQuickStatsList.setLayoutManager(new LinearLayoutManager(this));


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

        categoryFilter  =   adapterView.getItemAtPosition(i).toString().trim();

        Toast.makeText(QuickStats.this,"Filter: "+categoryFilter,Toast.LENGTH_SHORT).show();
        setListData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    void setListData()
    {

        if (categoryFilter.equals("Cow"))
        {
            reqQuery = reRequests.orderByChild("category").equalTo("Cow");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);

        }
        else if (categoryFilter.equals("Buffalo"))
        {
            reqQuery = reRequests.orderByChild("category").equalTo("Buffalo");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
        else if (categoryFilter.equals("Goat"))
        {
            reqQuery = reRequests.orderByChild("category").equalTo("Goat");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }

        else if (categoryFilter.equals("Sheep"))
        {
            reqQuery = reRequests.orderByChild("category").equalTo("Sheep");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
        else if (categoryFilter.equals("Bull"))
        {
            reqQuery = reRequests.orderByChild("category").equalTo("Bull");
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        else {
            reqQuery= reRequests;
            options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reqQuery,AnimalHelper.class).build();
            adapter.updateOptions(options);
            binding.recyclerVwQuickStatsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }

}
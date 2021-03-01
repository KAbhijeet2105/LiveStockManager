package com.sih.livestockmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.livestockmanager.databinding.ActivityAddAnimalBinding;
import com.sih.livestockmanager.databinding.ActivityMyAnimalsBinding;

public class MyAnimals extends AppCompatActivity {

    ActivityMyAnimalsBinding binding;
    //RecyclerView animalList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference Userrroot,regUser,reAnimalusr;

    AnimalListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityMyAnimalsBinding.inflate(getLayoutInflater());
        View v =binding.getRoot();
        setContentView(v);

        mAuth =FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        Userrroot =rootnode.getReference().child("root").child("Users");
        reAnimalusr = Userrroot.child(mAuth.getCurrentUser().getUid()).child("Animal");
          //reAnimalusr.keepSynced(true);


        binding.recyclerVwMyanimalsList.setHasFixedSize(true);
        binding.recyclerVwMyanimalsList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AnimalHelper> options = new FirebaseRecyclerOptions.Builder<AnimalHelper>().setQuery(reAnimalusr,AnimalHelper.class).build();

        adapter = new AnimalListAdapter(options);

         binding.recyclerVwMyanimalsList.setAdapter(adapter);


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
}
package com.sih.livestockmanager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AnimalListAdapter extends FirebaseRecyclerAdapter<AnimalHelper, AnimalListAdapter.AnimalListViewHolder> {

    Context context;
    public AnimalListAdapter(@NonNull FirebaseRecyclerOptions<AnimalHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnimalListViewHolder holder, final int position, @NonNull final AnimalHelper model) {

        //context = context.getApplicationContext();


        context = holder.itemView.getContext();

        holder.animal_id.setText("ID: "+model.getAnimalId());
        holder.animal_category.setText("Category: "+model.getCategory());
        holder.animal_breed.setText("Breed: "+model.getBreed());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =new Intent(context,MyAnimalDetails.class);
                i.putExtra("selected_animal_id", getRef(position).getKey());
                context.startActivity(i);

            }
        });



    }

    @NonNull
    @Override
    public AnimalListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_animals_list_card, parent, false);

        return new AnimalListViewHolder(view);
    }

    class AnimalListViewHolder extends RecyclerView.ViewHolder{

      TextView animal_id,animal_breed,animal_category;


        public AnimalListViewHolder(@NonNull View itemView) {
            super(itemView);

            animal_id =  itemView.findViewById(R.id.my_animal_card_animal_id);
            animal_category = itemView.findViewById(R.id.my_animal_card_animal_category);
            animal_breed = itemView.findViewById(R.id.my_animal_card_animal_breed);


        }
    }

}

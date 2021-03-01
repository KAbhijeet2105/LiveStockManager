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

public class RequestListAdapter extends FirebaseRecyclerAdapter<AnimalHelper, RequestListAdapter.RequestListViewHolder>
 {

     Context context;

     public RequestListAdapter(@NonNull FirebaseRecyclerOptions<AnimalHelper> options) {
         super(options);
     }

     @Override
     protected void onBindViewHolder(@NonNull RequestListViewHolder holder, final int position, @NonNull AnimalHelper model) {

         context = holder.itemView.getContext();

         holder.animal_id.setText(model.getAnimalId());
         holder.username.setText(model.getOwnernm());
         holder.category.setText(model.getCategory());
         holder.request_status.setText(model.getReq_approve());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent i =new Intent(context,AdminRequestDetail.class);
                 i.putExtra("selected_req_id", getRef(position).getKey());
                 context.startActivity(i);
             }
         });

     }

     @NonNull
     @Override
     public RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_request_list_card, parent, false);

         return new RequestListViewHolder(view);

     }

     class RequestListViewHolder extends RecyclerView.ViewHolder {

         TextView  animal_id,username,category,request_status;

      public RequestListViewHolder(@NonNull View itemView) {
          super(itemView);

          animal_id= itemView.findViewById(R.id.txtVw_adminrequest_card_animal_id);
          username = itemView.findViewById(R.id.txtVw_adminrequest_card_animal_usernm);
          category = itemView.findViewById(R.id.txtVw_adminrequest_card_animal_category);
          request_status= itemView.findViewById(R.id.txtVw_adminrequest_card_animal_reqApprove);


      }

  }



}

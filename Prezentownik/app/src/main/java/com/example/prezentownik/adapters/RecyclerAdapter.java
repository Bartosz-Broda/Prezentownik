package com.example.prezentownik.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prezentownik.R;
import com.example.prezentownik.models.Person;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";

    private List<Person> mPersons;
    private Context mContext;

    public RecyclerAdapter(Context mContext, List<Person> persons) {
        this.mPersons = persons;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        // Set the name of the Person
        holder.name.setText(mPersons.get(position).getName());

        // Set the text of gift quantity
        holder.giftQuantity.setText(mPersons.get(position).getGiftQuantity());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on something");

                Toast.makeText(mContext, (CharSequence) mPersons.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         private TextView name;
         private TextView giftQuantity;
         RelativeLayout parentLayout;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);

             name = itemView.findViewById(R.id.person_name);
             giftQuantity = itemView.findViewById(R.id.gift_quantity);

             parentLayout = itemView.findViewById(R.id.parent_layout);


         }
     }

}

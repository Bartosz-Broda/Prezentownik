package com.example.prezentownik.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prezentownik.GiftsActivity;
import com.example.prezentownik.MainActivity;
import com.example.prezentownik.R;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";

    private List<Person> mPersons;
    private String listName;
    private Context mContext;

    public RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPersonModels(List<Person> mPersons){
        this.mPersons = mPersons;
    }

    public void setListName (String listName) {this.listName = listName;}

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
                String value = mPersons.get(position).getName();
                Log.d(TAG, "onClick: clicked on something " + mPersons.get(position).getName());
                Intent myIntent = new Intent(mContext, GiftsActivity.class);
                myIntent.putExtra("key", value);
                myIntent.putExtra("key2", listName);//Optional parameters
                mContext.startActivity(myIntent);

                Toast.makeText(mContext, String.valueOf(mPersons.get(position)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mPersons == null){
            return 0;
        } else {
            return mPersons.size();
        }
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

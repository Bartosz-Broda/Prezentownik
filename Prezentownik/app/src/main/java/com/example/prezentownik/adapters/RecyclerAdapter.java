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

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mGiftQuantity = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mGiftQuantity) {
        this.mNames = mNames;
        this.mGiftQuantity = mGiftQuantity;
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

        holder.name.setText(mNames.get(position));
        holder.giftQuantity.setText(mGiftQuantity.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on something");

                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView name;
         TextView giftQuantity;
         RelativeLayout parentLayout;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);

             name = itemView.findViewById(R.id.person_name);
             giftQuantity = itemView.findViewById(R.id.gift_quantity);

             parentLayout = itemView.findViewById(R.id.parent_layout);


         }
     }

}

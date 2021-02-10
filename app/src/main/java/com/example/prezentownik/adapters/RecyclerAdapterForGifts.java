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
import com.example.prezentownik.R;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;

import java.util.List;

public class RecyclerAdapterForGifts extends RecyclerView.Adapter<RecyclerAdapterForGifts.ViewHolder> {
    //TODO: Pojedyncze itemy w recycelrview sa z tego samego layoutu co poprzedni recycelrview - zrobic inne jesli bedzei zle dzialac

    private static final String TAG = "RecyclerAdapterForGifts";

    private List<Gift> mGifts;
    private Context mContext;


    public RecyclerAdapterForGifts(Context mContext) {
        this.mContext = mContext;
    }
    public void setGiftModels(List<Gift> mGifts){
        this.mGifts = mGifts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        // Set the name of the Person
        holder.giftName.setText(mGifts.get(position).getGiftName());

        // Set the text of gift quantity
        holder.giftPrice.setText(mGifts.get(position).getGiftPrice());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on something");
                Toast.makeText(mContext, String.valueOf(mGifts.get(position)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mGifts == null){
            return 0;
        } else {
            return mGifts.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView giftName;
        private TextView giftPrice;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            giftName = itemView.findViewById(R.id.person_name);
            giftPrice = itemView.findViewById(R.id.gift_quantity);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

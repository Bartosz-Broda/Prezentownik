package com.example.prezentownik.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prezentownik.GiftsActivity;
import com.example.prezentownik.R;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.GiftRepository;

import java.util.List;

public class RecyclerAdapterForGifts extends RecyclerView.Adapter<RecyclerAdapterForGifts.ViewHolder> {

    private static final String TAG = "RecyclerAdapterForGifts";
    RecyclerAdapterForGifts.OnCheckboxClicked onCheckBoxClicked;

    private List<Gift> mGifts;
    private Context mContext;
    int sumGiftPrice;
    int sumBoughtGiftPrice;
    boolean wasClicked = false;


    public RecyclerAdapterForGifts(Context mContext, RecyclerAdapterForGifts.OnCheckboxClicked onCheckBoxClicked) {
        this.mContext = mContext;
        this.onCheckBoxClicked = onCheckBoxClicked;
    }

    public void setGiftModels(List<Gift> mGifts) {
        this.mGifts = mGifts;
    }
    //public int getGiftCosts(){return sumGiftPrice;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_gift, parent, false);
        ViewHolder holder = new ViewHolder(view);
        wasClicked = false;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        // Set the name of the Person
        holder.giftName.setText(mGifts.get(position).getGiftName());
        // Set the text of gift quantity
        holder.giftPrice.setText(String.valueOf(mGifts.get(position).getGiftPrice()));
        //If gift is bought, set the checkbox on checked
        holder.isBoughtCheckBox.setChecked(mGifts.get(position).getIsBought());

        //If that is initial loaading, get price of checked items and set colors
        int price = mGifts.get(position).getGiftPrice();
        if (mGifts.get(position).getIsBought() && !wasClicked) {
            holder.giftPrice.setTextColor((0xFF32CD32));
            sumBoughtGiftPrice += price;
            onCheckBoxClicked.getBoughtGiftPrice(sumBoughtGiftPrice);
        } else if (!wasClicked){
            holder.giftPrice.setTextColor(Color.GRAY);
            onCheckBoxClicked.getBoughtGiftPrice(sumBoughtGiftPrice);
        }

        //setting the checkbox to true changes data in repository
        //If item is clicked, data in repo changes, which causes reload of recyclerview
        //If item is clicked, change the sum of bought gifts prices.
        holder.isBoughtCheckBox.setOnClickListener(v -> {
            if (!mGifts.get(position).getIsBought()) {
                //wasClicked = true;
                sumBoughtGiftPrice += price;
                holder.giftPrice.setTextColor((0xFF32CD32));
                onCheckBoxClicked.getBoughtGiftPrice(sumBoughtGiftPrice);
                mGifts.get(position).setIsBought(true);
                wasClicked = true;
            } else {
                sumBoughtGiftPrice -= price;
                holder.giftPrice.setTextColor(Color.GRAY);
                onCheckBoxClicked.getBoughtGiftPrice(sumBoughtGiftPrice);
                mGifts.get(position).setIsBought(false);
                wasClicked = true;
            }

            //Passing info to interface, so i can call a method in GiftsActivity to update firestore data.
            onCheckBoxClicked.getGiftCheck(mGifts.get(position).getIsBought(), mGifts.get(position).getGiftName(), mGifts.get(position).getGiftPrice());

        });

        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on something");
            Toast.makeText(mContext, String.valueOf(mGifts.get(position).getIsBought()), Toast.LENGTH_SHORT).show();

        });
    }

    public interface OnCheckboxClicked {
        void getBoughtGiftPrice(int PriceOfCheckedGifts);
        void getGiftCheck(boolean isGiftChecked, String name, int price);

        void onError(Exception e);
    }

    @Override
    public int getItemCount() {
        if (mGifts == null) {
            return 0;
        } else {
            return mGifts.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView giftName;
        private TextView giftPrice;
        private CheckBox isBoughtCheckBox;
        private ImageButton goToShopBtn;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            giftName = itemView.findViewById(R.id.person_name);
            giftPrice = itemView.findViewById(R.id.gift_quantity);
            isBoughtCheckBox = itemView.findViewById(R.id.checkBox);
            goToShopBtn = itemView.findViewById(R.id.imageButton);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

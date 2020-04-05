package com.z5214480_infs3634.cryptopbag;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.z5214480_infs3634.cryptopbag.entities.Coin;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Coin> mDataset;
    private LaunchListener mLaunchListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nameText;
        public TextView costText;
        public TextView percentText;
        LaunchListener myLaunchListener;

        public MyViewHolder(View itemView, LaunchListener myLaunchListener) {
            super(itemView);

            this.nameText = itemView.findViewById(R.id.nameText);
            this.costText = itemView.findViewById(R.id.costText);
            this.percentText = itemView.findViewById(R.id.percentText);

            this.myLaunchListener = myLaunchListener;

            itemView.setOnClickListener(this);

        }

        public void onClick(View view){
            int position = getAdapterPosition();
            Log.d("MyAdapter.java", "onClick: position is " + position);

            myLaunchListener.launch(position);

        }

    }

    public MyAdapter(List<Coin> myDataset, LaunchListener myLaunchListener) {
        // using a Coin list as the dataset
        mDataset = myDataset;

        this.mLaunchListener = myLaunchListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v, mLaunchListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // getting data from each coin in mDataset
        holder.nameText.setText(mDataset.get(position).getName());
        holder.costText.setText(mDataset.get(position).getPriceUsd());
        holder.percentText.setText(mDataset.get(position).getPercentChange1h());

    }

    public interface LaunchListener{
        void launch(int position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = 0;
        if (mDataset != null) {
            size = mDataset.size();
        }
        return size;
    }

}
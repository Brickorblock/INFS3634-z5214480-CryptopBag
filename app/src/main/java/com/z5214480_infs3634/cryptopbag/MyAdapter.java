package com.z5214480_infs3634.cryptopbag;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Coin> mDataset;
    private LaunchListener mLaunchListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
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

            myLaunchListener.launchActivity(position);

        }

    }


    public MyAdapter(ArrayList<Coin> myDataset, LaunchListener myLaunchListener) {
        // using a Coin arraylist as the dataset
        mDataset = myDataset;

        this.mLaunchListener = myLaunchListener;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view (didn't use card views; couldnt figure that out)
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v, mLaunchListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // getting data from each coin in mDataset
        holder.nameText.setText(mDataset.get(position).getName());
        holder.costText.setText(Double.toString(mDataset.get(position).getValue()));
        holder.percentText.setText(Double.toString(mDataset.get(position).getChange1h()));

    }

    public interface LaunchListener{
        void launchActivity(int position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
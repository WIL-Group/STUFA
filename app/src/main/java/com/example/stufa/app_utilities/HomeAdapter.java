package com.example.stufa.app_utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufa.R;
import com.example.stufa.data_models.Announcement;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    ArrayList<Announcement> List;
    Context context;

    public HomeAdapter (Context context, ArrayList<Announcement>list)
    {
        this.context = context;
        this.List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_row, parent, false);

        return new HomeAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.itemView.setTag(List.get(i));
        holder.itemTvTitle.setText(List.get(i).getTitle());
        holder.itemTvName.setText("Thabo");
        if (List.get(i).isViewed())
        {
            holder.itemIvView.setImageResource(R.drawable.viewed);
            holder.itemCardView.setBackgroundColor(R.color.white);

        }
        else
        {
            holder.itemIvView.setImageResource(R.drawable.not_viewed);
            holder.itemCardView.setBackgroundColor(R.color.light_yellow);

        }
        holder.itemMessage.setText(List.get(i).getMessage());
        holder.itemTvDate.setText(String.format("%s", List.get(i).getDate()));
    }



    @Override
    public int getItemCount() {
        return List.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView itemTvTitle;
        public TextView itemTvName;
        public ImageView itemIvView;
        public TextView itemTvDate;
        public TextView itemMessage;
        public CardView itemCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            itemTvName = (TextView)itemView.findViewById(R.id.tvName);
            itemIvView = (ImageView)itemView.findViewById(R.id.ivViewed);
            itemTvDate = (TextView)itemView.findViewById(R.id.tvDate);
            itemMessage = (TextView)itemView.findViewById(R.id.tvAnnounceMessage);
            itemCardView = (CardView)itemView.findViewById(R.id.cvAnnounceRow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List.indexOf((Announcement) v.getTag());
                }
            });
        }

    }
}

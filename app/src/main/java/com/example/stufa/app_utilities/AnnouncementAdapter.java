package com.example.stufa.app_utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stufa.R;
import com.example.stufa.activities.AnnouncementBrowsing;
import com.example.stufa.data_models.Announcement;
import com.example.stufa.data_models.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>
{
    ArrayList<Announcement> announcements;
    ItemClickListener activity;


    public interface ItemClickListener {
        void onItemClick(int pos);
    }

    public AnnouncementAdapter(Context context, ArrayList<Announcement> list)
    {
        this.activity = (ItemClickListener) context;
        this.announcements = list;

    }



    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_row, parent, false);

        return new ViewHolder(v);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, int i) {
        //get the current announcement
        holder.itemView.setTag(announcements.get(i));
        holder.itemTvTitle.setText(announcements.get(i).getTitle());
        holder.itemTvName.setText("Thabo");
        if (announcements.get(i).isViewed())
        {
            holder.itemIvView.setImageResource(R.drawable.viewed);
            holder.itemCardView.setBackgroundColor(R.color.white);
           /* holder.itemIvView.setBackgroundColor(R.color.white);
            holder.itemMessage.setBackgroundColor(R.color.white);
            holder.itemTvName.setBackgroundColor(R.color.white);
            holder.itemTvDate.setBackgroundColor(R.color.white);
            holder.itemTvTitle.setBackgroundColor(R.color.white);*/
        }
        else
        {
            holder.itemIvView.setImageResource(R.drawable.not_viewed);
            holder.itemCardView.setBackgroundColor(R.color.light_yellow);
           /* holder.itemIvView.setBackgroundColor(R.color.light_yellow);
            holder.itemMessage.setBackgroundColor(R.color.light_yellow);
            holder.itemTvName.setBackgroundColor(R.color.light_yellow);
            holder.itemTvDate.setBackgroundColor(R.color.light_yellow);
            holder.itemTvTitle.setBackgroundColor(R.color.light_yellow);*/
        }
        holder.itemMessage.setText(announcements.get(i).getMessage());
        holder.itemTvDate.setText(String.format("%s", announcements.get(i).getDate()));

    }

    @Override
    public int getItemCount() {

        return announcements.size();
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

            itemView.setOnClickListener(v -> activity.onItemClick(announcements.indexOf((Announcement) v.getTag())));

        }

    }
}

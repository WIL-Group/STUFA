package com.example.stufa.app_utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.stufa.R;
import com.example.stufa.data_models.Query;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {

    ArrayList<Query> queries;
    ItemClicked activity;
    int selectedPosition = -1;

    public interface ItemClicked {
        void onItemClicked(int index);

    }

    public QueryAdapter(ArrayList<Query> queries, Context context) {
        this.queries = queries;
        this.activity = (ItemClicked) context;
    }

    @NonNull
    @Override
    public QueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(queries.get(position));
        holder.tvType.setText(queries.get(position).getType());
        holder.tvMessage.setText(queries.get(position).getMessage());
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffbb33"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return queries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvMessage = itemView.findViewById(R.id.tvMessage);

            itemView.setOnClickListener(v -> activity.onItemClicked(queries.indexOf((Query) v.getTag())));

        }
    }


}

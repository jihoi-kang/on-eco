package com.project.oneco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder> {

    private final List<String> itemList;
    private final OnTrashItemClickListener listener;

    public TrashAdapter(OnTrashItemClickListener listener) {
        itemList = new ArrayList<>();

        this.listener = listener;
    }

    public void updateItems(List<String> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trash, parent, false);
        TrashViewHolder holder = new TrashViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, int position) {
        holder.bind(itemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class TrashViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bind(String title, int position) {
            tvTitle.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

    }
}

package com.project.oneco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.oneco.data.MyTrash;

import java.util.ArrayList;

public class WriteTrashAdapter extends RecyclerView.Adapter<WriteTrashAdapter.WriteTrashViewHolder> {

    private ArrayList<MyTrash> itemList;

    public WriteTrashAdapter() {
        itemList = new ArrayList<>();
    }

    public void updateItems(ArrayList<MyTrash> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WriteTrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_write_trash, parent, false);
        WriteTrashViewHolder holder = new WriteTrashViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WriteTrashViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class WriteTrashViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDate;
        private final TextView tvType;
        private final TextView tvAmount;
        private final TextView tvMemo;

        public WriteTrashViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvType = itemView.findViewById(R.id.tv_type);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvMemo = itemView.findViewById(R.id.tv_memo);
        }

        public void bind(MyTrash item) {
            // 실제 값을 넣어줌
            tvDate.setText(item.getDate());
            switch (item.getType()) {
                case "normal_trash":
                    tvType.setText("일반 / 기타");
                    break;
                case "glass":
                    tvType.setText("유리");
                    break;
                case "can":
                    tvType.setText("캔");
                    break;
                case "paper":
                    tvType.setText("종이");
                    break;
                case "plastic":
                    tvType.setText("플라스틱");
                    break;
                case "plastic_bag":
                    tvType.setText("비닐");
                    break;
            }
            tvAmount.setText(item.getAmount() + "g");
            tvMemo.setText(item.getMemo());
        }

    }
}

package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<History> historyList;
    private OnHistoryClickListener listener;

    public interface OnHistoryClickListener {
        void onEditRequested(String date);
    }

    public HistoryAdapter(List<History> historyList, OnHistoryClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.tvDate.setText(history.getDate());
        holder.detailsContainer.removeAllViews();

        if (history.isExpanded()) {
            for (String detail : history.getDetails()) {
                TextView textView = new TextView(holder.itemView.getContext());
                textView.setText(detail);
                holder.detailsContainer.addView(textView);
            }
            holder.detailsContainer.setVisibility(View.VISIBLE);
        } else {
            holder.detailsContainer.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            history.setExpanded(!history.isExpanded());
            notifyItemChanged(position);
        });

        holder.btnEdit.setOnClickListener(v -> {
            listener.onEditRequested(history.getDate());
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        LinearLayout detailsContainer;
        View btnEdit;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            detailsContainer = itemView.findViewById(R.id.details_container);
            btnEdit = itemView.findViewById(R.id.btn_edit); // 確保 layout 有這個 id
        }
    }
}

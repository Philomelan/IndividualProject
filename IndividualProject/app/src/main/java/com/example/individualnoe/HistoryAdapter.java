package com.example.individualnoe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<GameAttempt> attemptsList;

    public HistoryAdapter(List<GameAttempt> attemptsList) {
        this.attemptsList = attemptsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_attempt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameAttempt attempt = attemptsList.get(position);
        holder.tvYears.setText("Год: " + attempt.year);
        holder.tvReason.setText("Причина: " + attempt.deathReason);
    }

    @Override
    public int getItemCount() {
        return attemptsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvYears;
        TextView tvReason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYears = itemView.findViewById(R.id.tvYears);
            tvReason = itemView.findViewById(R.id.tvReason);
        }
    }
}

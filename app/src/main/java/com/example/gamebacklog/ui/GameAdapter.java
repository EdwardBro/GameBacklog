package com.example.gamebacklog.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebacklog.Game;
import com.example.gamebacklog.R;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGames;

    public GameAdapter(List<Game> mGames) {
        this.mGames = mGames;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int i) {
        Game game = mGames.get(i);
        viewHolder.titleView.setText(game.getGameName());
        viewHolder.platformView.setText(game.getGamePlatform());
        viewHolder.statusView.setText(game.getGameStatus());
        viewHolder.dateView.setText(game.getGameEdited());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void swapList (List<Game> newList) {
        mGames = newList;
        if(newList != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, platformView, statusView, dateView;

        public ViewHolder(View itemView){
            super(itemView);
            titleView = itemView.findViewById(R.id.titleView);
            platformView = itemView.findViewById(R.id.platformView);
            statusView = itemView.findViewById(R.id.statusView);
            dateView = itemView.findViewById(R.id.dateView);
        }
    }
}

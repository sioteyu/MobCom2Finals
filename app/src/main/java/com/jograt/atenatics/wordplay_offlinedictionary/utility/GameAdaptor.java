package com.jograt.atenatics.wordplay_offlinedictionary.utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jograt.atenatics.wordplay_offlinedictionary.R;
import com.jograt.atenatics.wordplay_offlinedictionary.TrueFalseActivity;

import java.util.List;

/**
 * Created by it.admin on 9/16/2017.
 */

public class GameAdaptor extends RecyclerView.Adapter<GameAdaptor.GameViewHolder>{
    List<Game> games;
    private Context context;
    public GameAdaptor(List<Game>games) {
        this.games = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamecard, parent, false);
        GameViewHolder mh = new GameViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        holder.description.setText(games.get(position).getDescription());
        holder.title .setText(games.get(position).getTitle());
        holder.gameImg.setImageResource(games.get(position).getImage());
        holder.play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (position==0){
                    v.getContext().startActivity(new Intent(v.getContext(), TrueFalseActivity.class));
                }
            }
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView gameImg;
        TextView title;
        TextView description;
        Button play;
        GameViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            play = (Button)itemView.findViewById(R.id.playBtn);
            title = (TextView)itemView.findViewById(R.id.Title);
            gameImg = (ImageView)itemView.findViewById(R.id.gameImage);
            description = (TextView)itemView.findViewById(R.id.Description);
        }
    }
}

package com.jograt.atenatics.wordplay_offlinedictionary.utility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jograt.atenatics.wordplay_offlinedictionary.R;

import java.util.List;

/**
 * Created by it.admin on 9/16/2017.
 */

public class WordAdaptor extends RecyclerView.Adapter<WordAdaptor.PersonViewHolder>{
    List<Word> words;

    public WordAdaptor(List<Word>words) {
        this.words = words;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordcard, parent, false);
        PersonViewHolder mh = new PersonViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(WordAdaptor.PersonViewHolder holder, int position) {
        holder.description.setText(words.get(position).getDescription());
        holder.part.setText(words.get(position).getPart());
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView part;
        TextView description;
        PersonViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            part = (TextView)itemView.findViewById(R.id.part);
            description = (TextView)itemView.findViewById(R.id.description);
        }
    }
}

package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.Game;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.GameAdaptor;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameListFragment extends android.support.v4.app.Fragment {
    List<Game> games;
    RecyclerView rv;

    public GameListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_games, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        adDrawer draw = new adDrawer((AdView) view.findViewById(R.id.banner_AdView), new AdRequest.Builder().build());
        initialize();
        GameAdaptor adaptor = new GameAdaptor(games);
        rv.setAdapter(adaptor);
        LinearLayoutManager lmm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lmm);
        return view;
    }

    private void initialize() {
        games = new ArrayList<Game>();
        games.add(new Game(R.drawable.trueorfalse, "True or False", "Test your knowledge, Which is right? Think carefully..."));
    }
}

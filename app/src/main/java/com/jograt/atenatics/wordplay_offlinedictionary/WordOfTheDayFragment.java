package com.jograt.atenatics.wordplay_offlinedictionary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordOfTheDayFragment extends Fragment {

    TextView word;
    TextView description;
    public WordOfTheDayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_of_the_day, container, false);
        Bundle bundle = this.getArguments();
        word = (TextView) view.findViewById(R.id.wordOfTheDay);
        description = (TextView) view.findViewById(R.id.wotdDescription);
        try {
            String json = bundle.getString("json");
            JSONObject wordObj = new JSONObject(json);
            JSONArray defArr = wordObj.getJSONArray("definitions");

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < defArr.length(); ++i) {
                JSONObject defObj = new JSONObject(defArr.getJSONObject(i).toString());
                sb.append("\n" + defObj.getString("partOfSpeech"));
                sb.append("\n" + defObj.getString("text"));
            }

            //set word and def
            word.setText(wordObj.getString("word"));
            description.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdView mAdView = (AdView) view.findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

}

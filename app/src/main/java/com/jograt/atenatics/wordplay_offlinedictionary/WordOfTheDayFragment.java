package com.jograt.atenatics.wordplay_offlinedictionary;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordOfTheDayFragment extends Fragment {

    TextView word;
    TextView description;
    ProgressBar progressBar;
    AdView mAdView;
    String uri = "http://api.wordnik.com:80/v4/words.json/wordOfTheDay?api_key=" +
            "1ed321800025e469d74033a1221fe952d933bcb9aa78041be";
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
        progressBar = (ProgressBar)view.findViewById(R.id.wordOfTheDayProgress);
        mAdView = (AdView) view.findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        new doBackground().execute(uri);
        return view;
    }
    private class doBackground extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result){
            progressBar.setVisibility(View.INVISIBLE);
            try {
                JSONObject wordObj = new JSONObject(result);
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
        }
        @Override
        protected String doInBackground(String... param) {
            try{
                URL url = new URL(param[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();

            }catch (Exception e){
                e.printStackTrace();
            }

            return "offline";
        }
    }
}

package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RandomWordFragment extends Fragment {
    ProgressBar progressBar;
    TextView word;
    TextView definition;
    String uri = "http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=false&minCorpusCount=0&" +
            "maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=5&maxLength=-1&" +
            "api_key=1ed321800025e469d74033a1221fe952d933bcb9aa78041be";
    public RandomWordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_word, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.randomProgress);
        word = (TextView)view.findViewById(R.id.randomWord);
        definition = (TextView)view.findViewById(R.id.randomDef);
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
                JSONArray wordArr = new JSONArray(result);
                word.setText(new JSONObject(wordArr.getJSONObject(0).toString()).getString("word"));
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<wordArr.length();++i){
                    JSONObject defObj = new JSONObject(wordArr.getJSONObject(i).toString());
                    sb.append("\n"+defObj.getString("partOfSpeech"));
                    sb.append("\n"+defObj.getString("text"));
                }
                definition.setText(sb.toString());
            }catch (Exception e){
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
                connection.disconnect();
                JSONObject wordObj = new JSONObject(sb.toString());
                String uri2="http://api.wordnik.com:80/v4/word.json/"+wordObj.getString("word")+"/definitions?limit=200&includeRelated=true&" +
                        "useCanonical=false&includeTags=false&api_key=1ed321800025e469d74033a1221fe952d933bcb9aa78041be";

                url = new URL(uri2);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
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

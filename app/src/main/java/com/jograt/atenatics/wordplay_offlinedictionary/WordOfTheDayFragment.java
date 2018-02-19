package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.Word;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.WordAdaptor;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordOfTheDayFragment extends Fragment {

    TextView word;
    ProgressBar progressBar;
    List<Word> words;
    RecyclerView rv;
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
        progressBar = (ProgressBar)view.findViewById(R.id.wordOfTheDayProgress);
        rv = (RecyclerView)view.findViewById(R.id.rvwotd);
        //draw ad
        adDrawer draw = new adDrawer((AdView) view.findViewById(R.id.banner_AdView), new AdRequest.Builder().build(), getActivity());

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
            if (result.equals("offline")){
                new Toast(getActivity()).makeText(getActivity().getApplicationContext(),"Currently Offline, Try again", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject wordObj = new JSONObject(result);
                    JSONArray defArr = wordObj.getJSONArray("definitions");

                    words = new ArrayList<Word>();

                    for (int i = 0; i < defArr.length(); ++i) {
                        JSONObject defObj = new JSONObject(defArr.getJSONObject(i).toString());
                        words.add(new Word(defObj.getString("partOfSpeech"), defObj.getString("text")));
                    }
                    WordAdaptor adaptor = new WordAdaptor(words);
                    rv.setAdapter(adaptor);
                    LinearLayoutManager lmm = new LinearLayoutManager(getActivity());
                    rv.setLayoutManager(lmm);
                    //set word
                    word.setText(wordObj.getString("word"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

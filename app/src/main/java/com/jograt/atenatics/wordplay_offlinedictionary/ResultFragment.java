package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.Word;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.WordAdaptor;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    String uri;
    TextView word;
    ProgressBar progressBar;
    List<Word> words;
    RecyclerView rv;
    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        Bundle bundle = this.getArguments();
        uri = "http://api.wordnik.com:80/v4/word.json/"+bundle.getString("query").toLowerCase()+"/definitions?limit=200&includeRelated=true&" +
                "useCanonical=false&includeTags=false&api_key=1ed321800025e469d74033a1221fe952d933bcb9aa78041be";
        word = (TextView)view.findViewById(R.id.wordSearch);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        rv = (RecyclerView)view.findViewById(R.id.rv);

        adDrawer draw = new adDrawer((AdView) view.findViewById(R.id.banner_AdView), new AdRequest.Builder().build(),getActivity());

        new doBackground().execute(uri);
        word.setText(bundle.getString("query"));
        return view;
    }


    private class doBackground extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result){
            if(result.equals("offline")){
                try{
                    words = new ArrayList<Word>();
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    String def = obj.getString(word.getText().toString().toUpperCase());
                        words.add(new Word("Go online to see more", def));
                    Log.v("Result is", def);

                }catch (Exception e){
                    words.add(new Word("Go online to see more", "No results found"));
                    e.printStackTrace();
                }
            }else{
                if(result.equals("[]")){
                    words = new ArrayList<Word>();
                    words.add(new Word("Error", "No definition found"));
                }else{
                    words = new ArrayList<Word>();
                    StringBuilder sb = new StringBuilder();
                    try{
                        JSONArray defArr = new JSONArray(result);
                        for(int i=0;i<defArr.length();++i){
                            JSONObject defObj = new JSONObject(defArr.getJSONObject(i).toString());

                            words.add(new Word(defObj.getString("partOfSpeech"),defObj.getString("text")));
                            Log.v("Expected Result", defObj.getString("partOfSpeech")+", "+defObj.getString("text"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            WordAdaptor adaptor = new WordAdaptor(words);
            rv.setAdapter(adaptor);
            LinearLayoutManager lmm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(lmm);
            progressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        protected String doInBackground(String... param) {
            try{
                URL url = new URL(param[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("dictionary.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

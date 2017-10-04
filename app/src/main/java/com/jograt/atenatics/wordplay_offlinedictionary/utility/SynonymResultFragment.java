package com.jograt.atenatics.wordplay_offlinedictionary.utility;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.R;
import com.jograt.atenatics.wordplay_offlinedictionary.SynonymsFragment;

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
public class SynonymResultFragment extends Fragment {
    String uri;
    ProgressBar progressBar;
    TextView results;
    List<Word> words;
    RecyclerView rv;
    String word;
    String option;
    String message;
    Button button;
    public SynonymResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        word = bundle.getString("query");
        option = bundle.getString("option");

        if(option.equals("Related To")){
            message = word+" is "+option;
            uri = "https://api.datamuse.com/words?ml="+word;
        }else if(option.equals("Sounds Like")){
            message = word + " " + option;
            uri = "https://api.datamuse.com/words?sl="+word;
        }else if(option.equals("Spelled Similar")){
            message = word + " is " + option + " to";
            uri = "https://api.datamuse.com/words?sp="+word;
        }else {
            message = word + " " + option;
            uri = "https://api.datamuse.com/words?rel_rhy="+word;
        }

        View view = inflater.inflate(R.layout.fragment_synonym_result, container, false);
        adDrawer draw = new adDrawer((AdView) view.findViewById(R.id.banner_AdView), new AdRequest.Builder().build());
        progressBar = (ProgressBar)view.findViewById(R.id.synonymprogress);
        rv = (RecyclerView)view.findViewById(R.id.synonymRv);
        results = (TextView)view.findViewById(R.id.results);
        button = (Button)view.findViewById(R.id.tryagain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setTitle("");
                SynonymsFragment synonymsFragment = new SynonymsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, synonymsFragment).commit();
            }
        });

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
            if(result.equals("offline")){
                    new Toast(getActivity()).makeText(getActivity().getApplicationContext(), "Currently Offline, Try Again", Toast.LENGTH_SHORT).show();
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
                            words.add(new Word("Popularity Points: "+defObj.getString("score"),defObj.getString("word")));
                        }
                        results.setText(message);
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

}

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


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    String uri;
    TextView word;
    TextView definition;
    ProgressBar progressBar;
    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        Bundle bundle = this.getArguments();
        uri = "http://api.wordnik.com:80/v4/word.json/"+bundle.getString("query")+"/definitions?limit=200&includeRelated=true&" +
                "useCanonical=false&includeTags=false&api_key=1ed321800025e469d74033a1221fe952d933bcb9aa78041be";
        word = (TextView)view.findViewById(R.id.wordSearch);
        definition = (TextView)view.findViewById(R.id.wordDef);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        word.setText(bundle.getString("query"));
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
            if(result.equals("[]")){
                definition.setText("No Definition Found");
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                StringBuilder sb = new StringBuilder();
                try{
                    JSONArray defArr = new JSONArray(result);
                    for(int i=0;i<defArr.length();++i){
                        JSONObject defObj = new JSONObject(defArr.getJSONObject(i).toString());
                        sb.append("\n"+defObj.getString("partOfSpeech"));
                        sb.append("\n"+defObj.getString("text"));
                    }
                    definition.setText(sb.toString());
                }catch (Exception e){
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

package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumberTriviaFragment extends Fragment {

    ListView lv;
    List<String> trivia;
    ProgressBar progressBar;
    String uri = "http://numbersapi.com/random/trivia";

    public NumberTriviaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_trivia, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.triviaprogress);
        lv = view.findViewById(R.id.lv);
        trivia = new ArrayList<String>();
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
            if (result.equals("online")){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listlayout, R.id.trivia, trivia);
                lv.setAdapter(adapter);
            }else{
                new Toast(getActivity()).makeText(getActivity().getApplicationContext(),"Currently Offline, Try again", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... param) {
            try{
                URL url;
                HttpURLConnection connection;
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i<10; ++i){
                    url = new URL(param[0]);
                    connection = (HttpURLConnection)url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    sb = new StringBuilder();
                    while ((line = reader.readLine())!=null){
                        sb.append(line);
                    }
                    trivia.add(sb.toString());
                }

                return "online";
            }catch (Exception e){
                e.printStackTrace();
            }

            return "offline";
        }
    }
}

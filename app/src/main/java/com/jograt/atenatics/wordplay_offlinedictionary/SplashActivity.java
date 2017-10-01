package com.jograt.atenatics.wordplay_offlinedictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
        ProgressBar progressBar;
        TextView textView;
        String uri = "http://api.wordnik.com:80/v4/words.json/wordOfTheDay?date=2017-09-30&api_key=\n" +
                "1ed321800025e469d74033a1221fe952d933bcb9aa78041be";
        String status;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_splash);
            new doBackground().execute(uri);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, SearchActivity.class));
//            }
//        },2000);
        }

        private class doBackground extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute(){

            }
            @Override
            protected void onPostExecute(String result){
                if(result.equals("offline")){
                    status = "offline";
                }else{
                    status = "online";
                    Log.v("JSON", result);
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("status", status);
                intent.putExtra("json", result);
                startActivity(intent);
                finish();
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

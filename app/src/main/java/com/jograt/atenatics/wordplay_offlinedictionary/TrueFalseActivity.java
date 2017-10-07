package com.jograt.atenatics.wordplay_offlinedictionary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrueFalseActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    List<String>questions;
    List<String>answer;
    MediaPlayer bgm;
    TextView question;
    TextView text;
    TextView points;
    LinearLayout ll;
    ProgressBar progressBar;
    Button tru;
    Button fals;
    String uri;
    int count = 0;
    int easy = 16;
    int medium = 35;
    int hard = 100-easy-medium;
    int hearts;
    int level;
    int stage = 1;
    int heartID[] = {R.id.hp5,R.id.hp4,R.id.hp3,R.id.hp2,R.id.hp1};
    int multiplier = 2;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);
        question = (TextView)findViewById(R.id.question);
        points = (TextView)findViewById(R.id.point);
        text = (TextView)findViewById(R.id.text);

        progressBar = (ProgressBar)findViewById(R.id.loading);
        tru = (Button)findViewById(R.id.trueKey);
        fals = (Button)findViewById(R.id.falseKey);
        ll = (LinearLayout)findViewById(R.id.hearts);
        uri = dificulty(easy);
        Typeface type = Typeface.createFromAsset(getAssets(),"mainfont.ttf");
        question.setTypeface(type);
        points.setTypeface(type);
        text.setTypeface(type);
        points.setText("0");
        text.setText("Total Points");
        hearts = 5;
        level = 1;
        count = easy;
        new doBackground().execute(uri);
        bgm = MediaPlayer.create(TrueFalseActivity.this, R.raw.main);
        bgm.setLooping(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgm.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgm.start();
    }
    public void checkAns(View view){
        if (stage == 100){
            //Game Clear
        }else {
            if(count==1){
                level++;
                new Toast(this).makeText(getApplicationContext(), "Level " + level, Toast.LENGTH_SHORT).show();
                if(level==2){
                    multiplier = 5;
                    uri = dificulty(medium);
                    count = medium;
                    new doBackground().execute(uri);
                }else if (level==3){
                    multiplier = 10;
                    uri = dificulty(hard);
                    count = hard;
                    new doBackground().execute(uri);
                }
            }else {
                switch (view.getId()){
                    case R.id.trueKey:
                        Log.v("Pressed", "True");
                        if (answer.get(count-1).equals("True")){
                            int current = Integer.parseInt(points.getText().toString());
                            points.setText((current+multiplier)+"");
                        }else{
                            ll.removeView(ll.findViewById(heartID[hearts-1]));
                            hearts--;
                        }

                        count--;
                        stage++;
                        question.setText(questions.get(count-1));
                        break;
                    case R.id.falseKey:
                        Log.v("Pressed", "False");
                        if (answer.get(count-1).equals("False")){
                            int current = Integer.parseInt(points.getText().toString());
                            points.setText((current+multiplier)+"");
                        }else{
                            ll.removeView(ll.findViewById(heartID[hearts-1]));
                            hearts--;
                        }
                        count--;
                        stage++;
                        question.setText(questions.get(count-1));
                        break;
                }
            }
        }
        if (hearts==0){
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            int defaultValue = 0;
            int highScore = sharedPref.getInt("highscore", defaultValue);
            if(highScore<Integer.parseInt(points.getText().toString())){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("highscore", Integer.parseInt(points.getText().toString()));
                editor.commit();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Game Over")
                    .setMessage("Do you want to continue?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // show ads
                            hearts = 5;
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(TrueFalseActivity.this, TrueFalseActivity.class));
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    public String dificulty(int number){
        if(number==16){
            return "https://opentdb.com/api.php?amount=16&difficulty=easy&type=boolean";
        }else if(number==35){
            return "https://opentdb.com/api.php?amount=35&difficulty=medium&type=boolean";
        }else{
            return "https://opentdb.com/api.php?amount=49&difficulty=hard&type=boolean";
        }
    }

    private class doBackground extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            tru.setEnabled(false);
            fals.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result){
            progressBar.setVisibility(View.INVISIBLE);
            if (result.equals("offline")){
                new Toast(TrueFalseActivity.this).makeText(getApplicationContext(),"Currently Offline, Try again", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                Log.v("JSON", result);
                questions = new ArrayList<String>();
                answer = new ArrayList<String>();
                try{
                    JSONObject obj = new JSONObject(result);
                    JSONArray objArr = new JSONArray(obj.getJSONArray("results").toString());
                    for (int i = 0; i<objArr.length();++i){
                        JSONObject quest = new JSONObject(objArr.getJSONObject(i).toString());
                        questions.add(quest.getString("question").replace("&quot;", "\"").replace("&#039;","'").
                                replace("&ldquo;","'").replace("&rdquo;","'").replace("&ocirc;","Ô").replace("&Aring;","Å"));
                        answer.add(quest.getString("correct_answer"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                question.setText(Html.fromHtml(questions.get(count-1),Html.FROM_HTML_MODE_LEGACY));
            } else {
                question.setText(Html.fromHtml(questions.get(count-1)));
            }
            tru.setEnabled(true);
            fals.setEnabled(true);
            bgm.start();
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
                }

                return sb.toString();
            }catch (Exception e){
                e.printStackTrace();
            }

            return "offline";
        }
    }
}
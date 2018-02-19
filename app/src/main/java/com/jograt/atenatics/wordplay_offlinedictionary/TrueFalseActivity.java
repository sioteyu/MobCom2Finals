package com.jograt.atenatics.wordplay_offlinedictionary;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrueFalseActivity extends AppCompatActivity implements RewardedVideoAdListener{
    List<String> questions;
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
    int heartID[] = {R.id.hp1,R.id.hp2,R.id.hp3,R.id.hp4,R.id.hp5};
    int multiplier = 2;
    Typeface type;
    RewardedVideoAd rvd;
    SharedPreferences sharedPref;
    boolean adFinished = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_true_false);
        adDrawer draw = new adDrawer((AdView) findViewById(R.id.banner_AdView), new AdRequest.Builder().build(),this);
        rvd = MobileAds.getRewardedVideoAdInstance(this);
        rvd.setRewardedVideoAdListener(this);
        loadAd();

        question = (TextView)findViewById(R.id.question);
        points = (TextView)findViewById(R.id.point);
        text = (TextView)findViewById(R.id.scoretext);

        progressBar = (ProgressBar)findViewById(R.id.loading);
        tru = (Button)findViewById(R.id.trueKey);
        fals = (Button)findViewById(R.id.falseKey);
        ll = (LinearLayout)findViewById(R.id.truehearts);
        uri = dificulty(easy);
        type = Typeface.createFromAsset(getAssets(),"mainfont.ttf");
        question.setTypeface(type);
        points.setTypeface(type);
        text.setTypeface(type);
        points.setText("0");
        text.setText("Total Points");
        hearts = 5;
        level = 1;
        count = 10;
        new doBackground().execute(uri);
        bgm = MediaPlayer.create(this, R.raw.theme);
        bgm.setLooping(true);
    }
    public void loadAd(){
        if(!rvd.isLoaded()){
            rvd.loadAd("ca-app-pub-4142791821230838/7116394426", new AdRequest.Builder().build());
        }
    }
    @Override
    public void onBackPressed() {
        bgm.stop();
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        bgm.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        bgm.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        bgm.stop();
        super.onDestroy();
    }
    public void checkAns(View view) {
        tru.setEnabled(false);
        fals.setEnabled(false);
        if (count == 1) {
            level++;

            if (level == 2) {
                new Toast(this).makeText(getApplicationContext(), "Loading Level " + level, Toast.LENGTH_SHORT).show();
                multiplier = 5;
                uri = dificulty(medium);
                count = 10;
                new doBackground().execute(uri);

            } else if (level == 3) {
                new Toast(this).makeText(getApplicationContext(), "Loading Level " + level, Toast.LENGTH_SHORT).show();
                multiplier = 10;
                uri = dificulty(hard);
                count = 10;
                new doBackground().execute(uri);

            } else {
                multiplier = 10;
                uri = dificulty(hard);
                count = 10;
                new doBackground().execute(uri);
            }
        } else {
            switch (view.getId()) {
                case R.id.falseKey:
                    if (answer.get(count - 1).equals("False")) {
                        int current = Integer.parseInt(points.getText().toString());
                        points.setText((current + multiplier) + "");
                    } else {
                        ll.findViewById(heartID[hearts - 1]).setVisibility(View.INVISIBLE);
                        hearts--;
                    }

                    count--;
                    stage++;
                    question.setText(questions.get(count - 1));
                    break;
                case R.id.trueKey:
                    if (answer.get(count - 1).equals("True")) {
                        int current = Integer.parseInt(points.getText().toString());
                        points.setText((current + multiplier) + "");
                    } else {
                        ll.findViewById(heartID[hearts - 1]).setVisibility(View.INVISIBLE);
                        hearts--;
                    }
                    count--;
                    stage++;
                    question.setText(questions.get(count - 1));
                    break;
            }
            tru.setEnabled(true);
            fals.setEnabled(true);
        }
        if (hearts == 0) {
            tru.setEnabled(false);
            fals.setEnabled(false);
            sharedPref = getSharedPreferences("values", 0);
            int defaultValue = 0;
            int highScore = sharedPref.getInt("highscore", defaultValue);
            if (highScore < Integer.parseInt(points.getText().toString())) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("highscore", Integer.parseInt(points.getText().toString()));
                editor.commit();
            }
            //Game Over Screen
            gameOver(sharedPref);
        }
    }
    public void gameOver(final SharedPreferences sharedPref){
        LayoutInflater factory = LayoutInflater.from(this);
        final View continueDialogView = factory.inflate(R.layout.continuegame, null);
        final AlertDialog continueDialog = new AlertDialog.Builder(this).create();
        continueDialog.setView(continueDialogView);
        continueDialog.setCancelable(false);
        continueDialog.setCanceledOnTouchOutside(false);

        TextView textdialog = (TextView) continueDialogView.findViewById(R.id.textdialog);
        final TextView counter = (TextView) continueDialogView.findViewById(R.id.counter);

        textdialog.setTypeface(type);
        counter.setTypeface(type);

        textdialog.setText("Do you want to continue?");
        counter.setText("0");

        final CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText((millisUntilFinished / 1000)+"");
            }

            public void onFinish() {
                endGame(sharedPref);
            }
        };
        continueDialogView.findViewById(R.id.btn_ads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if(rvd.isLoaded()){
                    rvd.show();
                    continueDialog.dismiss();
                }else{
                    new Toast(TrueFalseActivity.this).makeText(getApplicationContext(),"AD is loading, Try again", Toast.LENGTH_SHORT).show();
                    countDownTimer.start();
                }
            }
        });
        continueDialogView.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                endGame(sharedPref);
                continueDialog.dismiss();
            }
        });
        continueDialog.show();
        countDownTimer.start();
    }
    public void continueGame(){
        // show ads
        tru.setEnabled(true);
        fals.setEnabled(true);
        ll.findViewById(heartID[0]).setVisibility(View.VISIBLE);
        ll.findViewById(heartID[1]).setVisibility(View.VISIBLE);
        ll.findViewById(heartID[2]).setVisibility(View.VISIBLE);
        ll.findViewById(heartID[3]).setVisibility(View.VISIBLE);
        ll.findViewById(heartID[4]).setVisibility(View.VISIBLE);
        hearts = 5;
    }
    public void endGame(SharedPreferences sharedPref){
        int coins = sharedPref.getInt("coins",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("coins", coins+(Integer.parseInt(points.getText().toString())/10));
        editor.commit();
        onBackPressed();
    }
    public String dificulty(int number){
        if(number==16){
            return "https://opentdb.com/api.php?amount=10&type=boolean";
        }else if(number==35){
            return "https://opentdb.com/api.php?amount=10&type=boolean";
        }else{
            return "https://opentdb.com/api.php?amount=10&type=boolean";
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadAd();
        if (adFinished){
            adFinished = false;
        }else{
            gameOver(this.sharedPref);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        adFinished = true;
        continueGame();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

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
                        questions.add(Html.fromHtml(quest.getString("question")).toString());
                        answer.add(quest.getString("correct_answer"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                question.setText(questions.get(count-1));

                tru.setEnabled(true);
                fals.setEnabled(true);
            }
        }
        @Override
        protected String doInBackground(String... param) {
            ConnectivityManager manager = (ConnectivityManager)getSystemService(TrueFalseActivity.CONNECTIVITY_SERVICE);

            if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED||
                    manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
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
                    Log.v("Rsult",sb.toString());
                    return sb.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return "offline";
        }
    }
}

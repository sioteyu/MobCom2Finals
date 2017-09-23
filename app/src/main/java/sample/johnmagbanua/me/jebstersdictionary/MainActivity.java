package sample.johnmagbanua.me.jebstersdictionary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {
    EditText input;
    TextView word;
    TextView description;
    JSONObject dictionaryObject;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        word = (TextView)findViewById(R.id.word);
        description = (TextView)findViewById(R.id.definition);
        try {
            dictionaryObject = new JSONObject(loadJSONFromAsset());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("dictionary.json");

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

    public void searchWord(View v){
        input = (EditText)findViewById(R.id.inputWord);
        try {
            String definition = dictionaryObject.getString(input.getText().toString().toUpperCase());
            word.setText(input.getText().toString());
            description.setText(definition);
        }catch (JSONException e){
            word.setText(input.getText().toString());
            description.setText("No data found sorry :(");
        }
    }
    public void viewAll(View v){
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

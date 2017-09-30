package sample.johnmagbanua.me.jebstersdictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {
    List<Word> words;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        offlineInitialize();
        rv = (RecyclerView)findViewById(R.id.rv);
        WordAdaptor adaptor = new WordAdaptor(words);
        rv.setAdapter(adaptor);
        LinearLayoutManager lmm = new LinearLayoutManager(this);
        rv.setLayoutManager(lmm);

    }

    public void offlineInitialize(){
        JSONObject wordObject = null;
        try {
            wordObject = new JSONObject(loadJSONFromAsset());
        }catch (JSONException e){
            e.printStackTrace();
        }

        words = new ArrayList<Word>();

        JSONArray wordarray = wordObject.names();
        String names[] = new String[wordarray.length()];
        for (int i = 0; i<wordarray.length();++i){
           try {
               names[i] = wordarray.getString(i);
           }catch (JSONException e){
               e.printStackTrace();
           }
        }

        for(int i = 0; i<wordObject.length();++i){
            try {
                words.add(new Word(names[i], wordObject.getString(names[i])));
            }catch (JSONException e){
                e.printStackTrace();
            }
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
}

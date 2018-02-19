package com.jograt.atenatics.wordplay_offlinedictionary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jograt.atenatics.wordplay_offlinedictionary.utility.adDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SynonymsFragment extends Fragment{
    Spinner spinner;
    String selected;
    Button button;
    EditText word;
    public SynonymsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_synonyms, container, false);
        adDrawer draw = new adDrawer((AdView) view.findViewById(R.id.banner_AdView), new AdRequest.Builder().build(),getActivity());
        spinner = (Spinner)view.findViewById(R.id.dropdown);
        button = (Button)view.findViewById(R.id.spinnerBtn);
        word = (EditText)view.findViewById(R.id.searchQuery);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.choice_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(word.getText().toString().trim().equals("")){
                    word.setError("Please Enter A word or phrase");
                }else{
                    String wordQuery = word.getText().toString();
                    selected = spinner.getSelectedItem().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("query", wordQuery);
                    bundle.putString("option", selected);
                    getActivity().setTitle("");
                    SynonymResultFragment synonymResultFragment = new SynonymResultFragment();
                    synonymResultFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment, synonymResultFragment).commit();
                }
            }
        });

        return view;
    }
}

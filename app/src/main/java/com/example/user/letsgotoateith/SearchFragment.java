package com.example.user.letsgotoateith;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.letsgotoateith.data.TransfersContract;

/**
 * Created by user on 28/4/2015.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    View rootView;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Button sub=(Button)rootView.findViewById(R.id.submit_button);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner sp;
                sp=(Spinner)rootView.findViewById(R.id.whichDaySpinner);
                int day=sp.getSelectedItemPosition();
                sp=(Spinner)rootView.findViewById(R.id.depTimeSpinner);
                int depTime=sp.getSelectedItemPosition();
                sp=(Spinner)rootView.findViewById(R.id.retTimeSpinner);
                int retTime=sp.getSelectedItemPosition();
                sp=(Spinner)rootView.findViewById(R.id.freqSpinner);
                int freq=sp.getSelectedItemPosition();
                sp=(Spinner)rootView.findViewById(R.id.areaSpinner);
                int area=sp.getSelectedItemPosition();

                uri= TransfersContract.CarsEntry.buildSearchRegUri(day,depTime,retTime,freq,area);

                Intent intent = new Intent(getActivity(), ResultsActivity.class);
                intent.putExtra(Constants.EXTRA_URI, uri.toString());
                startActivity(intent);
            }

        });


        return rootView;
    }


}

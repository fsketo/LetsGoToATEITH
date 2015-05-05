package com.example.user.letsgotoateith;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.user.letsgotoateith.data.TransfersContract;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;

/**
 * Created by user on 28/4/2015.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    View rootView;
    Uri uri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
//        if(!MainActivity.mTwoPane) {
//            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
//            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        else {
//            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.toolbar_Linear);
//            ll.removeViewAt(0);
//        }
        ButtonRectangle sub=(ButtonRectangle)rootView.findViewById(R.id.submit_button);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.mTwoPane)
            return true;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new SnackBar(getActivity(), "Are you sure you want to logout?", "Yes", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SharedPreferences prefs=getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putInt(Constants.EXTRA_USERID, -1);
                    editor.putString(getString(R.string.pref_username_key), "-1");
                    editor.commit();
                    Log.v("User ID", "#####********User ID:" + prefs.getInt(Constants.EXTRA_USERID, -5555));
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    getActivity().sendBroadcast(broadcastIntent);
                    Intent it = new Intent(getActivity(), LoginActivity.class);
                    startActivity(it);
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }


}

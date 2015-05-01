package com.example.user.letsgotoateith;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 28/4/2015.
 */
public class ResultsFragment extends Fragment {

    private String temp;
    //private String[] detailsTemp=new String[2];
    private static final int QUERY_LOADER = 101;
    //private static final int QUERY_LOADER2 = 102;
    private ArrayAdapter<String> myAdapter;
    // these indices must match the projection
    private static final int USERS_INDEX_FULLNAME = 13;
    private static final int USERS_INDEX_EMAIL = 15;
    private static final int INDEX_DAY = 2;
    private static final int INDEX_DEP_TIME = 3;
    private static final int INDEX_RET_TIME = 4;
    private static final int INDEX_FREQ = 5;
    private static final int INDEX_DRIVER_ID = 7;
    private static final int INDEX_AREA = 8;



        public ResultsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_results, container, false);
            final ListView results = (ListView) rootView.findViewById(R.id.resultListview);
            myAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    new ArrayList());

            results.setAdapter(myAdapter);
            findCars();
            results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(results.getItemAtPosition(position).toString());
                    while (m.find()) {
                        intent.putExtra(Intent.EXTRA_EMAIL, m.group());
                    }
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Let's go to ATEITH");
                    intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(results.getItemAtPosition(position).toString()));

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });

            return rootView;
        }

        private void findCars(){

            getLoaderManager().initLoader(QUERY_LOADER, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                    return new CursorLoader(getActivity(),
                            Uri.parse(getActivity().getIntent().getStringExtra(Constants.EXTRA_URI)),
                            null,
                            null,
                            null,
                            null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    String[] tempTimeSpinnerValues=getResources().getStringArray(R.array.timeSpinner);
                    String[] tempFreqSpinnerValues=getResources().getStringArray(R.array.freqSpinner);
                    String[] tempDaySpinnerValues=getResources().getStringArray(R.array.weekDaySpinner);
                    String[] tempAreaSpinnerValues=getResources().getStringArray(R.array.areaSpinner);
                    if(data.moveToFirst()) {
                       do {
                           temp="Day: "+tempDaySpinnerValues[data.getInt(INDEX_DAY)]+
                                   " Departure time: "+tempTimeSpinnerValues[data.getInt(INDEX_DEP_TIME)]+
                                   " Return time: "+tempTimeSpinnerValues[data.getInt(INDEX_RET_TIME)]+
                                   " Frequency: "+tempFreqSpinnerValues[data.getInt(INDEX_FREQ)]+
                                   " Area: "+tempAreaSpinnerValues[data.getInt(INDEX_AREA)]+
                                   " Driver's name: "+data.getString(USERS_INDEX_FULLNAME)+
                                   " Driver's email: "+data.getString(USERS_INDEX_EMAIL);


                           //Toast.makeText(getActivity(),temp,Toast.LENGTH_LONG).show();

                           Log.v("Query Result","Query Result: "+temp);

                           //findName(data.getInt(INDEX_DRIVER_ID));
                           myAdapter.add(temp);
                           temp="";
                       }while(data.moveToNext());
                    }
                    else{
                        myAdapter.add("No results found");
                    }

                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {


                }
            });


        }

//        private void findName(final int userID) {
//
//        getLoaderManager().initLoader(QUERY_LOADER2, null, new LoaderManager.LoaderCallbacks<Cursor>() {
//            @Override
//            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//                return new CursorLoader(getActivity(),
//                        TransfersContract.UsersEntry.CONTENT_URI,
//                        new String[]{TransfersContract.UsersEntry.COLUMN_FULLNAME,TransfersContract.UsersEntry.COLUMN_EMAIL},
//                        TransfersContract.UsersEntry._ID+" = ?",
//                        new String[]{Integer.toString(userID)},
//                        null);
//            }
//
//            @Override
//            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//                data.moveToFirst();
////                detailsTemp[0]=data.getString(USERS_INDEX_FULLNAME);
////                Toast.makeText(getActivity(),detailsTemp[0],Toast.LENGTH_LONG).show();
////                Log.v("User ID","User ID:"+detailsTemp[0]);
////                detailsTemp[1]=data.getString(USERS_INDEX_EMAIL);
//
//                temp=temp+" Driver's name: "+data.getString(USERS_INDEX_FULLNAME)+
//                        " Driver's email: "+data.getString(USERS_INDEX_EMAIL);
//                myAdapter.add(temp);
//                Log.v("Result row","Result row"+temp);
//                temp="";
//            }
//
//            @Override
//            public void onLoaderReset(Loader<Cursor> loader) {
//
//
//            }
//        });
//    }
}

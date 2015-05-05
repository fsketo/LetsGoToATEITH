package com.example.user.letsgotoateith;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;

import static com.example.user.letsgotoateith.Utilities.printArray;

/**
 * Created by user on 28/4/2015.
 */
public class ResultsFragment extends Fragment {

//    private ArrayList<String> myDataset=new ArrayList<>();
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    ListView results;
    private String[][] idTemp;
    private static final int QUERY_LOADER = 101;
    private static final int INSERT_LOADER = 101;
    //private static final int QUERY_LOADER2 = 102;
    private ArrayAdapter<String> myAdapter;
    // these indices must match the projection
    private static final int USERS_INDEX_ID = 10;
    private static final int USERS_INDEX_FULLNAME = 15;
    private static final int USERS_INDEX_EMAIL = 13;
    private static final int INDEX_DAY = 2;
    private static final int INDEX_DEP_TIME = 3;
    private static final int INDEX_RET_TIME = 4;
    private static final int INDEX_FREQ = 5;
    private static final int INDEX_DRIVER_ID = 7;
    private static final int INDEX_AREA = 8;
    private static final int INDEX_ID=0;


    public ResultsFragment() {
        }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String text);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_results, container, false);
//            if(!MainActivity.mTwoPane) {
//                Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//                ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
//                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            }
//            else {
//                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.toolbar_Linear);
//                ll.removeViewAt(0);
//            }


//            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//            mRecyclerView.setHasFixedSize(true);
//            // use a linear layout manager
//            mLayoutManager = new LinearLayoutManager(getActivity());
//            mRecyclerView.setLayoutManager(mLayoutManager);
//            // specify an adapter
//            mAdapter = new MyAdapter(myDataset);
//            ((MyAdapter)mAdapter).setListener(new MyAdapter.Callbacks() {
//                @Override
//                public void onClick(String text) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/html");
//                    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
//                    while (m.find()) {
//                        intent.putExtra(Intent.EXTRA_EMAIL, m.group());
//                    }
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Let's go to ATEITH");
//                    intent.putExtra(Intent.EXTRA_TEXT, text);
//
//                    startActivity(Intent.createChooser(intent, "Send Email"));
//                }
//            });

            results = (ListView) rootView.findViewById(R.id.resultListview);
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
                    //Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(results.getItemAtPosition(position).toString());
                    //while (m.find()) {
                        //String tmp=m.group();
                        //Log.v("Email***********", "Email: " + temp[13]);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{idTemp[position][2]});
                    //}
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Let's go to ATEITH");
                    intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(results.getItemAtPosition(position).toString()));

                    //registerTransp(position);

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });

            return rootView;
        }

        private void findCars(){
            Log.v("Search URI", "Search URI :" + getActivity().getIntent().getStringExtra(Constants.EXTRA_URI));
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
                    String temp[] = new String[14];
                    int count = 0;
                    String[] tempTimeSpinnerValues = getResources().getStringArray(R.array.timeSpinner);
                    String[] tempFreqSpinnerValues = getResources().getStringArray(R.array.freqSpinner);
                    String[] tempDaySpinnerValues = getResources().getStringArray(R.array.weekDaySpinner);
                    String[] tempAreaSpinnerValues = getResources().getStringArray(R.array.areaSpinner);
                    Log.v("Results count","Results count: "+data.getCount());
                    if (data.moveToFirst()) {
                        idTemp = new String[data.getCount()][3];
                        data.moveToFirst();
                        do {
                            temp[0] = "Day: ";
                            temp[1] = tempDaySpinnerValues[data.getInt(INDEX_DAY)];
                            temp[2] = " Departure time: ";
                            temp[3] = tempTimeSpinnerValues[data.getInt(INDEX_DEP_TIME)];
                            temp[4] = " Return time: ";
                            temp[5] = tempTimeSpinnerValues[data.getInt(INDEX_RET_TIME)];
                            temp[6] = " Frequency: ";
                            temp[7] = tempFreqSpinnerValues[data.getInt(INDEX_FREQ)];
                            temp[8] = " Area: ";
                            temp[9] = tempAreaSpinnerValues[data.getInt(INDEX_AREA)];
                            temp[10] = " Driver's name: ";
                            temp[11] = data.getString(USERS_INDEX_FULLNAME);
                            temp[12] = " Driver's email: ";
                            temp[13] = data.getString(USERS_INDEX_EMAIL);

                            idTemp[count][0] = data.getString(INDEX_ID);
                            idTemp[count][1] = data.getString(USERS_INDEX_ID);
                            idTemp[count][2] = data.getString(USERS_INDEX_EMAIL);


                            count++;

                            Log.v("Query Result", "Query Result: " + printArray(temp));

                            //findName(data.getInt(INDEX_DRIVER_ID));
                            myAdapter.add(printArray(temp));
//                          myDataset.add(temp);
                            temp = new String[14];
                        } while (data.moveToNext());
                    } else {
                        //myDataset.add("No results found");
                        myAdapter.add("No results found");
                    }

                    results.setAdapter(myAdapter);
                    //mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {


                }
            });


        }

//    private void registerTransp(int position){
//        Uri uri= TransfersContract.TransportsEntry.buildTranspUri();
//        ContentValues mNewValues = new ContentValues();
//
//        mNewValues.put(TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID,idTemp[position][0]);
//        mNewValues.put(TransfersContract.TransportsEntry.COLUMN_USER_ID,idTemp[position][1]);
//        mNewValues.put(TransfersContract.TransportsEntry.COLUMN_CONFIRMED_D,1);
//        mNewValues.put(TransfersContract.TransportsEntry.COLUMN_CONFIRMED_U,1);
//
//        new AsyncTask<Object, Void, Void>() {
//            @Override
//            protected Void doInBackground(Object... params) {
//                Context context=getActivity();
//                context.getContentResolver().insert((Uri)params[0],(ContentValues)params[1]);
//                return null;
//            }
//        }.execute(uri, mNewValues);
//
//
//        Dialog dialog = new Dialog(getActivity(), getString(R.string.transpRegPopup), getString(R.string.transpPopUp));
//        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//
//        dialog.show();
//    }

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
//                    it.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(it);

                    //NavUtils.navigateUpTo(getActivity(),new Intent(getActivity(), LoginActivity.class));
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
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

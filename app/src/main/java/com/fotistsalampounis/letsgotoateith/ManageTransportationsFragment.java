package com.fotistsalampounis.letsgotoateith;

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
import android.widget.TextView;

import com.example.user.letsgotoateith.R;
import com.fotistsalampounis.letsgotoateith.data.TransfersContract;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;

import static com.fotistsalampounis.letsgotoateith.Utilities.printArray;


public class ManageTransportationsFragment extends Fragment {

    private static final int QUERY_LOADER = 150;
    private static final int USERS_QUERY_LOADER = 151;
    private static final int USERS_INDEX_DRIVERSNAME = 11;
    private String[][] transpTempPass, transpTempDr;
    private ListView resultsPass;
    private ArrayAdapter<String> myAdapterPass;
    private ListView resultsDriver;
    private ArrayAdapter<String> myAdapterDriver;
    private final int INDEXreg_car_idtransp=0;
    private final int INDEXconfirmed_u=1;
    private final int INDEXconfirmed_d=2;
    private final int INDEXuser_idtransp=3;
    private final int INDEXregcar_id=4;
    private final int INDEXpeople=5;
    private final int INDEXday=6;
    private final int INDEXdep_time=7;
    private final int INDEXret_time=8;
    private final int INDEXfreq=9;
    private final int INDEXpeople_reg=10;
    private final int INDEXdriver_id=11;
    private final int INDEXarea=12;
    private final int INDEXusername=13;
    private final int INDEXuserarea=14;
    private final int INDEXschool=15;
    private final int INDEXuser_id=16;
    private  final int INDEXemail=17;
    private final int INDEXfb=18;
    private final int INDEXfullname=19;
    private String userIDS[];
    private boolean flagDr, flagPass;
    private SharedPreferences prefs1;
    private SharedPreferences.Editor editor1;

    public ManageTransportationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_manage_transportations, container, false);

        flagDr=true;
        flagPass=true;
        prefs1=getActivity().getSharedPreferences(getString(R.string.pref_key2), Context.MODE_PRIVATE);
        editor1=prefs1.edit();
        resultsPass = (ListView) rootView.findViewById(R.id.resultListviewPass);

        TextView textView1 = new TextView(getActivity());
        textView1.setText("Transports as driver");
        TextView textView2 = new TextView(getActivity());
        textView2.setText("Transports as passenger");


        myAdapterPass = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList());
        resultsPass.addHeaderView(textView2);
        resultsPass.setAdapter(myAdapterPass);

        resultsDriver = (ListView) rootView.findViewById(R.id.resultListviewDr);
        myAdapterDriver = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList());
        resultsDriver.addHeaderView(textView1);
        resultsDriver.setAdapter(myAdapterDriver);

        findUserTransports();

        resultsPass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (!flagPass) {
                        Log.v("Temp ID", transpTempPass[position - 1][16]);
                        findUsersInTransport(transpTempPass[position - 1][16], false);
                    }
                }
            }
        });

        resultsDriver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("Position","Position "+position);
                if (position > 0) {
                    if (!flagDr) {

                        findUsersInTransport(transpTempDr[position - 1][16], true);
                    }
                }
            }
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    private void findUsersInTransport(final String reg_car_id1, final boolean isDriver) {
        getLoaderManager().destroyLoader(USERS_QUERY_LOADER);
        getLoaderManager().initLoader(USERS_QUERY_LOADER,null,new LoaderManager.LoaderCallbacks<Cursor>(){

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri=TransfersContract.TransportsEntry.buildUsersTranspUri();
                return new CursorLoader(getActivity(),
                        uri,
                        new String[]{TransfersContract.TransportsEntry.COLUMN_USER_ID},
                        TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID+" = ?",
                        new String[]{reg_car_id1},
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(data.moveToFirst()){
                    userIDS=new String[data.getCount()];
                    int count=0;
                    data.moveToFirst();
                    do{
                        userIDS[count]=data.getString(0);
                        count++;
                    }while(data.moveToNext());
                    Intent intent = new Intent(getActivity(), TransportDetailsActivity.class);
//                    intent.putExtra(Constants.EXTRA_ARRAY, userIDS);
//                    intent.putExtra(Constants.EXTRA_REGCARID,reg_car_id);
//                    intent.putExtra(Constants.EXTRA_ISDRIVER,!flagDr);

                    editor1.putString(Constants.EXTRA_REGCARID, reg_car_id1);
                    editor1.putBoolean(Constants.EXTRA_ISDRIVER, isDriver);
                    editor1.putInt("array_size", userIDS.length);
                    for(int i=0;i<userIDS.length; i++)
                        editor1.putString("array_" + i, userIDS[i]);
                    editor1.commit();
                    startActivity(intent);
                }
                Log.v("Position","Position "+reg_car_id1);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

    }

    private void findUserTransports(){
       final Uri uri= TransfersContract.TransportsEntry.buildTranspUri(getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555));
        getLoaderManager().initLoader(QUERY_LOADER, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(getActivity(),
                        uri,
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                myAdapterPass.clear();
                myAdapterDriver.clear();
                //String temp[] = new String[20];
                int countDr = 0;
                int countPass = 0;

                String[] tempTimeSpinnerValues = getResources().getStringArray(R.array.timeSpinner);
                String[] tempFreqSpinnerValues = getResources().getStringArray(R.array.freqSpinner);
                String[] tempDaySpinnerValues = getResources().getStringArray(R.array.weekDaySpinner);
                String[] tempAreaSpinnerValues = getResources().getStringArray(R.array.areaSpinner);
                if (data.moveToFirst()) {
                    String[] columnNames =data.getColumnNames();
                    Log.v("Column names","Column names "+ Utilities.printArray(columnNames));
                    transpTempDr = new String[data.getCount()][26];
                    transpTempPass = new String[data.getCount()][26];
                    data.moveToFirst();
                    do {
                        if(data.getInt(INDEXdriver_id)==getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555)) {

                            transpTempDr[countDr][0] = "Day: ";
                            transpTempDr[countDr][1] = tempDaySpinnerValues[data.getInt(INDEXday)];
                            transpTempDr[countDr][2] = " | Departure time: ";
                            transpTempDr[countDr][3] = tempTimeSpinnerValues[data.getInt(INDEXdep_time)];
                            transpTempDr[countDr][4] = " | Return time: ";
                            transpTempDr[countDr][5] = tempTimeSpinnerValues[data.getInt(INDEXret_time)];
                            transpTempDr[countDr][6] = " | Frequency: ";
                            transpTempDr[countDr][7] = tempFreqSpinnerValues[data.getInt(INDEXfreq)];
                            transpTempDr[countDr][8] = " | Area: ";
                            transpTempDr[countDr][9] = tempAreaSpinnerValues[data.getInt(INDEXarea)];
                            transpTempDr[countDr][10] =" | People Registered: ";
                            transpTempDr[countDr][11] =String.valueOf(data.getInt(INDEXpeople_reg));

                            transpTempDr[countDr][12]=data.getString(INDEXreg_car_idtransp);
                            transpTempDr[countDr][13]=data.getString(INDEXconfirmed_u);
                            transpTempDr[countDr][14]=data.getString(INDEXconfirmed_d);
                            transpTempDr[countDr][15]=data.getString(INDEXuser_idtransp);
                            transpTempDr[countDr][16]=data.getString(INDEXregcar_id);
                            transpTempDr[countDr][17]=data.getString(INDEXpeople);
                            transpTempDr[countDr][18]=data.getString(INDEXdriver_id);
                            transpTempDr[countDr][19]=data.getString(INDEXusername);
                            transpTempDr[countDr][20]=data.getString(INDEXuserarea);
                            transpTempDr[countDr][21]=data.getString(INDEXschool);
                            transpTempDr[countDr][22]=data.getString(INDEXuser_id);
                            transpTempDr[countDr][23]=data.getString(INDEXemail);
                            transpTempDr[countDr][24]=data.getString(INDEXfb);
                            transpTempDr[countDr][25]=data.getString(INDEXfullname);


                            flagDr=false;


                            Log.v("Query Result", "Query Result: " + Utilities.printArray(transpTempDr, countDr, 9));

                            //findName(data.getInt(INDEX_DRIVER_ID));
                            myAdapterDriver.add(Utilities.printArray(transpTempDr, countDr, 10));
                            countDr++;
                        }
                        else{
                            transpTempPass[countPass][0] = "Driver's name: ";
                            transpTempPass[countPass][1] = data.getString(INDEXfullname);
                            transpTempPass[countPass][2] = " | Day: ";
                            transpTempPass[countPass][3] = tempDaySpinnerValues[data.getInt(INDEXday)];
                            transpTempPass[countPass][4] = " | Departure time: ";
                            transpTempPass[countPass][5] = tempTimeSpinnerValues[data.getInt(INDEXdep_time)];
                            transpTempPass[countPass][6] = " | Return time: ";
                            transpTempPass[countPass][7] = tempTimeSpinnerValues[data.getInt(INDEXret_time)];
                            transpTempPass[countPass][8] = " | Frequency: ";
                            transpTempPass[countPass][9] = tempFreqSpinnerValues[data.getInt(INDEXfreq)];
                            transpTempPass[countPass][10] = " | Area: ";
                            transpTempPass[countPass][11] = tempAreaSpinnerValues[data.getInt(INDEXarea)];

                            transpTempPass[countPass][12]=data.getString(INDEXreg_car_idtransp);
                            transpTempPass[countPass][13]=data.getString(INDEXconfirmed_u);
                            transpTempPass[countPass][14]=data.getString(INDEXconfirmed_d);
                            transpTempPass[countPass][15]=data.getString(INDEXuser_idtransp);
                            transpTempPass[countPass][16]=data.getString(INDEXregcar_id);
                            transpTempPass[countPass][17]=data.getString(INDEXpeople);
                            transpTempPass[countPass][18]=data.getString(INDEXdriver_id);
                            transpTempPass[countPass][19]=data.getString(INDEXusername);
                            transpTempPass[countPass][20]=data.getString(INDEXuserarea);
                            transpTempPass[countPass][21]=data.getString(INDEXschool);
                            transpTempPass[countPass][22]=data.getString(INDEXuser_id);
                            transpTempPass[countPass][23]=data.getString(INDEXemail);
                            transpTempPass[countPass][24]=data.getString(INDEXfb);
                            transpTempPass[countPass][25]=data.getString(INDEXpeople_reg);

                            flagPass=false;

                            Log.v("Query Result", "Query Result: " + Utilities.printArray(transpTempPass, countPass, 11));

                            myAdapterPass.add(Utilities.printArray(transpTempPass, countPass, 12));
                            countPass++;
                        }

                    } while (data.moveToNext());
                    if(flagDr)
                        myAdapterDriver.add("No registered transports as a driver");
                }
                if (flagPass)
                    myAdapterPass.add("You haven't registered in any transports yet!");

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Log.v("Own a car menu selected","Own a car menu selected");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Log.v("logout selected","logout selected");
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

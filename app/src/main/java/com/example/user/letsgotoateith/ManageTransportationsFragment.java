package com.example.user.letsgotoateith;

import android.content.Context;
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
import android.widget.TextView;

import com.example.user.letsgotoateith.data.TransfersContract;

import java.util.ArrayList;

import static com.example.user.letsgotoateith.Utilities.printArray;


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
    public ManageTransportationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_manage_transportations, container, false);

        flagDr=true;
        flagPass=true;

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

        resultsPass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    if(!flagPass)
                        findUsersInTransport(transpTempPass[position - 1][16]);
                }
            }
        });

        resultsDriver = (ListView) rootView.findViewById(R.id.resultListviewDr);
        myAdapterDriver = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList());
        resultsDriver.addHeaderView(textView1);
        resultsDriver.setAdapter(myAdapterDriver);

        findUserTransports();

        resultsDriver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("Position","Position "+position);
                if(position>0) {
                    if(!flagDr)
                        findUsersInTransport(transpTempDr[position - 1][16]);
                }
            }
        });
        return rootView;
    }

    private void findUsersInTransport(final String reg_car_id) {
        getLoaderManager().destroyLoader(USERS_QUERY_LOADER);
        getLoaderManager().initLoader(USERS_QUERY_LOADER,null,new LoaderManager.LoaderCallbacks<Cursor>(){

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri=TransfersContract.TransportsEntry.buildUsersTranspUri();
                return new CursorLoader(getActivity(),
                        uri,
                        new String[]{TransfersContract.TransportsEntry.COLUMN_USER_ID},
                        TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID+" = ?",
                        new String[]{reg_car_id},
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
                    intent.putExtra(Constants.EXTRA_ARRAY, userIDS);
                    intent.putExtra(Constants.EXTRA_REGCARID,reg_car_id);
                    startActivity(intent);
                }
                Log.v("Position","Position "+reg_car_id);
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
                //String temp[] = new String[20];
                int count = 0;

                String[] tempTimeSpinnerValues = getResources().getStringArray(R.array.timeSpinner);
                String[] tempFreqSpinnerValues = getResources().getStringArray(R.array.freqSpinner);
                String[] tempDaySpinnerValues = getResources().getStringArray(R.array.weekDaySpinner);
                String[] tempAreaSpinnerValues = getResources().getStringArray(R.array.areaSpinner);
                if (data.moveToFirst()) {
                    String[] columnNames =data.getColumnNames();
                    Log.v("Column names","Column names "+printArray(columnNames));
                    transpTempDr = new String[data.getCount()][26];
                    transpTempPass = new String[data.getCount()][26];
                    data.moveToFirst();
                    do {
                        if(data.getInt(INDEXdriver_id)==getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555)) {

                            transpTempDr[count][0] = "Day: ";
                            transpTempDr[count][1] = tempDaySpinnerValues[data.getInt(INDEXday)];
                            transpTempDr[count][2] = " | Departure time: ";
                            transpTempDr[count][3] = tempTimeSpinnerValues[data.getInt(INDEXdep_time)];
                            transpTempDr[count][4] = " | Return time: ";
                            transpTempDr[count][5] = tempTimeSpinnerValues[data.getInt(INDEXret_time)];
                            transpTempDr[count][6] = " | Frequency: ";
                            transpTempDr[count][7] = tempFreqSpinnerValues[data.getInt(INDEXfreq)];
                            transpTempDr[count][8] = " | Area: ";
                            transpTempDr[count][9] = tempAreaSpinnerValues[data.getInt(INDEXarea)];
                            transpTempDr[count][10] =" | People Registered: ";
                            transpTempDr[count][11] =String.valueOf(data.getInt(INDEXpeople_reg));

                            transpTempDr[count][12]=data.getString(INDEXreg_car_idtransp);
                            transpTempDr[count][13]=data.getString(INDEXconfirmed_u);
                            transpTempDr[count][14]=data.getString(INDEXconfirmed_d);
                            transpTempDr[count][15]=data.getString(INDEXuser_idtransp);
                            transpTempDr[count][16]=data.getString(INDEXregcar_id);
                            transpTempDr[count][17]=data.getString(INDEXpeople);
                            transpTempDr[count][18]=data.getString(INDEXdriver_id);
                            transpTempDr[count][19]=data.getString(INDEXusername);
                            transpTempDr[count][20]=data.getString(INDEXuserarea);
                            transpTempDr[count][21]=data.getString(INDEXschool);
                            transpTempDr[count][22]=data.getString(INDEXuser_id);
                            transpTempDr[count][23]=data.getString(INDEXemail);
                            transpTempDr[count][24]=data.getString(INDEXfb);
                            transpTempDr[count][25]=data.getString(INDEXfullname);


                            flagDr=false;


                            Log.v("Query Result", "Query Result: " + printArray(transpTempDr,count,9));

                            //findName(data.getInt(INDEX_DRIVER_ID));
                            myAdapterDriver.add(printArray(transpTempDr,count,12));
                            count++;
                        }
                        else{
                            transpTempPass[count][0] = "Driver's name: ";
                            transpTempPass[count][1] = data.getString(INDEXfullname);
                            transpTempPass[count][2] = " | Day: ";
                            transpTempPass[count][3] = tempDaySpinnerValues[data.getInt(INDEXday)];
                            transpTempPass[count][4] = " | Departure time: ";
                            transpTempPass[count][5] = tempTimeSpinnerValues[data.getInt(INDEXdep_time)];
                            transpTempPass[count][6] = " | Return time: ";
                            transpTempPass[count][7] = tempTimeSpinnerValues[data.getInt(INDEXret_time)];
                            transpTempPass[count][8] = " | Frequency: ";
                            transpTempPass[count][9] = tempFreqSpinnerValues[data.getInt(INDEXfreq)];
                            transpTempPass[count][10] = " | Area: ";
                            transpTempPass[count][11] = tempAreaSpinnerValues[data.getInt(INDEXarea)];

                            transpTempPass[count][12]=data.getString(INDEXreg_car_idtransp);
                            transpTempPass[count][13]=data.getString(INDEXconfirmed_u);
                            transpTempPass[count][14]=data.getString(INDEXconfirmed_d);
                            transpTempPass[count][15]=data.getString(INDEXuser_idtransp);
                            transpTempPass[count][16]=data.getString(INDEXregcar_id);
                            transpTempPass[count][17]=data.getString(INDEXpeople);
                            transpTempPass[count][18]=data.getString(INDEXdriver_id);
                            transpTempPass[count][19]=data.getString(INDEXusername);
                            transpTempPass[count][20]=data.getString(INDEXuserarea);
                            transpTempPass[count][21]=data.getString(INDEXschool);
                            transpTempPass[count][22]=data.getString(INDEXuser_id);
                            transpTempPass[count][23]=data.getString(INDEXemail);
                            transpTempPass[count][24]=data.getString(INDEXfb);
                            transpTempPass[count][25]=data.getString(INDEXpeople_reg);

                            flagPass=false;

                            Log.v("Query Result", "Query Result: " + printArray(transpTempPass,count,11));

                            myAdapterPass.add(printArray(transpTempPass, count, 12));
                            count++;
                        }

                    } while (data.moveToNext());
                    if(flagDr)
                        myAdapterDriver.add("No registered transports as a driver");
                }
                if (flagPass)
                    myAdapterPass.add("You haven't registerd in any transports yet!");

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {


            }
        });

    }
}

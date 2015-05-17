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
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;

import static com.fotistsalampounis.letsgotoateith.Utilities.printArray;


public class TransportDetailsActivityFragment extends Fragment {

    String[] usid;
    private static final int QUERY_LOADER = 160;
    private int INDEX_username=0;
    private int INDEX_area=1;
    private int INDEX_school=2;
    private int INDEX_id=3;
    private int INDEX_email=4;
    private int INDEX_fb=5;
    private int INDEX_fullname=6;
    private String selec=TransfersContract.UsersEntry._ID+ "  = ? ";
    private String[][] usersCopy;
    private ArrayAdapter<String> myAdapter;
    private android.widget.ListView results;
    private Intent intent;
    private SharedPreferences prefs1;
    private SharedPreferences.Editor editor1;


    public TransportDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        intent = getActivity().getIntent();
//        if (savedInstanceState != null) {
//            // Restore last state for checked position.
//            usid = savedInstanceState.getStringArray("userids");
//        }
//        else
//            usid=intent.getStringArrayExtra(Constants.EXTRA_ARRAY);

        prefs1=getActivity().getSharedPreferences(getString(R.string.pref_key2), Context.MODE_PRIVATE);
        editor1=prefs1.edit();

        int size = prefs1.getInt("array_size", 0);
        usid = new String[size];
        for(int i=0; i<size; i++)
            usid[i]=prefs1.getString("array_" + i, null);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       View rootView=inflater.inflate(R.layout.fragment_transport_details, container, false);




            checkUser();

        results = (ListView) rootView.findViewById(R.id.resultListview);
        TextView textView2 = new TextView(getActivity());
        textView2.setText("Users in Transport");

        myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList());
            results.addHeaderView(textView2);
            results.setAdapter(myAdapter);

            results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] sendTemp = new String[14];
                    if (position > 0) {
                        for (int j = 0; j < 14; j++) {
                            sendTemp[j] = usersCopy[position - 1][j];
                        }
                        Intent intent = new Intent(getActivity(), DisplayTranspUserRegActivity.class);
                        intent.putExtra(Constants.EXTRA_ARRAY, sendTemp);
                        startActivity(intent);
                    }
                }
            });

        results.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(prefs1.getBoolean(Constants.EXTRA_ISDRIVER,false)) {
                    if (usersCopy[position - 1][13].equals(Integer.toString(getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555)))) {
                        Dialog dialog = new Dialog(getActivity(), getString(R.string.deleteTransportTitle), getString(R.string.deleteTransport));
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Log.v("On Long Click", "On Long Click");
                                getActivity().getContentResolver().delete(Uri.parse(TransfersContract.CarsEntry.CONTENT_URI + "/0/0/0/0/0"),
                                            TransfersContract.CarsEntry._ID + " = ? ",
                                        new String[]{prefs1.getString(Constants.EXTRA_REGCARID, null)});

                                myAdapter.notifyDataSetChanged();

//                                getActivity().finish();

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("com.package.ACTION_REFRESH");
                                getActivity().sendBroadcast(broadcastIntent);
//                                Intent it = new Intent(getActivity(), MainActivity.class);
//                                startActivity(it);
                            }
                        });

                        dialog.addCancelButton(getString(R.string.dialogCancel));
                        dialog.show();
                    }
                    else{
                        Dialog dialog = new Dialog(getActivity(), getString(R.string.deleteFromTransportTitle), getString(R.string.deleteFromTransport1));
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Log.v("On Long Click", "On Long Click");
                                getActivity().getContentResolver().delete(Uri.parse(TransfersContract.TransportsEntry.CONTENT_URI + "/0"),
                                        TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID + " = ? AND " +
                                                TransfersContract.TransportsEntry.COLUMN_USER_ID + " = ? ",
                                        new String[]{prefs1.getString(Constants.EXTRA_REGCARID, null), usersCopy[position - 1][13]});

                                myAdapter.notifyDataSetChanged();
//                                getActivity().finish();
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("com.package.ACTION_REFRESH");
                                getActivity().sendBroadcast(broadcastIntent);
//                                Intent it = new Intent(getActivity(), MainActivity.class);
//                                startActivity(it);
                            }
                        });
                        dialog.addCancelButton(getString(R.string.dialogCancel));
                        dialog.show();
                    }
                }
                else if (usersCopy[position - 1][13].equals(Integer.toString(getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555)))) {
                    Dialog dialog = new Dialog(getActivity(), getString(R.string.deleteFromTransportTitle), getString(R.string.deleteFromTransport));
                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.v("On Long Click", "On Long Click");
                            getActivity().getContentResolver().delete(Uri.parse(TransfersContract.TransportsEntry.CONTENT_URI + "/0"),
                                    TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID + " = ? AND " +
                                            TransfersContract.TransportsEntry.COLUMN_USER_ID + " = ? ",
                                    new String[]{prefs1.getString(Constants.EXTRA_REGCARID, null), usersCopy[position - 1][13]});

                            myAdapter.notifyDataSetChanged();

                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("com.package.ACTION_REFRESH");
                            getActivity().sendBroadcast(broadcastIntent);
//                            Intent it = new Intent(getActivity(), MainActivity.class);
//                            startActivity(it);
//                            getActivity().finish();
                        }
                    });
                    dialog.addCancelButton(getString(R.string.dialogCancel));

                    dialog.show();
                }

                return true;
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    private void checkUser(){

        for (int i = 0; i < usid.length - 1; i++) {
            selec = selec + " OR " + TransfersContract.UsersEntry._ID + "  = ? ";
        }
        getLoaderManager().initLoader(QUERY_LOADER, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(getActivity(),
                        TransfersContract.UsersEntry.CONTENT_URI,
                        null,
                        selec,
                        usid,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                myAdapter.clear();
                if (data.moveToFirst()) {

                    String[] tempAreaSpinnerValues = getResources().getStringArray(R.array.areaSpinner);
                    usersCopy = new String[data.getCount()][14];
                    data.moveToFirst();
                    int count = 0;
                    do {
                        usersCopy[count][0] = " Username: ";
                        usersCopy[count][1] = data.getString(INDEX_username);
                        usersCopy[count][2] = " Full name: ";
                        usersCopy[count][3] = data.getString(INDEX_fullname);
                        usersCopy[count][4] = " Area: ";
                        usersCopy[count][5] = tempAreaSpinnerValues[data.getInt(INDEX_area)];
                        usersCopy[count][6] = " School: ";
                        usersCopy[count][7] = data.getString(INDEX_school);
                        usersCopy[count][8] = " E-mail: ";
                        usersCopy[count][9] = data.getString(INDEX_email);
                        usersCopy[count][10] = " Facebook profile link: ";
                        usersCopy[count][11] = data.getString(INDEX_fb);
                        usersCopy[count][12] = " User ID: ";
                        usersCopy[count][13] = data.getString(INDEX_id);
                        myAdapter.add(Utilities.printArray(usersCopy, count, 4));
                        count++;
                    } while (data.moveToNext());

                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {


            }
        });


    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        savedInstanceState.putStringArray("userids", usid);
//    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            // Restore last state for checked position.
//            usid = savedInstanceState.getStringArray("userids");
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Log.v("Own a car menu selected", "Own a car menu selected");
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

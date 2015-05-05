package com.example.user.letsgotoateith;

import android.content.ContentValues;
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
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;

/**
 * Created by user on 18/4/2015.
 */
public class OwnACarFragment extends Fragment{

        private int userId;
        private String username;
        View rootView;
//        Callback Listener;
//
//        public void setListener(Callback listener) {
//            Listener = listener;
//        }

        public OwnACarFragment() {
        }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_own_car, container, false);
//            if(!MainActivity.mTwoPane) {
//                Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//                ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
//                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                Log.v("Two pane","Two pane: "+MainActivity.mTwoPane);
//            }
//            else{
//                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.toolbar_Linear);
//                ll.removeViewAt(0);
//                Log.v("Two pane","Two pane: "+MainActivity.mTwoPane);
//
//            }
            //((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Let's go to ATEITH");
            ButtonRectangle submitButton=(ButtonRectangle)rootView.findViewById(R.id.submit_button);

            submitButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View flatButton) {
                    Dialog dialog = new Dialog(getActivity(), getString(R.string.ownAcarDataPopUpTitle), getString(R.string.ownAcarDataPopUp));
                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new Dialog(getActivity(), getString(R.string.ownAcarPopUpTitle), getString(R.string.ownAcarPopUp));
                            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = getActivity().getIntent();
                                    //if (intent != null && intent.hasExtra(Constants.EXTRA_USERNAME)) {
                                        username = intent.getStringExtra(Constants.EXTRA_USERNAME);
                                        userId=getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555);
                                        //userId = intent.getIntExtra(Constants.EXTRA_USERID, -15555);
                                        Log.v("USER ID ALALA","USER ID ALALA "+getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE).getInt(Constants.EXTRA_USERID, -5555));
                                    //}
                                    insertQuery(rootView);
                                    if (!MainActivity.mTwoPane)
                                        getActivity().finish();
                                }
                            });
                            dialog.show();
                        }
                    });
                    dialog.addCancelButton(getString(R.string.dialogCancel),new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if(!MainActivity.mTwoPane)
                                getActivity().finish();
                            Log.v("Cancel Button","Cancel Button");
                        }
                    });
                    dialog.show();
                }
            });




//                    setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(getActivity())
//                            .setMessage(R.string.ownAcarPopUp)
//                            .setPositiveButton(R.string.dialog_resume, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Intent intent = getActivity().getIntent();
//                                    if (intent != null && intent.hasExtra(Constants.EXTRA_USERNAME)) {
//                                        username = intent.getStringExtra(Constants.EXTRA_USERNAME);
//                                        userId = intent.getIntExtra(Constants.EXTRA_USERID, -1);
//                                    }
//                                    insertQuery(rootView);
//                                    if(!MainActivity.mTwoPane)
//                                        getActivity().finish();
//                                }
//                            }).show();
//                }
//
//            });
            return rootView;
        }

    public void insertQuery(View rootview){
        Spinner sp;

        sp=(Spinner)rootview.findViewById(R.id.howManyPeopleSpinner);
        int people=sp.getSelectedItemPosition();
        sp=(Spinner)rootview.findViewById(R.id.whichDaySpinner);
        int day=sp.getSelectedItemPosition();
        sp=(Spinner)rootview.findViewById(R.id.depTimeSpinner);
        int depTime=sp.getSelectedItemPosition();
        sp=(Spinner)rootview.findViewById(R.id.retTimeSpinner);
        int retTime=sp.getSelectedItemPosition();
        sp=(Spinner)rootview.findViewById(R.id.freqSpinner);
        int freq=sp.getSelectedItemPosition();
        sp=(Spinner)rootview.findViewById(R.id.areaSpinner);
        int area=sp.getSelectedItemPosition();

        Uri uri= TransfersContract.CarsEntry.buildRegUri(people,day,depTime,retTime,freq,area);

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(TransfersContract.CarsEntry.COLUMN_PEOPLE, people);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_DAY, day);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_DEP_TIME, depTime);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_RET_TIME, retTime);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_FREQ, freq);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_DRIVER_ID, userId);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_PEOPLE_REG, 0);
        mNewValues.put(TransfersContract.CarsEntry.COLUMN_AREA, area);
        Context context=getActivity();
        context.getContentResolver().insert(uri, mNewValues);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.mTwoPane)
            return true;
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_own_car, menu);
//    }

//    public interface Callback {
//        /**
//         * FragmentCallback for when an item has been selected.
//         */
//        public void onOptionsItemSelected(MenuItem item, final Activity context);
//    }
}

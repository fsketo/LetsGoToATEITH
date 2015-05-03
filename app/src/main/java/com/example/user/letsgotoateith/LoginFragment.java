package com.example.user.letsgotoateith;

/**
 * Created by user on 14/4/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.letsgotoateith.data.TransfersContract;
import com.gc.materialdesign.views.ButtonRectangle;

public class LoginFragment extends Fragment {

    String usernameStr;
    private static final int QUERY_LOADER = 100;
    private EditText username;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public LoginFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        editor=prefs.edit();
//        editor.putInt(getString(R.string.pref_id_key),-1);
//        editor.putString(getString(R.string.pref_username_key),"-1");
//        editor.commit();
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_login, menu);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        int loggedInUserID=prefs.getInt(getString(R.string.pref_id_key),-1);
//        if(loggedInUserID!=-1){
//            String loggedInUsername=prefs.getString(getActivity().getString(R.string.pref_username_key),"-1");
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.putExtra(Constants.EXTRA_USERNAME, loggedInUsername);
//            intent.putExtra(Constants.EXTRA_USERID, loggedInUserID);
//            startActivity(intent);
//        }
        //Log.v("User ID", "User ID: " + loggedInUserID);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

//        Toolbar toolbar=(Toolbar) rootView.findViewById(R.id.toolbar);
//        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        //((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Let's go to ATEITH");

        ButtonRectangle loginButton =(ButtonRectangle)rootView.findViewById(R.id.loginButton);
        TextView aboutButton=(TextView)rootView.findViewById(R.id.aboutText);
        TextView registerButton=(TextView)rootView.findViewById(R.id.registerText);
        username=(EditText)rootView.findViewById(R.id.username);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameStr=username.getText().toString();
                checkUser();

            }

        });
        EditText pass=(EditText)rootView.findViewById(R.id.password);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Password not supported yet", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void checkUser(){

        getLoaderManager().initLoader(QUERY_LOADER, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(getActivity(),
                        TransfersContract.UsersEntry.CONTENT_URI,
                        new String[]{TransfersContract.UsersEntry._ID},
                        TransfersContract.UsersEntry.COLUMN_USERNAME+" = ?",
                        new String[]{usernameStr},
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(data.moveToFirst()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(Constants.EXTRA_USERNAME, usernameStr);
                    intent.putExtra(Constants.EXTRA_USERID, data.getInt(0));
                    editor.putInt(getString(R.string.pref_id_key), data.getInt(0));
                    editor.putString(getString(R.string.pref_username_key), usernameStr);
                    editor.commit();
                    Log.v("User ID", "#####********User ID:" + prefs.getInt(getActivity().getString(R.string.pref_id_key), -5555));
                    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{
                    username.setText("");
                    username.setHint("Wrong Username");
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {


            }
        });


    }

}

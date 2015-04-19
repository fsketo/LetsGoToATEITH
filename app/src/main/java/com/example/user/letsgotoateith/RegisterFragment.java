package com.example.user.letsgotoateith;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.letsgotoateith.data.TransfersContract;

/**
 * Created by user on 19/4/2015.
 */
public class RegisterFragment extends Fragment {

    EditText username,fullname,school,email,facebookLink;
    Spinner area;
    public RegisterFragment() {
    }

    private String usernameS,fullnameS,schoolS,emailS,facebookS;
    private int areaS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        fullname=(EditText)rootView.findViewById(R.id.fullname);
        username=(EditText)rootView.findViewById(R.id.username);
        school=(EditText)rootView.findViewById(R.id.school);
        email=(EditText)rootView.findViewById(R.id.email);
        facebookLink=(EditText)rootView.findViewById(R.id.facebookLink);
        area=(Spinner)rootView.findViewById(R.id.areaSpinner);
        Button signUp=(Button) rootView.findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameS= username.getText().toString();
                areaS=area.getSelectedItemPosition();
                fullnameS=fullname.getText().toString();

                schoolS=school.getText().toString();

                emailS= email.getText().toString();

                if(!facebookLink.getText().toString().trim().isEmpty()) {
                    facebookS = facebookLink.getText().toString();
                }

                if(usernameS.trim().isEmpty()){
                    username.setText("Insert Username");
                    username.setBackgroundColor( -65536);
                }
                else if(fullnameS.trim().isEmpty()){
                    fullname.setText("Insert Full name");
                    fullname.setBackgroundColor( -65536);
                }
                else if(schoolS.trim().isEmpty()){
                    school.setText("Insert School");
                    school.setBackgroundColor( -65536);
                }
                else if(emailS.trim().isEmpty()){
                    email.setText("Insert Email");
                    email.setBackgroundColor( -65536);
                    System.out.println(emailS);
                }
                else if(!emailS.trim().contains("@teithe.gr")){
                    System.out.println(emailS);
                    email.setText("You can only use your Teithe email address");
                    email.setBackgroundColor( -65536);
                }
                else{
                    Uri uri= TransfersContract.UsersEntry.buildUserUri();
                    ContentValues mNewValues = new ContentValues();

                    mNewValues.put(TransfersContract.UsersEntry.COLUMN_USERNAME, usernameS);
                    mNewValues.put(TransfersContract.UsersEntry.COLUMN_FULLNAME, fullnameS);
                    mNewValues.put(TransfersContract.UsersEntry.COLUMN_SCHOOL, schoolS);
                    mNewValues.put(TransfersContract.UsersEntry.COLUMN_AREA, areaS);
                    mNewValues.put(TransfersContract.UsersEntry.COLUMN_EMAIL, emailS);
                    if(facebookS!=null) {
                        mNewValues.put(TransfersContract.UsersEntry.COLUMN_FB, facebookS);
                    }
                    else
                        mNewValues.put(TransfersContract.UsersEntry.COLUMN_FB, "No Facebook Data");

                    new AsyncTask<Object, Void, Void>() {
                        @Override
                        protected Void doInBackground(Object... params) {
                            Context context=getActivity();
                            context.getContentResolver().insert((Uri)params[0],(ContentValues)params[1]);
                            return null;
                        }
                    }.execute(uri,mNewValues);

                }
            }

        });
        return rootView;
    }
}

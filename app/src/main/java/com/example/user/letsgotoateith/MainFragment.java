package com.example.user.letsgotoateith;

/**
 * Created by user on 14/4/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment{

    private String username;
    private Integer userId;
    private Button ownAcarButton,lookingForCarButton,manageTransButton;
    public MainFragment() {
    }



    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int button);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USERNAME)) {
            username = intent.getStringExtra(Constants.EXTRA_USERNAME);
            userId=intent.getIntExtra(Constants.EXTRA_USERID,-1);
            getActivity().setTitle(getString(R.string.title_activity_main)+" "+username+" "+userId);
        }

        ownAcarButton =(Button)rootView.findViewById(R.id.ownAcar);
        lookingForCarButton =(Button)rootView.findViewById(R.id.lookingForCar);
        manageTransButton =(Button)rootView.findViewById(R.id.manageTrans);

        ownAcarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.mTwoPane)
                    ((Callback)getActivity()).onItemSelected(R.id.ownAcar);
                else{
                    Intent intent = new Intent(getActivity(), OwnCarActivity.class);
                    intent.putExtra(Constants.EXTRA_USERNAME,username);
                    intent.putExtra(Constants.EXTRA_USERID,userId);
                    startActivity(intent);
                }

            }

        });

        lookingForCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mTwoPane)
                    ((Callback)getActivity()).onItemSelected(R.id.lookingForCar);
                else{
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra(Constants.EXTRA_USERNAME,username);
                    intent.putExtra(Constants.EXTRA_USERID,userId);
                    startActivity(intent);
                }

            }

        });

        manageTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mTwoPane)
                    ((Callback)getActivity()).onItemSelected(R.id.manageTrans);
                else{
                    Toast.makeText(getActivity(),"Function not supported yet!",Toast.LENGTH_LONG).show();
                }

            }

        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}

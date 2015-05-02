package com.example.user.letsgotoateith;

/**
 * Created by user on 14/4/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment{

    private String username;
    private Integer userId;
    private Button ownAcarButton,lookingForCarButton,manageTransButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Intent intent = getActivity().getIntent();
        if (!MainActivity.mTwoPane) {
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            //((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Log.v("Two pane", "OWN A CAR Two pane: " + MainActivity.mTwoPane);
        } else {
            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.toolbar_Linear);
            ll.removeViewAt(0);
            Log.v("Two pane", "OWN A CAR Two pane: " + MainActivity.mTwoPane);
        }

        if (intent != null && intent.hasExtra(Constants.EXTRA_USERNAME)) {
            username = intent.getStringExtra(Constants.EXTRA_USERNAME);
            userId=intent.getIntExtra(Constants.EXTRA_USERID, -1);
            if (!MainActivity.mTwoPane) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_activity_main) + " " + username);
            }
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<String> myDataset=new ArrayList<>(3);
        myDataset.add(getString(R.string.ownAcar));
        myDataset.add(getString(R.string.lookingForCar));
        myDataset.add(getString(R.string.manageTrans));
        mAdapter = new MyAdapter(myDataset);
        ((MyAdapter)mAdapter).setListener(new MyAdapter.Callbacks() {
            @Override
            public void onClick(String text) {
                if (MainActivity.mTwoPane)
                    ((Callback)getActivity()).onItemSelected(text);
                else if(text.equals(getString(R.string.ownAcar))){
                    Intent intent = new Intent(getActivity(), OwnCarActivity.class);
                    intent.putExtra(Constants.EXTRA_USERNAME,username);
                    intent.putExtra(Constants.EXTRA_USERID,userId);
                    startActivity(intent);
                }
                else if(text.equals(getString(R.string.lookingForCar))){
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra(Constants.EXTRA_USERNAME,username);
                    intent.putExtra(Constants.EXTRA_USERID,userId);
                    startActivity(intent);
                }
                else if(text.equals(getString(R.string.manageTrans))){
                    Toast.makeText(getActivity(), "Function not supported yet!", Toast.LENGTH_LONG).show();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String tv= (String) ((TextView) v.findViewById(R.id.firstLine)).getText();
//                if (MainActivity.mTwoPane)
//                    ((Callback)getActivity()).onItemSelected(tv);
//                else if(tv.equals(getString(R.string.ownAcar))){
//                    Intent intent = new Intent(getActivity(), OwnCarActivity.class);
//                    intent.putExtra(Constants.EXTRA_USERNAME,username);
//                    intent.putExtra(Constants.EXTRA_USERID,userId);
//                    startActivity(intent);
//                }
//                else if(tv.equals(getString(R.string.lookingForCar))){
//                    Intent intent = new Intent(getActivity(), SearchActivity.class);
//                    intent.putExtra(Constants.EXTRA_USERNAME,username);
//                    intent.putExtra(Constants.EXTRA_USERID,userId);
//                    startActivity(intent);
//                }
//                else if(tv.equals(getString(R.string.manageTrans))){
//                    Toast.makeText(getActivity(), "Function not supported yet!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        ownAcarButton =(Button)rootView.findViewById(R.id.ownAcar);
//        lookingForCarButton =(Button)rootView.findViewById(R.id.lookingForCar);
//        manageTransButton =(Button)rootView.findViewById(R.id.manageTrans);
//
//        ownAcarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(MainActivity.mTwoPane)
//                    ((Callback)getActivity()).onItemSelected(R.id.ownAcar);
//                else{
//                    Intent intent = new Intent(getActivity(), OwnCarActivity.class);
//                    intent.putExtra(Constants.EXTRA_USERNAME,username);
//                    intent.putExtra(Constants.EXTRA_USERID,userId);
//                    startActivity(intent);
//                }
//
//            }
//
//        });
//
//        lookingForCarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (MainActivity.mTwoPane)
//                    ((Callback) getActivity()).onItemSelected(R.id.lookingForCar);
//                else {
//                    Intent intent = new Intent(getActivity(), SearchActivity.class);
//                    intent.putExtra(Constants.EXTRA_USERNAME, username);
//                    intent.putExtra(Constants.EXTRA_USERID, userId);
//                    startActivity(intent);
//                }
//
//            }
//
//        });
//
//        manageTransButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (MainActivity.mTwoPane)
//                    ((Callback) getActivity()).onItemSelected(R.id.manageTrans);
//                else {
//                    Toast.makeText(getActivity(), "Function not supported yet!", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Utilities.logout(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}

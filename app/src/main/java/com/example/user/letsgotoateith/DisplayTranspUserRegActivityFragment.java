package com.example.user.letsgotoateith;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.user.letsgotoateith.Utilities.printArray;


/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayTranspUserRegActivityFragment extends Fragment {

    private String[] userdata;
    private ListView results;
    private ArrayAdapter<String> myAdapter;

    public DisplayTranspUserRegActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_display_transp_user_reg, container, false);

        userdata=new String[14];

        Intent intent = getActivity().getIntent();
        userdata=intent.getStringArrayExtra(Constants.EXTRA_ARRAY);

        results = (ListView) rootView.findViewById(R.id.listview);
        TextView textView2 = new TextView(getActivity());
        textView2.setText("Users details");

        myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList());
        results.addHeaderView(textView2);
        results.setAdapter(myAdapter);

        int j2=0;

        for (int j = 0; j < 7; j++) {
            myAdapter.add(userdata[j2] + userdata[j2 + 1]);
            j2 = j2 + 2;
        }

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 5) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{userdata[9]});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Let's go to ATEITH");
                    intent.putExtra(Intent.EXTRA_TEXT, printArray(userdata));
                    startActivity(Intent.createChooser(intent, "Send Email"));

                } else if (position == 6) {
                    if(!userdata[11].equals("No Facebook Data")){
                        String url = "http://www.facebook.com/"+userdata[9];
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
            }
        });

        return rootView;
    }


}

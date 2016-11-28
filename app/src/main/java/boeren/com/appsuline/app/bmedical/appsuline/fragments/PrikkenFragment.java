package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrikkenFragment extends android.support.v4.app.Fragment {


    public PrikkenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_prikken, container, false);


        return rootview;
    }


}

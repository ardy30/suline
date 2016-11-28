package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InjectielocatiesFragment extends android.support.v4.app.Fragment {


    public InjectielocatiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_injectielocaties, container, false);
        TextView tv = (TextView) rootview.findViewById(R.id.tv_injectie);
        ImageView imview = (ImageView) rootview.findViewById(R.id.imview_injectieplaatsen);
        //imview.setImageResource();
        tv.setMovementMethod(new ScrollingMovementMethod());


        return rootview;
    }


}

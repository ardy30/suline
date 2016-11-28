package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends android.support.v4.app.Fragment {


    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_feedback, container, false);
        TextView tvfeedback1 = (TextView)rootview.findViewById(R.id.tv_feebackp1);
        Linkify.addLinks(tvfeedback1, Linkify.ALL);
        return rootview;
    }


}

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
public class OverBoerenMedicalFragment extends android.support.v4.app.Fragment {


    public OverBoerenMedicalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_over_boeren_medical, container, false);
        TextView tv = (TextView)rootview.findViewById(R.id.tv_overwebdiabetes);
        TextView tvservice = (TextView)rootview.findViewById(R.id.tv_overwebdiabetes_service);
        Linkify.addLinks(tv , Linkify.ALL);
        Linkify.addLinks(tvservice , Linkify.PHONE_NUMBERS);
        return rootview;
    }


}

package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.utils.MeterPreferenceDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothmeterFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

   Button btnv4meterconnect;
   Meterv4fragment meterDialog;
    private boolean isDualPan = false;

    public BluetoothmeterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity().findViewById(R.id.container2) != null)isDualPan = true;
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_bluetoothmeter, container, false);
        btnv4meterconnect = (Button) rootview.findViewById(R.id.btnv4meterconnect);
        btnv4meterconnect.setOnClickListener(this);
        return rootview;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnv4meterconnect: /** AlerDialog when click on Exit */
                // call the Fragment
                ShowMeterDialog();

                break;


        }
    }
    private void ShowMeterDialog() {
        meterDialog = Meterv4fragment.newInstance();
       // meterDialog.show(getActivity().getSupportFragmentManager(), "FRAGMENT_SETTING_METER");
        Meterv4fragment fragment = Meterv4fragment.newInstance();
        // meterDialog.show(getActivity().getSupportFragmentManager(), "FRAGMENT_SETTING_METER");
        if (fragment != null) {
            int containerId = R.id.container;
            if(isDualPan){
                containerId = R.id.container2;
                fragmentPopUp();
            }
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                    .replace(containerId, fragment).addToBackStack(null)
                    .commit();
           // adapter.notifyDataSetInvalidated();
        }
    }
    private void fragmentPopUp(){
        if( getFragmentManager().getBackStackEntryCount()>0)
            getFragmentManager().popBackStack();
    }
}

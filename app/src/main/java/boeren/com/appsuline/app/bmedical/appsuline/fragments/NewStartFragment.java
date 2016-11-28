package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewStartFragment extends DialogFragment {


    public NewStartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_start, container, false);
        Button btnfill = (Button)rootview.findViewById(R.id.btn_filldetails);
        btnfill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               DialogFragment fragment = new PersonalInfoDialogFragment();
                fragment.setCancelable(false);
                fragment.show(getActivity().getSupportFragmentManager(),"");

                dismiss();
//                    if(fragment!=null) {
//                        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
//                                .replace(R.id.container, fragment).addToBackStack(null)
//                                .commit();
//
//
//                    }

            }

        });



        return rootview;
    }


}

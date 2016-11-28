package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodDialogFragment extends BaseDialogFragment{


    private DiaryDetailFragment d;

    public BloodDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    protected String getStringTag() {
        return "BloedwaardeDialog";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getEntry().setEntryType(LogBookEntry.Type.BLOOD);

        inflateMainView(inflater, R.layout.fragment_bloedwaarde_dialog, container, false);

        addTextWatcher(getMainViw().findViewById(R.id.etBloodLevelAmount));
        addTextWatcher(getMainViw().findViewById(R.id.etComment));

        setCancelView(getMainViw().findViewById(R.id.btnCancel));
        setSaveView(getMainViw().findViewById(R.id.btnSave));

        getDialog().setTitle(R.string.bloedwaarde);
        getEntry().setEntryName(getString(R.string.bloedwaarde));
        Button btnTime = (Button) getMainViw().findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);
       // Adding the delete icon to the dialog
        if(isUpdateFlow()) {
            TextView title = (TextView) getDialog().findViewById(android.R.id.title);
            if(title!=null) {
                ViewGroup viewGroup = (ViewGroup) title.getRootView();
                RelativeLayout titleItemsContainer = new RelativeLayout(getActivity());
                titleItemsContainer.setLayoutParams(((ViewGroup) title.getParent()).getLayoutParams());
                ImageView iconDel = new ImageView(getActivity());
                iconDel.setImageResource(R.drawable.ic_trashcan);
                iconDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != getEntry()) {
                            BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().delete(getEntry());

                        }
                        notifyDataSave();
                        getDialog().dismiss();

                    }
                });

                RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                deleteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                iconDel.setLayoutParams(deleteParams);
                titleItemsContainer.addView(iconDel);
                viewGroup.addView(titleItemsContainer);
            }
        }

        if(isUpdateFlow()){
             btnTime.setText(getEntry().getEntryTime());

            ((EditText)getMainViw().findViewById(R.id.etBloodLevelAmount)).setText(String.valueOf(getEntry().getEntryAmount()));
            ((EditText)getMainViw().findViewById(R.id.etComment)).setText(getEntry().getEntryComment());
        }else{
            btnTime.setText(Utilities.getInstance().getCurrentFormattedTime());
            getEntry().setEntryTime(btnTime.getText().toString());
        }

        return getMainViw();
    }

    @Override
    public void onClick(final View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnTime:
                showTimePickerDialogWithTime((Button)v);
                break;
        }
    }
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }
    private android.support.v4.app.Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? getActivity().getSupportFragmentManager().findFragmentByTag(Integer.toString(index)) : null;
    }
    protected android.support.v4.app.Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount() - 1);
    }

    private void showTimePickerDialogWithTime(final Button v) {
        String setTime=((Button)v).getText().toString();
        try {
            Utilities.getInstance().showTimePickerDialogWithTime(getActivity(),setTime,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    v.setText(Utilities.getInstance().getFormattedTime(hourOfDay,minute));
                    getEntry().setEntryTime(v.getText().toString());
                }
            });

        }catch (ParseException parseException){

        }
    }

    @Override
    protected void afterDialogTextChanged(EditText editText, Editable s) {

            switch (editText.getId()) {

                case R.id.etBloodLevelAmount:
                    try{
                        double value = Double.valueOf(s.toString());
                        getEntry().setEntryAmount(value);
                    }catch (NumberFormatException numberFormatException){
                        getEntry().setEntryAmount(0);
                    }
                    break;
                case R.id.etComment:
                    getEntry().setEntryComment(s.toString());
                    break;

            }

    }

}

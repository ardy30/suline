package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.utils.CalendarManagerNew;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DialogEditingListener;

/**
 * Created by Jamil on 20-1-2015.
 */

public class TussendoortjeDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, CheckBox.OnCheckedChangeListener {

    private Button btntime;
    private ImageView btnCancel, btnSave;
    private ListView listView;
    private int xhour;
    private int xminute;
    private TimePickerDialog timePicker;
    private CheckBox cbSunday, cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday;
    String su, mo, tu, we, th, fr, sa = "";
    private DialogEditingListener mCallback;
    private CalendarEvent mEvent;

    public TussendoortjeDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public void setEvent(CalendarEvent event) {
        this.mEvent = event;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_tussendoortje, container);

        btntime = (Button) view.findViewById(R.id.btn_bloedmetentime);
        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);
        btnSave = (ImageView) view.findViewById(R.id.btnSave);

        (cbSunday = (CheckBox) view.findViewById(R.id.cb_Sunday)).setOnCheckedChangeListener(this);
        (cbMonday = (CheckBox) view.findViewById(R.id.cb_monday)).setOnCheckedChangeListener(this);
        (cbTuesday = (CheckBox) view.findViewById(R.id.cb_tuesday)).setOnCheckedChangeListener(this);
        (cbWednesday = (CheckBox) view.findViewById(R.id.cb_wednesday)).setOnCheckedChangeListener(this);
        (cbThursday = (CheckBox) view.findViewById(R.id.cb_Thurday)).setOnCheckedChangeListener(this);
        (cbFriday = (CheckBox) view.findViewById(R.id.cb_Friday)).setOnCheckedChangeListener(this);
        (cbSaturday = (CheckBox) view.findViewById(R.id.cb_Saturday)).setOnCheckedChangeListener(this);

        btntime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Process to get Current Time
                final Calendar c = Calendar.getInstance();
                xhour = c.get(Calendar.HOUR_OF_DAY);
                xminute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                xhour = hourOfDay;
                                xminute = minute;
                                btntime.setText(new StringBuilder().append(padding_str(hourOfDay)).append(":").append(padding_str(minute)));
                                mEvent.setEventEndTime(String.format("%02d:%02d",xhour,xminute));
                                Log.e("appsuline", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().update(mEvent) + "");
                                mCallback.onCheckChangeCallback(-1);

                            }
                        }, xhour, xminute, true);
                tpd.show();
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("appsuline del", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().delete(mEvent) + "");
                mCallback.onCheckChangeCallback(-1);
                getDialog().dismiss();
            }

        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CalendarManagerNew cm = new CalendarManagerNew();
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, xhour);
                calendar.set(Calendar.MINUTE, xminute);

                cm.addRepeatingReminder(getActivity(),calendar, mEvent.getEventID());
                getDialog().dismiss();
            }

        });

        getDialog().setTitle(R.string.tussendoortje);
        setCurrentTimeOnView();
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            mCallback.onFinishEditDialog(btntime.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    // display current time + 1hr
    public void setCurrentTimeOnView() {

        final Calendar c = Calendar.getInstance();
             c.add(Calendar.HOUR_OF_DAY,1);
        xhour = c.get(Calendar.HOUR_OF_DAY);
        xminute = c.get(Calendar.MINUTE);

        // set current time into textview
        btntime.setText(String.format("%02d:%02d",xhour,xminute));


    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            xhour = selectedHour;
            xminute = selectedMinute;

            // set current time into textview
            btntime.setText(String.format("%02d:%02d",xhour,xminute));
           // System.out.println(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));
           /* // set current time into timepicker
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);*/

        }
    };

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_monday:
                mEvent.setMandage(isChecked);
                break;
            case R.id.cb_tuesday:
                mEvent.setDinsdag(isChecked);
                break;
            case R.id.cb_wednesday:
                mEvent.setWoensdag(isChecked);
                break;
            case R.id.cb_Thurday:
                mEvent.setDonderdag(isChecked);
                break;
            case R.id.cb_Friday:
                mEvent.setVrijdag(isChecked);
                break;
            case R.id.cb_Saturday:
                mEvent.setZaterdag(isChecked);
                break;
            case R.id.cb_Sunday:
                mEvent.setZondage(isChecked);
                break;

        }
        BaseController.getInstance().getDbManager(getActivity()).getEventsTable().update(mEvent);
        if (mCallback != null) {
            mCallback.onCheckChangeCallback(buttonView.getId());
        }
    }

    public void registerCallbacks(DialogEditingListener callback) {
        this.mCallback = callback;
    }
}
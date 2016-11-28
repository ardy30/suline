package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.utils.CalendarManagerNew;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DateUtils;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DialogEditingListener;

/**
 * Created by Jamil on 20-1-2015.
 */

public class ProductBestellenDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    static private Button btntime, btnorderdate;
    private ImageView btnCancel, btnSave;
    private ListView listView;
    private int xhour;
    private int xminute;
    public static int xDay, xMonth, xYear;
    private TimePickerDialog timePicker;
    private EditText edit_naamproduct;
    private CalendarEvent mEvent;
    private DialogEditingListener mCallback;


    public ProductBestellenDialogFragment() {
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
        View view = inflater.inflate(R.layout.layout_productbestellen, container);

        btntime = (Button) view.findViewById(R.id.btn_bloedmetentime);
        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);
        btnSave = (ImageView) view.findViewById(R.id.btnSave);
        btnorderdate = (Button) view.findViewById(R.id.btn_dateofororder);
        edit_naamproduct = (EditText) view.findViewById(R.id.edit_naamproduct);
        btnorderdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showStartDatePickerDialog(view);
            }

        });
        initCurrentDate();
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
                                mEvent.setEventEndTime(new StringBuilder().append(padding_str(hourOfDay)).append(":").append(padding_str(minute)).toString());
                                System.out.println((new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)).toString()));
                                if (TextUtils.isEmpty(edit_naamproduct.getText().toString().trim()))
                                    edit_naamproduct.setText(R.string.product);
                                mEvent.setEventTitle(edit_naamproduct.getText().toString().trim());
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
                if (TextUtils.isEmpty(edit_naamproduct.getText().toString().trim()))
                    edit_naamproduct.setText(R.string.product);
                CalendarManagerNew cm = new CalendarManagerNew();
                mEvent.setEventTitle(edit_naamproduct.getText().toString().trim());
                mEvent.setEventEndDate(DateUtils.getDateString(xDay, xMonth, xYear));
                mEvent.setEventEndTime(String.format("%02d:%02d",xhour,xminute));
                Log.e("appsuline", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().update(mEvent) + "");
                mCallback.onCheckChangeCallback(-1);
                cm.addReminder(getActivity(), xDay, xMonth, xYear, xhour, xminute, mEvent.getEventID());
                getDialog().dismiss();
            }

        });

        getDialog().setTitle(R.string.productbestllen);
        setCurrentTimeOnView();
        setorderdateonView();
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

    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = new OrderDatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    // display current date
    public void setorderdateonView() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        c.add(Calendar.DAY_OF_MONTH, 1);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // set current time into textview
        btnorderdate.setText(new StringBuilder().append((day)).append(" ").append((getMonth(month))).append(" ").append((year)));
    }

    // display current time
    public void setCurrentTimeOnView() {

        final Calendar c = Calendar.getInstance();
        xhour = c.get(Calendar.HOUR_OF_DAY);
        xminute = c.get(Calendar.MINUTE);

        // set current time into textview
        btntime.setText(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));


    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            xhour = selectedHour;
            xminute = selectedMinute;

            // set current time into textview
            btntime.setText(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));
            System.out.println(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));


        }
    };

    public void initCurrentDate() {
        final Calendar c = Calendar.getInstance();
        xYear = c.get(Calendar.YEAR);
        xMonth = c.get(Calendar.MONTH);
        c.add(Calendar.DAY_OF_MONTH, 1);
        xDay = c.get(Calendar.DAY_OF_MONTH);
    }


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static class OrderDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, xYear, xMonth, xDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            // set current time into textview
            xMonth = month;
            xDay = day;
            xYear = year;
            btnorderdate.setText(new StringBuilder().append((day)).append(" ").append((getMonth(month))).append(" ").append((year)));
        }


    }

    public void registerCallbacks(DialogEditingListener callback) {
        this.mCallback = callback;
    }
}
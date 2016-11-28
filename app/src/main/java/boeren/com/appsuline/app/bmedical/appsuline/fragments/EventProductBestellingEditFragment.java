package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.utils.CalendarManagerNew;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DateUtils;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DialogEditingListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventProductBestellingEditFragment extends DialogFragment implements TextView.OnEditorActionListener {

    static private Button btntime, btnorderdate;
    private ImageView btnCancel, btnSave;
    private int xhour;
    private int xminute;
    public static int xDay, xMonth, xYear;

    private EditText edit_naamproduct;
    private DialogEditingListener mCallback;
    private CalendarEvent mEvent;
    private static final String KEY_EVENT = "event";
    private boolean isDualPan = false;

    public static EventProductBestellingEditFragment newInstance(CalendarEvent event){
        EventProductBestellingEditFragment s = new EventProductBestellingEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_EVENT, event);
        s.setArguments(args);
        return s;
    }
    public EventProductBestellingEditFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);

    }
    public void setEvent(CalendarEvent event) {
        this.mEvent = event;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_eventdelete, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem shareItem = menu.findItem(R.id.action_eventdelete);
        shareItem.setActionView(R.layout.deleteiconlayout);
        ImageView img = (ImageView) shareItem.getActionView().findViewById(R.id.img_view_del);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("appsuline del", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().delete(mEvent) + "");
                if (isDualPan) {
                    HerinneringenFragment fragment = (HerinneringenFragment) getCurrentFragment();
                    fragment.loadEventsAndUpdateList();
                }

                getActivity().onBackPressed();

            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_product_bestelling_edit, container, false);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_EVENT)) {
            mEvent = (CalendarEvent) getArguments().getSerializable(KEY_EVENT);
        }
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
                //Log.e("appsuline del", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().delete(mEvent) + "");
                //mCallback.onCheckChangeCallback(-1);
               // getDialog().dismiss();
                getActivity().onBackPressed();

            }

        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CalendarManagerNew cm = new CalendarManagerNew();
                mEvent.setEventTitle(edit_naamproduct.getText().toString().trim());
                mEvent.setEventEndDate(DateUtils.getDateString(xDay, xMonth, xYear));
                Log.e("appsuline", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().update(mEvent) + "");
                mCallback.onCheckChangeCallback(-1);
                cm.addReminder(getActivity(), xDay, xMonth, xYear, xhour, xminute, mEvent.getEventID());
                getActivity().onBackPressed();
            }

        });

        getActivity().setTitle(R.string.productbestllen);
        setCurrentEvent();
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
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }
    private android.support.v4.app.Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? getActivity().getSupportFragmentManager().findFragmentByTag(Integer.toString(index)) : null;
    }
    protected android.support.v4.app.Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount());
    }
    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = new OrderDatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void setCurrentEvent() {

        if(this.mEvent!=null) {
            Calendar c=Calendar.getInstance();
            DateFormat format=new SimpleDateFormat("dd/mm/yyyy");
            Date formatedDate;
            formatedDate =  DateUtils.getDateFromString(mEvent.getEventEndDate());
            format.format(formatedDate);
            c=format.getCalendar();

            xYear = c.get(Calendar.YEAR);
            xMonth = c.get(Calendar.MONTH);
            xDay = c.get(Calendar.DAY_OF_MONTH);
            btnorderdate.setText(new StringBuilder().append((xDay)).append(" ").append((getMonth(xMonth))).append(" ").append((xYear)));
            System.out.println(mEvent.getEventEndTime());
            btntime.setText(mEvent.getEventEndTime());
            edit_naamproduct.setText(mEvent.getEventTitle());
        }
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

            final Calendar c = Calendar.getInstance();
            xYear = c.get(Calendar.YEAR);
            xMonth = c.get(Calendar.MONTH);
            c.add(Calendar.DAY_OF_MONTH, 1);
            xDay = c.get(Calendar.DAY_OF_MONTH);


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

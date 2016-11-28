package boeren.com.appsuline.app.bmedical.appsuline.fragments;


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
 * A simple {@link Fragment} subclass.
 */
public class EventInsulineSpuitenEditFragment extends DialogFragment implements TextView.OnEditorActionListener, CheckBox.OnCheckedChangeListener {


    private Button btntime;
    private ImageView btnCancel, btnSave;
    //private ListView listView;
    private int xhour;
    private int xminute;
    //private TimePickerDialog timePicker;
    private CheckBox cbSunday, cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday;

    private CalendarEvent mEvent,initialEvent;
    private static final String KEY_EVENT = "event";
    private boolean isDualPan = false;
    private DialogEditingListener mCallback;

    public static EventInsulineSpuitenEditFragment newInstance(CalendarEvent event) {
        EventInsulineSpuitenEditFragment s = new EventInsulineSpuitenEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_EVENT, event);
        s.setArguments(args);
        return s;
    }
    public EventInsulineSpuitenEditFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);
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
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }

    private android.support.v4.app.Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? getActivity().getSupportFragmentManager().findFragmentByTag(Integer.toString(index)) : null;
    }

    protected android.support.v4.app.Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount());
    }
    public void setEvent(CalendarEvent event) {
        this.mEvent = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_insuline_spuiten, container, false);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_EVENT)) {
            mEvent = (CalendarEvent) getArguments().getSerializable(KEY_EVENT);
            initialEvent = new CalendarEvent();
            initialEvent.copy(mEvent);
        }
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

                Log.e("appsuline del", BaseController.getInstance().getDbManager(getActivity()).getEventsTable().update(initialEvent) + "");
                mCallback.onCheckChangeCallback(-1);
                getActivity().onBackPressed();
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
                getActivity().onBackPressed();
            }

        });

        getActivity().setTitle(R.string.insulinespuiten);
        setCurrentEvent();
        return view;
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            if (mCallback != null) {
                mCallback.onFinishEditDialog(btntime.getText().toString());
            }
            //this.dismiss();
            return true;
        }
        return false;
    }
    private void setCurrentEvent() {


        if (this.mEvent != null) {
            btntime.setText(mEvent.getEventEndTime());
            if (mEvent.isMandage()) {
                cbMonday.setChecked(true);
                mEvent.setMandage(true);
            } else {
                cbMonday.setChecked(false);
                mEvent.setMandage(false);
            }
            if (mEvent.isDinsdag()) {
                cbTuesday.setChecked(true);
                mEvent.setDinsdag(true);
            } else {
                cbTuesday.setChecked(false);
                mEvent.setDinsdag(false);
            }

            if (mEvent.isWoensdag()) {
                cbWednesday.setChecked(true);
                mEvent.setWoensdag(true);
            } else {
                cbWednesday.setChecked(false);
                mEvent.setWoensdag(false);
            }
            if (mEvent.isDonderdag()) {
                cbThursday.setChecked(true);
                mEvent.setDonderdag(true);
            } else {
                cbThursday.setChecked(false);
                mEvent.setDonderdag(false);
            }
            if (mEvent.isVrijdag()) {
                cbFriday.setChecked(true);
                mEvent.setVrijdag(true);
            } else {
                cbFriday.setChecked(false);
                mEvent.setVrijdag(false);
            }
            if (mEvent.isZaterdag()) {
                cbSaturday.setChecked(true);
                mEvent.setZaterdag(true);
            } else {
                cbSaturday.setChecked(false);
                mEvent.setZaterdag(false);
            }
            if (mEvent.isZondage()) {
                cbSunday.setChecked(true);
                mEvent.setZondage(true);
            } else {
                cbSunday.setChecked(false);
                mEvent.setZondage(false);
            }
        }
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            xhour = selectedHour;
            xminute = selectedMinute;

            // set current time into textview
            btntime.setText(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));
            //System.out.println(new StringBuilder().append(padding_str(xhour)).append(":").append(padding_str(xminute)));
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

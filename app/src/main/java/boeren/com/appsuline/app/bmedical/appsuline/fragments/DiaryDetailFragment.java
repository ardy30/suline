package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarData;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.LogBookEntryViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryDetailFragment extends BaseFragment implements BaseDialogFragment.OnDialogFragmentDismissListener{

    public static final String ARGUMENT="Argument";

    public ActionMode actionMode;
    public LogBookEntry selectedEntry;
    private LogBookEntry dateInfoEntry;
    private CalendarData calendarData;
    Button btnAddNew;
    TextView tvcurrentday;
    Boolean clicked = false;
    String measuretype ="";
    private DiaryFragment diaryFragment;
    private EditText dayComment ;
    private boolean isDualPan = false;
    LinearLayout entriesContainer;

    private User activeUser;
    private Callbacks mCallbacks;
    //private Runnable monthCalendarCallback;
    private ScrollView scrollView;

   /* public void setMonthCalendarCallback(Runnable monthCalendarCallback) {
        this.monthCalendarCallback = monthCalendarCallback;
    }*/

   /* public Runnable getMonthCalendarCallback() {
        return monthCalendarCallback;
    }*/

    public interface Callbacks {
        public void onFavoritePressedCallback(boolean clicked);
    }
    private Runnable calendarRunnable;
    public DiaryDetailFragment() {
        // Required empty public constructor
    }

    public void setMonthCalendarCallback(Runnable calendarRunnable){
        this.calendarRunnable=calendarRunnable;
    }
    public static DiaryDetailFragment newInstance(Serializable argument){
        DiaryDetailFragment fragment=new DiaryDetailFragment();
        Bundle arg=new Bundle();
        arg.putSerializable(ARGUMENT,argument);
        fragment.setArguments(arg);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);
        diaryFragment = ((MainActivity)getActivity()).getDiaryFragment();
        // Fragment must implement the callback.
        if (!(diaryFragment instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }

        mCallbacks = (Callbacks) diaryFragment;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getArguments().containsKey(ARGUMENT)){
            calendarData=(CalendarData) getArguments().getSerializable(ARGUMENT);
        }

        activeUser=BaseController.getInstance().getActiveUser();
        dateInfoEntry=BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getDateInfoEntry(activeUser.getUserId(),calendarData.getDateInfo().getDateString());

        dateInfoEntry.setUserId(activeUser.getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateMainView(inflater,R.layout.fragment_diary_detail, container, false);

        btnAddNew = (Button) getMainView().findViewById(R.id.btnaddNew);
        tvcurrentday = (TextView) getMainView().findViewById(R.id.tv_dayNew);
        dayComment = (EditText) getMainView().findViewById(R.id.editText_comment);
        scrollView=(ScrollView)getMainView().findViewById(R.id.sv_detaillist);
        dayComment.setText(dateInfoEntry.getEntryComment());
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", calendarData.getCalendarDate());
        dayOfTheWeek = dayOfTheWeek.substring(0,1).toUpperCase() + dayOfTheWeek.substring(1).toLowerCase();
        tvcurrentday.setText(dayOfTheWeek);

        String daynMonth = (String) android.text.format.DateFormat.format("d MMMM", calendarData.getCalendarDate());
        getActivity().setTitle(daynMonth);

        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setCurrentTimeOnView();
        updateData();
        return getMainView();
    }

    public void updateData(){
        entriesContainer=(LinearLayout)getMainView().findViewById(R.id.entriesContainer);
        loadDataBaseEntries(entriesContainer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dagboekfav, menu);
        MenuItem shareItem = menu.findItem(R.id.action_favorite);
        shareItem.setActionView(R.layout.favoriteiconlayout);
        final ImageView img=(ImageView)shareItem.getActionView().findViewById(R.id.img_view_fav);
        if(dateInfoEntry.isFavourite()){
            clicked = true;
            img.setBackgroundResource(R.drawable.ic_favstar_sel);
        }else{
            clicked = false;
            img.setBackgroundResource(R.drawable.ic_favstar);
        }
        dayComment.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                // TO DO Add the day comment to the database

                dateInfoEntry.setEntryComment(s.toString());

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Need to add logic here that connects the custom calendar marking the selected day as important with color orange
                clicked =!clicked;
                if(clicked){
                    img.setBackgroundResource(R.drawable.ic_favstar_sel);
                }else{
                    img.setBackgroundResource(R.drawable.ic_favstar);
                }

                dateInfoEntry.setEntryType(LogBookEntry.Type.DATE_INFO);
                dateInfoEntry.setFavourite(clicked);
                dateInfoEntry.setEntryDate(calendarData.getDateInfo().getDateString());
                BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().insert(dateInfoEntry);
                dateInfoEntry=BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getDateInfoEntry(activeUser.getUserId(),calendarData.getDateInfo().getDateString());
                mCallbacks.onFavoritePressedCallback(clicked);
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayPopupWindow(v);
            }
        });
    }

    private void showDialog(LogBookEntry.Type type){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        LogBookEntry entry= new LogBookEntry();
        entry.setUserId(activeUser.getUserId());
        entry.setEntryDate(calendarData.getDateInfo().getDateString());

        BaseDialogFragment bloodDialogFragment = BaseDialogFragment.newInstance(getDialogOfEntry(type),entry);
        bloodDialogFragment.setDismissListener(this);
        bloodDialogFragment.show(fm, bloodDialogFragment.getStringTag());

    }


    // display current time
    public void setCurrentTimeOnView() {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // set current time into textview
        btnAddNew.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)).append("    ").append(getString(R.string.nieuweinvoer)));


    }
    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void displayPopupWindow(View anchorView) {

        LayoutInflater layoutInflater =(LayoutInflater)getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.reminer_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = (TextView)popupView.findViewById(R.id.poptitle);
        tv.setText(R.string.diarypopuptitle);
        ListView lv = (ListView)popupView.findViewById(R.id.poplistview);

        Resources res = getResources();
        String diarytypes []= res.getStringArray(R.array.diarytype_array);


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.popup_listitem,R.id.poptitle,diarytypes);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        showDialog(LogBookEntry.Type.MEAL);
//                        showMealDialog();
                        break;
                    case 1:
                        showDialog(LogBookEntry.Type.BLOOD);
//                        showBloodLevelDialog();
                        break;
                    case 2:
                        showDialog(LogBookEntry.Type.INSULIN);
//                        showInsulinDialog();
                        break;
                    case 3:
                        showDialog(LogBookEntry.Type.ACTIVITY);
//                        showActivityDialog();
                        break;
                }
                popupWindow.dismiss();

            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popupView);
        lv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setWidth(anchorView.getWidth()-100);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(anchorView,0,5);
    }

    private void loadDataBaseEntries(ViewGroup container){
        ArrayList<LogBookEntry> entries=BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getAllLogEntries(activeUser.getUserId(), calendarData.getDateInfo().getDateString());
        populateEntriesList(entries,container);

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            }
        });

    }
    private void populateEntriesList(ArrayList<LogBookEntry> entries,ViewGroup container){
        if(null==container){
            return;
        }
        container.removeAllViews();

        LayoutInflater inflater=LayoutInflater.from(getActivity());

        for (int i = 0; i < entries.size(); i++) {
            View entryRow=inflater.inflate(R.layout.row_log_book_entry,null);
            entryRow=populateView(entryRow,entries.get(i));
            container.addView(entryRow);
            entryRow.setTag(entries.get(i));
            entryRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogBookEntry logEntry=(LogBookEntry) v.getTag();
                    selectedEntry=(LogBookEntry)v.getTag();
                    BaseDialogFragment fragment=BaseDialogFragment.newInstance(getDialogOfEntry(logEntry.getEntryType()),logEntry);
                    fragment.setDismissListener(DiaryDetailFragment.this);
                    fragment.show(getActivity().getSupportFragmentManager(),fragment.getStringTag());
                }
            });

            entryRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    actionMode=getActivity().startActionMode(new ActionModCallBack());
                    selectedEntry=(LogBookEntry)v.getTag();

                    return true;
                }
            });
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
    private View populateView(View inflatedView,LogBookEntry entry){

        LogBookEntryViewHolder entryViewHolder=new LogBookEntryViewHolder(inflatedView);
        entryViewHolder.getEntryTime().setText(entry.getEntryTime());
        entryViewHolder.getEntryIcon().setImageResource(getEntryIcon(entry.getEntryType()));
        entryViewHolder.getEntryName().setText(entry.getEntryName());


        if(entry.getEntryType().equals(LogBookEntry.Type.BLOOD)){
            if(entry.getEntryAmount()<activeUser.getMinBloodLevel() || entry.getEntryAmount()>activeUser.getMaxBloodLevel()) {
                entryViewHolder.getAlertIcon().setVisibility(View.VISIBLE);
            }
            else {
                entryViewHolder.getAlertIcon().setVisibility(View.VISIBLE);
                entryViewHolder.getAlertIcon().setBackgroundResource(R.drawable.dot_green);
            }

                /// Here we will set the color

        }

        if(entry.getEntryType()!=LogBookEntry.Type.ACTIVITY) {
            entryViewHolder.getEntryAmount().setText(String.valueOf(entry.getEntryAmount() + " " + measuretype));

        }
        else {
            entryViewHolder.getEntryAmount().setText(entry.getEntryDuration());

        }

        return inflatedView;
    }

    private int getEntryIcon(LogBookEntry.Type type){
        switch (type){
            case ACTIVITY:
                return R.drawable.icon_activity;
            case BLOOD:
                measuretype = "mmol/L";
                return R.drawable.icon_value;
            case INSULIN:
                measuretype = "EH";
                return R.drawable.icon_insulin;
            case BREAK_FAST:
                measuretype = "Kh";
                return R.drawable.icon_breakfast;
            case LUNCH:
                measuretype = "Kh";
                return R.drawable.icon_lunch;
            case SNACK:
                measuretype = "Kh";
                return R.drawable.icon_snack;
            case DINNER:
                measuretype = "Kh";
                return R.drawable.icon_diner;
            case DRINK:
                measuretype = "Kh";
                return R.drawable.icon_drink;

        }
        return R.drawable.icon_value2;
    }

    private int getDialogOfEntry(LogBookEntry.Type type){
        switch (type){
            case ACTIVITY:
                return BaseDialogFragment.DIALOG_ACTIVITY;
            case BLOOD:
                return BaseDialogFragment.DIALOG_BLOOD;
            case INSULIN:
                return BaseDialogFragment.DIALOG_INSULIN;
            case BREAK_FAST:
            case LUNCH:
            case SNACK:
            case DINNER:
            case DRINK:
            case MEAL:
                return BaseDialogFragment.DIALOG_MEAL;

        }
        return BaseDialogFragment.DIALOG_MEAL;
    }

    @Override
    public void onDismiss(DialogFragment dialogFragment) {
        updateData();
    }

    @Override
    public void onDismissWithDataSaved(DialogFragment dialogFragment) {
        updateData();

         mCallbacks.onFavoritePressedCallback(clicked);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        dateInfoEntry.setEntryDate(calendarData.getDateInfo().getDateString());
        dateInfoEntry.setEntryType(LogBookEntry.Type.DATE_INFO);
        dateInfoEntry.setUserId(activeUser.getUserId());
        BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().insert(dateInfoEntry);
    }

    private class ActionModCallBack implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.menu_delete,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if(item.getItemId()==R.id.action_delete){
                if(null!=selectedEntry){
                    BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().delete(selectedEntry);
                    selectedEntry=null;
                    updateData();
                }
                actionMode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
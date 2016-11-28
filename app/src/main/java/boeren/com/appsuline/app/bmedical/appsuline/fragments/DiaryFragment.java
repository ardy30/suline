package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.MonthCalendarAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarData;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemClickListener, DiaryDetailFragment.Callbacks {


    private MonthCalendarAdapter adapter;
    private ListView calendarView;
    private TextView tvType;
    private boolean isDualPan = false;
    public static CalendarData btcurrentDate;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String ARGUMENT = "Argument";
    DiaryPDFDialogFragment pdfDialog = null;
    private LogBookEntry.Type myCategoryType;
    int lastposition =0;
    Utilities util;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiaryFragment() {
        // Required empty public constructor
        Log.d("DairyDetailFragment", "DiaryFragment() ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);
        User activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        BaseController.getInstance().setActiveUser(activeUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateMainView(inflater, R.layout.fragment_diary, container, false);
        getActivity().setTitle(R.string.dairy);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        util = new Utilities(getActivity());
        //util.saveValueToSharedPrefs("lastposition",Integer.toString(0));
        User activeUser = BaseController.getInstance().getActiveUser();
        if (null != activeUser) {
            calendarView = (ListView) getMainView().findViewById(R.id.lvCalendar);
//            calendarView.setSelector(R.drawable.list_selector);
            myCategoryType = LogBookEntry.Type.MEAL;
            adapter = new MonthCalendarAdapter(getActivity(),calendarView, myCategoryType);
            View footerView = getActivity().getLayoutInflater().inflate(R.layout.month_calendar_footer, null);
            footerView.findViewById(R.id.btnLoadMore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.loadMoreData();
                }
            });
            calendarView.addFooterView(footerView);
            calendarView.setAdapter(adapter);

            calendarView.setOnItemClickListener(this);
        } else {
            MainActivity.userCount = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getUserCount();
            if (MainActivity.userCount > 0) {
               // Toast.makeText(getActivity(), R.string.selectuser, Toast.LENGTH_LONG).show();
                calendarView = (ListView) getMainView().findViewById(R.id.lvCalendar);
                MainActivity.isFirstStart = false;
            } else {
                Toast.makeText(getActivity(), R.string.createuser, Toast.LENGTH_LONG).show();
                NewStartFragment dialog=new NewStartFragment();
                dialog.setCancelable(false);
                dialog.show(getActivity().getSupportFragmentManager(),"");
                MainActivity.isFirstStart = true;
            }

        }
        tvType = (TextView) getMainView().findViewById(R.id.tvType);

        // tvType =  (TextView) getMainView().findViewById(R.id.tvType);

        addCheckedChangedListener(getMainView().findViewById(R.id.rbtCarbohydrate));
        addCheckedChangedListener(getMainView().findViewById(R.id.rbtInsulin));
        addCheckedChangedListener(getMainView().findViewById(R.id.rbtBlood));
        // This loads the last selected date only if its dualpane at the first time
        changeTabSelection();
        loadLastDate();
        return getMainView();
    }

    private void changeTabSelection(){
        LogBookEntry.Type type=BaseController.getInstance().getSelectedCalendarType();
        if(null!=type){
            if(type.equals(LogBookEntry.Type.MEAL)){
                ((RadioButton)getMainView().findViewById(R.id.rbtCarbohydrate)).setChecked(true);
            }else if(type.equals(LogBookEntry.Type.INSULIN)){
                ((RadioButton)getMainView().findViewById(R.id.rbtInsulin)).setChecked(true);
            }else if(type.equals(LogBookEntry.Type.BLOOD)){
                ((RadioButton)getMainView().findViewById(R.id.rbtBlood)).setChecked(true);
            }
        }
    }

    private void loadLastDate() {



        if(isDualPan && adapter!=null){
            CalendarData data=null!=BaseController.getInstance().getSelectedCalendarData()?BaseController.getInstance().getSelectedCalendarData():adapter.getItem(0);

                fragmentPopUp();
                DiaryDetailFragment fragment=DiaryDetailFragment.newInstance(data);
            fragment.setMonthCalendarCallback(new Runnable() {
                @Override
                public void run() {
                    adapter = new MonthCalendarAdapter(getActivity(), calendarView, myCategoryType);
                    adapter.changeType(myCategoryType);
                    calendarView.setAdapter(adapter);
                }
            });
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
                        .replace(R.id.container2, fragment).addToBackStack(null)
                        .commit();

        }

    }

    private void fragmentPopUp() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dagboek, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareItem.setActionView(R.layout.pdficonlayout);
        ImageView img = (ImageView) shareItem.getActionView().findViewById(R.id.img_view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPdfDialog();
            }
        });


    }

    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }

    private void showPdfDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        pdfDialog = new DiaryPDFDialogFragment();
        pdfDialog.show(fm, "pdfdialog");

    }

    private void setTypeText(int resource) {
        if (tvType != null) {
            tvType.setText(resource);
        }

    }

    private void addCheckedChangedListener(View view) {

        if (view instanceof RadioButton) {
            RadioButton rbt = (RadioButton) view;
            rbt.setOnCheckedChangeListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            CalendarData calendarData=adapter.getItem(position);
            if(calendarData.getType().equals(CalendarData.DataType.MONTH)){
                return;
            }

            BaseController.getInstance().setSelectedCalendarData(calendarData);
            util.saveValueToSharedPrefs("lastposition", Integer.toString(position));
            lastposition = position;
        DiaryDetailFragment fragment = DiaryDetailFragment.newInstance(calendarData);

            if (fragment != null) {
                int containerId = R.id.container;
                if (isDualPan) {
                    containerId = R.id.container2;
                    fragmentPopUp();
                    fragment.setMonthCalendarCallback(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new MonthCalendarAdapter(getActivity(), calendarView, myCategoryType);
                            adapter.changeType(myCategoryType);
                            calendarView.setAdapter(adapter);
                        }
                    });
                    getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
                            .replace(containerId, fragment).addToBackStack(null)
                            .commit();
                } else {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(fragment)
                            .replace(containerId, fragment, Integer.toString(getFragmentCount())).addToBackStack(null)
                            .commit();
                }

            }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            switch (buttonView.getId()) {
                case R.id.rbtCarbohydrate:

                    setTypeText(R.string.totalkoolhydraten);
                    myCategoryType = LogBookEntry.Type.MEAL;
                    adapter = new MonthCalendarAdapter(getActivity(),calendarView, myCategoryType);
                    adapter.changeType(myCategoryType);
                    calendarView.setAdapter(adapter);

                    break;
                case R.id.rbtInsulin:
                    myCategoryType = LogBookEntry.Type.INSULIN;
                    adapter = new MonthCalendarAdapter(getActivity(),calendarView, myCategoryType);
                    adapter.changeType(myCategoryType);
                    calendarView.setAdapter(adapter);
                    setTypeText(R.string.totaleenhedeninsuline);

                    break;
                case R.id.rbtBlood:
                    myCategoryType = LogBookEntry.Type.BLOOD;
                    setTypeText(R.string.gemiddeldebloedwaarde);
                    adapter = new MonthCalendarAdapter(getActivity(),calendarView, myCategoryType);
                    adapter.changeType(myCategoryType);
                    calendarView.setAdapter(adapter);

                    break;
            }
        } else {
            return;
        }

    }

    @Override
    public void onFavoritePressedCallback(boolean clicked) {
        if (isDualPan) {
           if(adapter!=null) {
                adapter = new MonthCalendarAdapter(getActivity(), calendarView, myCategoryType);
                adapter.changeType(myCategoryType);
                calendarView.setAdapter(adapter);
                // adapter.refresh();
          }
        }

        Log.d("DairyDetailFragment", "the favorite button click now must update UI");
    }
}

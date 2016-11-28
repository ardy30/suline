package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.CalendarEventAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DateUtils;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DialogEditingListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HerinneringenFragment extends android.support.v4.app.Fragment implements DialogEditingListener{

    public ShareActionProvider shareActionProvider = null;
    BloedglucosemetenDialogFragment bloedglucosemetenDialog = null;
    InsulinespuitenDialogFragment insulinespuitenDialog = null;
    TussendoortjeDialogFragment tussendoortjeDialog = null;
    ProductBestellenDialogFragment productBestellenDialog = null;
    CalendarEventAdapter adapter = null;
    ListView mListView = null;
    ArrayList<CalendarEvent> mList = new ArrayList<>();
    Button btnCancel;
    User activeUser;
    TextView tv_noreminder;
    private boolean isDualPan = false;

    public HerinneringenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        if(getActivity().findViewById(R.id.container2) != null)isDualPan = true;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_herinneringen, container, false);
        getActivity().setTitle(R.string.reminder);
        mListView = (ListView) rootview.findViewById(R.id.lv_reminder);

        tv_noreminder = (TextView) rootview.findViewById(R.id.tv_remindertitle);
        adapter = new CalendarEventAdapter(getActivity(), mList);
        mListView.setAdapter(adapter);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadEventsAndUpdateList();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
                CalendarEvent event = mList.get(position);
                System.out.println(event);
                int eventCategoryID =  mList.get(position).getEventCategory();
                switch (eventCategoryID) {
                    case 0:
                        EventBloodlevelEditFragment eFragment = EventBloodlevelEditFragment.newInstance(event);
                        eFragment.registerCallbacks(HerinneringenFragment.this);
                        fragment =  eFragment;
                        break;
                    case 1:
                        EventInsulineSpuitenEditFragment ifragment = EventInsulineSpuitenEditFragment.newInstance(event);
                        ifragment.registerCallbacks(HerinneringenFragment.this);
                        fragment =ifragment;
                        break;
                    case 2:
                        EventTussendoortijeEditFragment tfragment = EventTussendoortijeEditFragment.newInstance(event);
                        tfragment.registerCallbacks(HerinneringenFragment.this);
                        fragment =tfragment;
                        break;
                    case 3:
                        EventProductBestellingEditFragment pfragment = EventProductBestellingEditFragment.newInstance(event);
                        pfragment.registerCallbacks(HerinneringenFragment.this);
                        fragment =pfragment;
                        break;
                }
                if (fragment != null) {
                    int containerId = R.id.container;
                    if (isDualPan) {
                        containerId = R.id.container2;
                        fragmentPopUp();

                        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
                                .replace(containerId, fragment).addToBackStack(null)
                                .commit();
                    } else {
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(fragment)
                                .replace(containerId, fragment, Integer.toString(getFragmentCount())).addToBackStack(null)
                                .commit();
                    }

                }
            }
        });
        return rootview;
    }
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }
    private void fragmentPopUp(){
        if( getFragmentManager().getBackStackEntryCount()>0)
            getFragmentManager().popBackStack();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
        MenuItem shareItem = (MenuItem) menu.findItem(R.id.action_info);
        shareItem.setActionView(R.layout.badge);
        ImageView img = (ImageView) shareItem.getActionView().findViewById(R.id.img_view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupWindow(v);
            }
        });
    }

    private void showBloedglucoseMetenDialog() {
        CalendarEvent event = setInitialEvent(0, "Bloedglucuse meten");
        FragmentManager fm = getActivity().getSupportFragmentManager();
        bloedglucosemetenDialog = new BloedglucosemetenDialogFragment();
        bloedglucosemetenDialog.setEvent(event);
        bloedglucosemetenDialog.registerCallbacks(this);
        bloedglucosemetenDialog.show(fm, getString(R.string.bloedglucosemeten));
        tv_noreminder.setVisibility(View.INVISIBLE);
        loadEventsAndUpdateList();
    }

    private void showInsulineSpuitenDialog() {
        CalendarEvent event = setInitialEvent(1, "Insuline spuiten");
        FragmentManager fm = getActivity().getSupportFragmentManager();
        insulinespuitenDialog = new InsulinespuitenDialogFragment();
        insulinespuitenDialog.setEvent(event);
        insulinespuitenDialog.registerCallbacks(this);
        insulinespuitenDialog.show(fm, getString(R.string.insulinespuiten));
        tv_noreminder.setVisibility(View.INVISIBLE);
        loadEventsAndUpdateList();
    }

    private void showTussendoortjeDialog() {
        CalendarEvent event = setInitialEvent(2, "Tussendoortje");
        FragmentManager fm = getActivity().getSupportFragmentManager();
        tussendoortjeDialog = new TussendoortjeDialogFragment();
        tussendoortjeDialog.setEvent(event);
        tussendoortjeDialog.registerCallbacks(this);
        tussendoortjeDialog.show(fm, getString(R.string.tussendoortje));
        tv_noreminder.setVisibility(View.INVISIBLE);
        loadEventsAndUpdateList();

    }

    private void showProductbestellenDialog() {
        CalendarEvent event = setInitialEvent(3, "");
        FragmentManager fm = getActivity().getSupportFragmentManager();
        productBestellenDialog = new ProductBestellenDialogFragment();
        productBestellenDialog.setEvent(event);
        productBestellenDialog.registerCallbacks(this);
        productBestellenDialog.show(fm, getString(R.string.productbestllen));
        tv_noreminder.setVisibility(View.INVISIBLE);
        loadEventsAndUpdateList();

    }


    private void displayPopupWindow(View anchorView) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.reminer_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = (TextView) popupView.findViewById(R.id.poptitle);
        tv.setText(R.string.title_popup);
        ListView lv = (ListView) popupView.findViewById(R.id.poplistview);
        Resources res = getResources();
        String remindertypes[] = res.getStringArray(R.array.remindertype_array);
        // Set content width and height
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.popup_listitem, R.id.poptitle, remindertypes);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showBloedglucoseMetenDialog();
                        break;
                    case 1:
                        showInsulineSpuitenDialog();
                        break;
                    case 2:
                        showTussendoortjeDialog();
                        break;
                    case 3:
                        showProductbestellenDialog();
                        break;
                }
                popupWindow.dismiss();

            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popupView);
        lv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setWidth(lv.getMeasuredWidth() + 100);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(anchorView);

    }

    @Override
    public void onCheckChangeCallback(int buttonId) {
        loadEventsAndUpdateList();
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(getActivity(), "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }

    public void loadEventsAndUpdateList() {
        if(activeUser!=null) {
            mList = BaseController.getInstance().getDbManager(getActivity()).getEventsTable().getAllCalenderEvents(activeUser.getUserId());
            adapter.setList(mList);
            if (mList.size() > 0)
                tv_noreminder.setVisibility(View.GONE);
            else
                tv_noreminder.setVisibility(View.VISIBLE);

            scrollMyListViewToBottom();
        }
    }

    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    /**
     * Method will create initial Event and save it in DB
     *
     * @param category
     * @param title
     * @return
     */
    private CalendarEvent setInitialEvent(int category, String title) {
        User activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        CalendarEvent event = new CalendarEvent();
        event.setUserID(activeUser.getUserId());
        event.setEventCategory(category);
        event.setEventTitle(title);
        event.setEventEndTime(getCurrentTime());
        if (category == 3) {
            event.setEventEndDate(DateUtils.getTomorrow());
        } else {
            event.setEventEndDate(DateUtils.getCurrentDate());
        }
        event.setEventID(BaseController.getInstance().getDbManager(getActivity()).getEventsTable().insert(event));
        return event;
    }

    /**
     * Method will return Current Time in String
     *
     * @return String of Current Time
     */
    private String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY,1);
        return String.format("%02d:%02d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }

}
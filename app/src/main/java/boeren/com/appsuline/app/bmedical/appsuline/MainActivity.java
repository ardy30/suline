package boeren.com.appsuline.app.bmedical.appsuline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.AccountFragment;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.BluetoothFragment;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.DiaryFragment;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.HerinneringenFragment;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.InfoFragment;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.NewStartFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TabHost.OnTabChangeListener {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    TabHost mTabHost;
    public static int userCount = 0;
    public static boolean isFirstStart = false;
    private DiaryFragment diaryFragment;
    public static BluetoothFragment bluetoothFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    String textVal[];
    int imgVal[] = {R.drawable.tabbar_diary, R.drawable.tabbar_remindersm, R.drawable.tabbar_bluetooth, R.drawable.tabbar_information};
    int imgVal_sel[] = {R.drawable.tabbar_diaryselected, R.drawable.tabbar_reminder_selected, R.drawable.tabbar_bluetooth_selected, R.drawable.tabbar_information_selected};
    static Context context;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCount = BaseController.getInstance().getDbManager(this).getUserTable().getUserCount();
        SpannableString s = new SpannableString(getTitle());
        s.setSpan(new TypefaceSpan("DINPro-Black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setTitle(s);

        context = this;
        textVal = new String[]{context.getString(R.string.dairy),
                context.getString(R.string.reminder),
                context.getString(R.string.bluetooth),
                context.getString(R.string.info)};
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);


        for (int i = 0; i < imgVal.length; i++) {
            setupTab(i);
        }
        for (int i = 0; i < imgVal.length; i++) {
            if (i == 0) {
                mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.theme_sub_color_transparent));
                ImageView img = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.imgTab);
                img.setImageResource(imgVal_sel[0]);
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabsText);
                tv.setTextColor(getResources().getColor(R.color.theme_sub_color));

            } else {
                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));

            }
        }
        setListener();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setValues(MainActivity.this);
       
        // Check to see if there are any  users
        checkFirstStart();

    }

    protected int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    private void setListener() {
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        changeFragment(position);

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.dairy);
                break;
            case 2:
                mTitle = getString(R.string.reminder);
                break;
            case 3:
                mTitle = getString(R.string.bluetooth);
                break;
            case 4:
                mTitle = getString(R.string.info);
                break;
            default:
                break;
        }
    }

    private void checkFirstStart() {
        DialogFragment fragment = new NewStartFragment();
        if (userCount <= 0) {
            isFirstStart = true;
//            fragment.show(getSupportFragmentManager(),"");
//            if (fragment != null) {
//                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
//                        .replace(R.id.container, fragment, Integer.toString(getFragmentCount()))
//                        .commit();
//
//            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

        actionBar.setIcon(R.drawable.ic_launcher);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout), R.drawable.title_bar_back);

        } else {
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout), R.drawable.title_bar_menu_on);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Do Activity menu item stuff here
                return false;
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    setTitle(R.string.app_name);
                    getSupportFragmentManager().popBackStack();
                    return true;
                } else {
                    return false;
                }
            /*case R.id.action_share:
                // Not implemented here
                return false;*/
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // if(MainActivity.this.userCount>0)
            View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private void setupTab(int i) {

        final View view = new View(context);
        mTabHost.setup();
        mTabHost.getTabWidget().setDividerDrawable(null);
        View tabview = createTabView(mTabHost.getContext(), i);
        TabHost.TabSpec setContent = mTabHost.newTabSpec(textVal[i])
                .setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        return view;
                    }
                });

        mTabHost.addTab(setContent);
    }

    private View createTabView(final Context context, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_tabbar, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        ImageView img = (ImageView) view.findViewById(R.id.imgTab);
        img.setImageResource(imgVal[i]);
        tv.setText(textVal[i]);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        for (int i = 0; i < textVal.length; i++) {
            if (tabId.equalsIgnoreCase(textVal[i])) {
                changeFragment(i);

                break;
            }
        }

    }

    private void changeFragment(int position) {
        String tag = "fragment";
        Fragment fragment = null;

        onSectionAttached(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {

            case 0:
                tag = "DagboekFragment";
                fragment = diaryFragment = new DiaryFragment();
                break;
            case 1:
                tag = "HerinneringenFragment";
                fragment = new HerinneringenFragment();
                break;
            case 2:
                tag = "BluetoothFragment";
                fragment = bluetoothFragment = new BluetoothFragment();
                break;
            case 3:
                tag = "InfoFragment";
                fragment = new InfoFragment();
                break;
            case 4:
                tag = "AccountFragment";
                fragment = new AccountFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                    .replace(R.id.container, fragment, Integer.toString(getFragmentCount()))
                    .commit();

        }
        if (!isFirst) {
            for (int i = 0; i < imgVal.length; i++) {
                if (i == position) {
                    mTabHost.getTabWidget().getChildAt(position).setBackgroundColor(getResources().getColor(R.color.theme_sub_color_transparent));
                    ImageView img = (ImageView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.imgTab);
                    img.setImageResource(imgVal_sel[i]);
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabsText);
                    tv.setTextColor(getResources().getColor(R.color.theme_sub_color));

                } else {
                    mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    ImageView img = (ImageView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.imgTab);
                    img.setImageResource(imgVal[i]);
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabsText);
                    tv.setTextColor(getResources().getColor(R.color.theme_color));
                }
            }
        }
        isFirst = false;
    }

    public DiaryFragment getDiaryFragment()
    {
        return diaryFragment;
    }
    public static BluetoothFragment getBluetoothFragment()
    {
        return bluetoothFragment;
    }
}
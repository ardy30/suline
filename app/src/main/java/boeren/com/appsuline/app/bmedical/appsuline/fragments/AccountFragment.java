package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.UsersAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseFragment {
    //Views & Widgets

    private User selectedUser;
    private ActionMode actionMode;

    private View parentView;
    private ListView listView;
    private UsersAdapter mAdapter;
    private boolean isDualPan = false;
    private static final String KEY_USER = "User";
    private User user =null;
    ArrayList<User> listData = new ArrayList<User>();

    public static AccountFragment newInstance(User user){
        AccountFragment a = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        a.setArguments(args);
        return a;
    }

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflateMainView(inflater, R.layout.fragment_account, container, false);
        getActivity().setTitle(R.string.users);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_USER))
            user = (User) getArguments().getSerializable(KEY_USER);
        listView   = (ListView) getMainView().findViewById(R.id.listView);
        initView();
        return getMainView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_adduser, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem shareItem = menu.findItem(R.id.action_add_user);
        shareItem.setActionView(R.layout.addusericonlayout);
        ImageView img=(ImageView)shareItem.getActionView().findViewById(R.id.img_view);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment = new PersonalInfoFragment();
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

    }
    private void fragmentPopUp() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }
    private void initView(){
        mAdapter = new UsersAdapter(getActivity(), getListData());

       /* listView.setActionLayout(R.id.hidden_view);
        listView.setItemLayout(R.id.front_layout);*/
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                User user=mAdapter.getItem(position);
                user.setActiveUser(true);
                BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser().setActiveUser(true);
                PersonalInfoFragment fragment=PersonalInfoFragment.newInstance(user);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser=mAdapter.getItem(position);
                actionMode=getActivity().startActionMode(new ActionModCallBack());
                return true;
            }
        });
    }

    private ArrayList<User> getListData(){
        return BaseController.getInstance().getDbManager(getActivity()).getUserTable().getAllUsers();
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
                if(null!=selectedUser){

                    BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().deleteUserEntries(selectedUser.getUserId());
                    BaseController.getInstance().getDbManager(getActivity()).getUserTable().delete(selectedUser);
                    mAdapter = new UsersAdapter(getActivity(), getListData());

                    listView.setAdapter(mAdapter);
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

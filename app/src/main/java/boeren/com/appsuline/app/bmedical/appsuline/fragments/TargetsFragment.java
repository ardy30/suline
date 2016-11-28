package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TargetsFragment extends BaseFragment implements View.OnClickListener{



    private static final String KEY_USER = "User";
    private User user;
    private String lastusername="";

    public static TargetsFragment newInstance(User user){
        TargetsFragment s = new TargetsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        s.setArguments(args);
        return s;
    }
    public User getUser() {
        return (User)getArguments().getSerializable(KEY_USER);
    }
    public TargetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateMainView(inflater,R.layout.fragment_streefwaardes, container, false);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MainActivity.userCount = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getUserCount();

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_USER)) {
            user = (User) getArguments().getSerializable(KEY_USER);
        }
        else
        if(MainActivity.userCount>0){
            user = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();

        }
        Button btnSave = (Button) getMainView().findViewById(R.id.btnSave);

        if(MainActivity.userCount==0)
            btnSave.setText(R.string.start_tutorial);
        else
            btnSave.setText(R.string.save);


        addTextWatcher(getMainView().findViewById(R.id.etMinBloodLevel));
        addTextWatcher(getMainView().findViewById(R.id.etMaxBloodLevel));

        addOnClickListener(getMainView().findViewById(R.id.btnSave));
        if (null != user) {
            if(user.getMinBloodLevel()!=0)
            ((EditText) getMainView().findViewById(R.id.etMinBloodLevel)).setText(String.valueOf(user.getMinBloodLevel()));
            if(user.getMaxBloodLevel()!=0)
            ((EditText) getMainView().findViewById(R.id.etMaxBloodLevel)).setText(String.valueOf(user.getMaxBloodLevel()));
        }


        return getMainView();
    }

    private void addOnClickListener(View v){
        if(null!=v){
            v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if(user!=null)
                lastusername = user.getName();
                handleSave();
                break;
        }
    }

    private void handleSave(){

        if(MainActivity.userCount==0) {
            user.setActiveUser(true);
            BaseController.getInstance().getDbManager(getActivity()).getUserTable().insert(user);
            android.support.v4.app.Fragment fragment = new  HandleidingFragment();
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();
        }
        else {

            BaseController.getInstance().getDbManager(getActivity()).getUserTable().insert(user);
            android.support.v4.app.Fragment fragment = new AccountFragment();
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();

        }
    }

    @Override
    protected void afterFragmentTextChanged(EditText editText, Editable s) {
        if (null != user) {
            switch (editText.getId()) {
                case R.id.etMinBloodLevel:
                    try{
                        user.setMinBloodLevel(Float.valueOf(s.toString()));
                    }catch (NumberFormatException numberFormatException){
                        user.setMinBloodLevel(0);
                    }

                    break;
                case R.id.etMaxBloodLevel:
                    try{
                        user.setMaxBloodLevel(Float.valueOf(s.toString()));
                    }catch (NumberFormatException numberFormatException){
                        user.setMaxBloodLevel(0);
                    }

                    break;

            }
        } else {
            return;
        }

    }
}

package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class TargetsDialogFragment extends DialogFragment implements View.OnClickListener{



    private static final String KEY_USER = "User";
    private User user;
    private String lastusername="";
    View rootview;
    public static TargetsDialogFragment newInstance(User user){
        TargetsDialogFragment s = new TargetsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        s.setArguments(args);
        return s;
    }
    public User getUser() {
        return (User)getArguments().getSerializable(KEY_USER);
    }
    public TargetsDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        inflateMainView(inflater,R.layout.fragment_streefwaardes, container, false);
        rootview= inflater.inflate(R.layout.fragment_streefwaardes, container, false);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
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

     /*   addTextWatcher(getMainView().findViewById(R.id.etMinCarbohydrates));
        addTextWatcher(getMainView().findViewById(R.id.etMaxCarbohydrates));
        addTextWatcher(getMainView().findViewById(R.id.etMinInsulin));
        addTextWatcher(getMainView().findViewById(R.id.etMaxInsulin));*/
        addTextWatcher(getMainView().findViewById(R.id.etMinBloodLevel));
        addTextWatcher(getMainView().findViewById(R.id.etMaxBloodLevel));

        addOnClickListener(getMainView().findViewById(R.id.btnSave));
        if (null != user) {
           /* ((EditText) getMainView().findViewById(R.id.etMinCarbohydrates)).setText(String.valueOf(user.getMinCarbohydrate()));
            ((EditText) getMainView().findViewById(R.id.etMaxCarbohydrates)).setText(String.valueOf(user.getMaxCarbohydrate()));

            ((EditText) getMainView().findViewById(R.id.etMinInsulin)).setText(String.valueOf(user.getMinInsulin()));
            ((EditText) getMainView().findViewById(R.id.etMaxInsulin)).setText(String.valueOf(user.getMaxInsulin()));*/

            ((EditText) getMainView().findViewById(R.id.etMinBloodLevel)).setText(String.valueOf(user.getMinBloodLevel()));
            ((EditText) getMainView().findViewById(R.id.etMaxBloodLevel)).setText(String.valueOf(user.getMaxBloodLevel()));
        }

        return getMainView();
    }

    private View getMainView(){
        return rootview;
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

                if(lastusername.equals(user.getName())) {
                    BaseController.getInstance().getDbManager(getActivity()).getUserTable().update(user);
                    android.support.v4.app.Fragment fragment = new AccountFragment();
                    getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();
                }
            else {
                    if(MainActivity.userCount==1)
                         user.setActiveUser(true);
                    BaseController.getInstance().getDbManager(getActivity()).getUserTable().update(user);
                    android.support.v4.app.Fragment fragment = TargetsDialogFragment.newInstance(user);
                    getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();

                }
        }

        dismiss();
    }


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

    protected void addTextWatcher(View view){
        if(null!=view && view instanceof EditText){
            EditText editText=(EditText)view;
            editText.addTextChangedListener(new FragmentTextWatcher(editText));
        }

    }

    protected void onFragmentTextChanged(EditText editText,CharSequence s, int start, int before, int count){}
    private class FragmentTextWatcher implements TextWatcher {
        private View view;
        public FragmentTextWatcher(View view){
            this.view=view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onFragmentTextChanged((EditText)view,s,start,before,count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            afterFragmentTextChanged((EditText)view,s);

        }
    }

}

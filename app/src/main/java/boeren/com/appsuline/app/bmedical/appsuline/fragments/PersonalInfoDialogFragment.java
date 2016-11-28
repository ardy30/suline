package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Constants;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Logger;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class PersonalInfoDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {
    private static final String KEY_USER = "User";
    private static final int SELECT_PICTURE = 1;
    private User user=new User();
    private boolean isDualPan = false;
    int avatars[];
    View rootview;

    private Calendar dobCalendar;
    private SimpleDateFormat dobDateFormat;
    public PersonalInfoDialogFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity().findViewById(R.id.container2) != null) isDualPan = true;
        setRetainInstance(true);
        dobCalendar =Calendar.getInstance(new Locale(Constants.DUTCH_LOCAL));
        dobDateFormat=new SimpleDateFormat("d MMMM yyyy");
    }
    public static PersonalInfoDialogFragment newInstance(User user){
        PersonalInfoDialogFragment s = new PersonalInfoDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        s.setArguments(args);
        return s;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        inflateMainView(inflater,R.layout.fragment_persoonlijkegegevens, container, false);
        rootview= inflater.inflate(R.layout.fragment_persoonlijkegegevens, container, false);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        avatars= new int[]{R.drawable.avatars1, R.drawable.avatars2, R.drawable.avatars3};
        MainActivity.userCount = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getUserCount();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_USER)) {
            user = (User) getArguments().getSerializable(KEY_USER);
        }
        Button btnSave = (Button) rootview.findViewById(R.id.btnSave);
        if(MainActivity.userCount==0)
            btnSave.setText(R.string.filldetails);
        else
            btnSave.setText(R.string.save);

        rootview.findViewById(R.id.dateOfBirth).setOnClickListener(this);
        addTextWatcher(rootview.findViewById(R.id.etUserName));

        addTextWatcher(rootview.findViewById(R.id.etNurseName));
        addTextWatcher(rootview.findViewById(R.id.etNurseEmail));

        addCheckedChangedListener(rootview.findViewById(R.id.rbtMale));
        addCheckedChangedListener(rootview.findViewById(R.id.rbtFemale));
        addCheckedChangedListener(rootview.findViewById(R.id.rbtIsNeedle));
        addCheckedChangedListener(rootview.findViewById(R.id.rbtIsPomp));

        addOnclickListener(rootview.findViewById(R.id.btnProfilePic));
        addOnclickListener(rootview.findViewById(R.id.btnSave));
        addOnclickListener(rootview.findViewById(R.id.imgBtnProfilePic));

        if (null != user) {
            ((TextView)rootview.findViewById(R.id.dateOfBirth)).setText(user.getDateOfBirth());
            ((EditText) rootview.findViewById(R.id.etUserName)).setText(user.getName());
//            ((EditText) rootview.findViewById(R.id.dateOfBirth)).setText(String.valueOf(user.getDateOfBirth()));
            ((EditText) rootview.findViewById(R.id.etNurseName)).setText(user.getNurseName());
            ((EditText) rootview.findViewById(R.id.etNurseEmail)).setText(user.getNurseEmail());

            ((RadioButton) rootview.findViewById(R.id.rbtMale)).setChecked(user.isMale());
            ((RadioButton) rootview.findViewById(R.id.rbtFemale)).setChecked(!user.isMale());
            ((RadioButton) rootview.findViewById(R.id.rbtIsNeedle)).setChecked(user.isNeedle());
            ((RadioButton) rootview.findViewById(R.id.rbtIsPomp)).setChecked(!user.isNeedle());
        }

        return  rootview;
    }

    private void showImageDialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectphoto)), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                user.setImageSource(getPath(selectedImageUri));
                Logger.i("","Image Path : " + user.getImageSource());
     ((ImageButton)rootview.findViewById(R.id.imgBtnProfilePic)).setImageURI(selectedImageUri);
            }
        }
    }
    private List<String> getAvatarImages(Context conetx) throws IOException
    {
        AssetManager assetManager =conetx.getAssets();
        String[] files = assetManager.list("images");
        List<String> it= Arrays.asList(files);
        return it;

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void handleSave(){
        if (null != user) {

            EditText username = (EditText) rootview.findViewById(R.id.etUserName);
            if (username.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), R.string.fillname, Toast.LENGTH_LONG).show();
            } else {
                MainActivity.isFirstStart =true;
                DialogFragment fragment = TargetsDialogFragment.newInstance(user);
                fragment.setCancelable(false);
                fragment.show(getActivity().getSupportFragmentManager(),"");
                dismiss();

//                if (fragment != null) {
//                    int containerId = R.id.container;
//                    if (isDualPan) {
//                        containerId = R.id.container2;
//                        fragmentPopUp();
//
//                        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
//                                .replace(containerId, fragment).addToBackStack(null)
//                                .commit();
//                    } else {
//                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(fragment)
//                                .replace(containerId, fragment, Integer.toString(getFragmentCount())).addToBackStack(null)
//                                .commit();
//                    }
//
//                }
            }
        }
    }
    protected int getFragmentCount() {
        return getActivity().getSupportFragmentManager().getBackStackEntryCount();
    }
    private void fragmentPopUp() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }
    private void addOnclickListener(View view){

        if(null!=view){
            view.setOnClickListener(this);
        }

    }


    private void addCheckedChangedListener(View view){

        if(null!=view && view instanceof RadioButton){
            RadioButton rbt=(RadioButton)view;
            rbt.setOnCheckedChangeListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProfilePic:
            case R.id.imgBtnProfilePic:
                showImageDialog();
                break;
            case R.id.btnSave:
                handleSave();
                break;
            case R.id.dateOfBirth:

                Utilities.getInstance().showDatePickerDialog(getActivity(), dobCalendar,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dobCalendar.set(Calendar.YEAR, year);
                        dobCalendar.set(Calendar.MONTH, monthOfYear);
                        dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        user.setDateOfBirth(dobDateFormat.format(dobCalendar.getTime()));
                        ((TextView)rootview.findViewById(R.id.dateOfBirth)).setText(user.getDateOfBirth());
                    }
                });
                break;

        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != user) {
            switch (buttonView.getId()) {
                case R.id.rbtMale:
                    user.setMale(isChecked);
                    break;
                case R.id.rbtIsNeedle:
                    user.setNeedle(isChecked);
                    break;
            }
        }
    }


    protected void afterFragmentTextChanged(EditText editText, Editable s) {
        if (null != user) {
            switch (editText.getId()) {
                case R.id.etUserName:
                    user.setName(s.toString());
                    break;
                case R.id.etNurseName:
                    user.setNurseName(s.toString());
                    break;
                case R.id.etNurseEmail:
                    user.setNurseEmail(s.toString());
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

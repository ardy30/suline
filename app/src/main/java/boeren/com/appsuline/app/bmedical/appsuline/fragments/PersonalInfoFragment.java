package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.AvatarListAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Constants;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Logger;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {
    private static final String KEY_USER = "User";
    private static final int SELECT_PICTURE = 1;
    private User user=new User();
    private boolean isDualPan = false;
    int avatars[];
    private Calendar dobCalendar;
    private SimpleDateFormat dobDateFormat;
    public PersonalInfoFragment() {
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
    public static PersonalInfoFragment newInstance(User user){
        PersonalInfoFragment s = new PersonalInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        s.setArguments(args);
        return s;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateMainView(inflater,R.layout.fragment_persoonlijkegegevens, container, false);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        avatars= new int[]{R.drawable.avatars1, R.drawable.avatars2, R.drawable.avatars3};
        MainActivity.userCount = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getUserCount();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(KEY_USER)) {
            user = (User) getArguments().getSerializable(KEY_USER);
        }
        Button btnSave = (Button) getMainView().findViewById(R.id.btnSave);
        if(MainActivity.userCount==0)
            btnSave.setText(R.string.filldetails);
        else
            btnSave.setText(R.string.save);

        getMainView().findViewById(R.id.dateOfBirth).setOnClickListener(this);

        addTextWatcher(getMainView().findViewById(R.id.etUserName));
        addTextWatcher(getMainView().findViewById(R.id.etNurseName));
        addTextWatcher(getMainView().findViewById(R.id.etNurseEmail));

        addCheckedChangedListener(getMainView().findViewById(R.id.rbtMale));
        addCheckedChangedListener(getMainView().findViewById(R.id.rbtFemale));
//        addCheckedChangedListener(getMainView().findViewById(R.id.rbtIsNeedle));
//        addCheckedChangedListener(getMainView().findViewById(R.id.rbtIsPomp));

        addOnclickListener(getMainView().findViewById(R.id.rbtIsNeedle));
        addOnclickListener(getMainView().findViewById(R.id.rbtIsPomp));

        addOnclickListener(getMainView().findViewById(R.id.btnProfilePic));
        addOnclickListener(getMainView().findViewById(R.id.btnSave));
        addOnclickListener(getMainView().findViewById(R.id.imgBtnProfilePic));

        if (null != user) {
            ((TextView)getMainView().findViewById(R.id.dateOfBirth)).setText(user.getDateOfBirth());

            ((EditText) getMainView().findViewById(R.id.etUserName)).setText(user.getName());
//            ((EditText) getMainView().findViewById(R.id.dateOfBirth)).setText(String.valueOf(user.getDateOfBirth()));
            ((EditText) getMainView().findViewById(R.id.etNurseName)).setText(user.getNurseName());
            ((EditText) getMainView().findViewById(R.id.etNurseEmail)).setText(user.getNurseEmail());

            ((RadioButton) getMainView().findViewById(R.id.rbtMale)).setChecked(user.isMale());
            ((RadioButton) getMainView().findViewById(R.id.rbtFemale)).setChecked(!user.isMale());
            ((RadioButton) getMainView().findViewById(R.id.rbtIsNeedle)).setChecked(user.isNeedle());
            ((RadioButton) getMainView().findViewById(R.id.rbtIsPomp)).setChecked(!user.isNeedle());
        }

        return  getMainView();
    }

    private void showImageDialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectphoto)), SELECT_PICTURE);
    }
    private void displayPopupWindow(View anchorView) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.avatar_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = (TextView) popupView.findViewById(R.id.poptitle);
        tv.setText("Select an Image");
        ListView lv = (ListView) popupView.findViewById(R.id.avatarpoplistview);
        // Set content width and height
        AvatarListAdapter  adapter = new AvatarListAdapter(getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                user.setImageSource(getPath(selectedImageUri));
                Logger.i("","Image Path : " + user.getImageSource());
     ((ImageButton)getMainView().findViewById(R.id.imgBtnProfilePic)).setImageURI(selectedImageUri);
            }
        }
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

            EditText username = (EditText) getMainView().findViewById(R.id.etUserName);
            if (username.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), R.string.fillname, Toast.LENGTH_LONG).show();
            } else {
                MainActivity.isFirstStart =true;
                android.support.v4.app.Fragment fragment = TargetsFragment.newInstance(user);

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
                displayPopupWindow(v);
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
                        ((TextView)getMainView().findViewById(R.id.dateOfBirth)).setText(user.getDateOfBirth());
                    }
                });
                break;
            case R.id.rbtIsNeedle:
                if(((RadioButton)v).isChecked()){
                    user.setNeedle(true);



                }
                break;
            case R.id.rbtIsPomp:
                if(((RadioButton)v).isChecked()){
                    user.setNeedle(false);



                }
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
                    if(user.isNeedle()){
                        android.support.v4.app.Fragment fragment= new InsulinePompSchemaFragment();
                        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
                                .replace(R.id.container, fragment).addToBackStack(null)
                                .commit();
                    }
                    break;
            }
        }
    }

    @Override
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


}

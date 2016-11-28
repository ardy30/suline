package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.MealListAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.font.CaeciliaTextView;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.Meal;
import boeren.com.appsuline.app.bmedical.appsuline.utils.ProductCarb;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDialogFragment extends BaseDialogFragment implements TextView.OnEditorActionListener,AdapterView.OnItemSelectedListener {

    public interface MealDialogFragmentListener {
        void onFinishEditDialog(String inputText);
    }


    private Spinner spnMealTypes;
    private Boolean isDualPan =false;




    public MealDialogFragment() {
        // Required empty public constructor

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if(getActivity().findViewById(R.id.container2) != null)isDualPan = true;
        setRetainInstance(true);
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    protected String getStringTag() {
        return "MealDialog";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        inflateMainView(inflater, R.layout.fragment_meal, container, false);

        Button button= (Button)getMainViw().findViewById(R.id.btnMealtime);
        button.setOnClickListener(this);
        TextView tv_khtabel =(TextView) getMainViw().findViewById(R.id.tv_carboLink);


        // Hide all Products info for normal entry /  Will unhide from the prodcustinfo fragment
        RelativeLayout rl_productName =(RelativeLayout) getMainViw().findViewById(R.id.rlProducttitle);
        rl_productName.setVisibility(View.GONE);
        CaeciliaTextView et_productName =(CaeciliaTextView) getMainViw().findViewById(R.id.tvProductName);
        et_productName.setVisibility(View.GONE);
        EditText et_Qunty = (EditText) getMainViw().findViewById(R.id.etProductQty);
        et_Qunty.setVisibility(View.GONE);
        TextView tv_portion = (TextView) getMainViw().findViewById(R.id.tv_productportion);
        tv_portion.setVisibility(View.GONE);
        tv_khtabel.setOnClickListener(this);
        getDialog().setTitle(R.string.maaltijd);
        getDialog().setTitle(R.string.maaltijd);

        if(isUpdateFlow()) {
            TextView title = (TextView) getDialog().findViewById(android.R.id.title);
            if(title!=null) {
                ViewGroup viewGroup = (ViewGroup) title.getRootView();
                RelativeLayout titleItemsContainer = new RelativeLayout(getActivity());
                titleItemsContainer.setLayoutParams(((ViewGroup) title.getParent()).getLayoutParams());
                ImageView iconDel = new ImageView(getActivity());
                iconDel.setImageResource(R.drawable.ic_trashcan);
                iconDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    d = (DiaryDetailFragment) getCurrentFragment();
                        if (null != getEntry()) {
                            BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().delete(getEntry());
//                        d.selectedEntry = null;
//                        d.updateData();
                        }
                        notifyDataSave();
                        getDialog().dismiss();


                    }
                });

                RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                deleteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                iconDel.setLayoutParams(deleteParams);
                titleItemsContainer.addView(iconDel);
                viewGroup.addView(titleItemsContainer);
            }
        }
        addTextWatcher(getMainViw().findViewById(R.id.etCarbohydrate));
        if(ProductCarb.ProductInfo.compareTo("")!=0) {
            tv_khtabel.setVisibility(View.GONE);
            rl_productName.setVisibility(View.VISIBLE);
            et_productName.setVisibility(View.VISIBLE);
            et_Qunty.setVisibility(View.VISIBLE);
            tv_portion.setVisibility(View.VISIBLE);
        }
        ((EditText)getMainViw().findViewById(R.id.etCarbohydrate)).setText(ProductCarb.ProductInfo);
        ((TextView)getMainViw().findViewById(R.id.tv_productportion)).setText(ProductCarb.ProductQty);
        ((CaeciliaTextView)getMainViw().findViewById(R.id.tvProductName)).setText(ProductCarb.ProductName);
        ((EditText)getMainViw().findViewById(R.id.etProductQty)).setText(ProductCarb.ProductPortion);

        addTextWatcher(getMainViw().findViewById(R.id.etComment));
        addTextWatcher(getMainViw().findViewById(R.id.etProductQty));

        setCancelView(getMainViw().findViewById(R.id.btnCancel));
        setSaveView(getMainViw().findViewById(R.id.btnSave));

        MealListAdapter adapter = new MealListAdapter(getActivity(),getDefaultMeals());

        spnMealTypes = (Spinner) getMainViw().findViewById(R.id.spnMealType);
        spnMealTypes.setAdapter(adapter);
        spnMealTypes.setOnItemSelectedListener(this);

        if(isUpdateFlow()){
            button.setText(getEntry().getEntryTime());
            ((EditText)getMainViw().findViewById(R.id.etCarbohydrate)).setText(String.valueOf(getEntry().getEntryAmount()));
            ((EditText)getMainViw().findViewById(R.id.etComment)).setText(getEntry().getEntryComment());
            int index=getMealIndexFromAdapter(adapter,getEntry().getEntryType());
            spnMealTypes.setSelection(index);
        }else{
            button.setText(Utilities.getInstance().getCurrentFormattedTime());
            getEntry().setEntryTime(button.getText().toString());
           getEntry().setEntryName(ProductCarb.ProductName);
        }

        return  getMainViw();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        /*spnMealTypes.setSelection(position);*/
        Meal selectedMeal = (Meal) spnMealTypes.getSelectedItem();
        if(ProductCarb.ProductInfo.compareTo("")!=0)
            getEntry().setEntryName(ProductCarb.ProductName);
        else
            getEntry().setEntryName(selectedMeal.getName());
        getEntry().setEntryType(selectedMeal.getType());;

    }

    private int getMealIndexFromAdapter(Adapter adapter,LogBookEntry.Type type){
        int count=adapter.getCount();
        for (int i = 0; i <count ; i++) {
            if(((Meal)adapter.getItem(i)).getType().ordinal()==type.ordinal()){
                return i;
            }
        }
        return 0;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            MealDialogFragmentListener activity = (MealDialogFragmentListener) getActivity();
            //activity.onFinishEditDialog(btntime.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    private ArrayList<Meal> getDefaultMeals(){
        ArrayList<Meal> meals=new ArrayList<Meal>();
        Resources resources=getActivity().getResources();
        Meal breakFast=new Meal(R.drawable.ic_breakfast,resources.getString(R.string.breakFast), LogBookEntry.Type.BREAK_FAST);
        Meal lunch=new Meal(R.drawable.ic_lunch,resources.getString(R.string.lunch), LogBookEntry.Type.LUNCH);
        Meal snack=new Meal(R.drawable.ic_snack,resources.getString(R.string.snack), LogBookEntry.Type.SNACK);
        Meal dinner=new Meal(R.drawable.ic_dinner,resources.getString(R.string.dinner), LogBookEntry.Type.DINNER);
        Meal drink=new Meal(R.drawable.ic_drink,resources.getString(R.string.drink), LogBookEntry.Type.DRINK);

        meals.add(breakFast);
        meals.add(lunch);
        meals.add(snack);
        meals.add(dinner);
        meals.add(drink);

        return meals;
    }


    @Override
    public void onClick(final View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnMealtime:
                showTimePickerDialogWithTime((Button)v);
                break;
            case R.id.tv_carboLink:
                this.dismiss();
                android.support.v4.app.Fragment fragment =  ProductsInfoFragment.newInstance(getEntry().getEntryDate());
                if (fragment != null) {
                    int containerId = R.id.container;
                    if(isDualPan){
                        containerId = R.id.container2;
                        fragmentPopUp();
                    }
                    getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                            .replace(containerId, fragment).addToBackStack(null)
                            .commit();

                }
                break;
        }
    }

    private void showTimePickerDialogWithTime(final Button v) {
        String setTime=((Button)v).getText().toString();
        try {
            Utilities.getInstance().showTimePickerDialogWithTime(getActivity(),setTime,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    v.setText(Utilities.getInstance().getFormattedTime(hourOfDay,minute));
                    getEntry().setEntryTime(v.getText().toString());
                }
            });

        }catch (ParseException parseException){

        }
    }

    @Override
    protected void afterDialogTextChanged(EditText editText, Editable s) {
        EditText carbohydrates=(EditText)getMainViw().findViewById(R.id.etCarbohydrate);
        switch (editText.getId()) {

                case R.id.etCarbohydrate:
                    try{

                        if(!carbohydrates.getText().toString().isEmpty()) {
                            double value = Double.valueOf(s.toString());
                            getEntry().setEntryAmount(value);
                        }

                    }catch (NumberFormatException numberFormatException){
                        getEntry().setEntryAmount(0);
                    }

                    break;
                case R.id.etComment:
                    getEntry().setEntryComment(s.toString());
                    break;
                case R.id.etProductQty:
                    try{
                        double qty = Double.valueOf(s.toString());
                         carbohydrates=(EditText)getMainViw().findViewById(R.id.etCarbohydrate);
                        if(!carbohydrates.getText().toString().isEmpty()){
                            double carbs = Double.valueOf(ProductCarb.ProductInfo);
                            carbohydrates.setText((carbs*qty)+"");
                        }
                    }catch (NumberFormatException numberFormatException){

                    }
                    break;

            }

    }
    private void fragmentPopUp(){
        if( getFragmentManager().getBackStackEntryCount()>0)
            getFragmentManager().popBackStack();
    }
}
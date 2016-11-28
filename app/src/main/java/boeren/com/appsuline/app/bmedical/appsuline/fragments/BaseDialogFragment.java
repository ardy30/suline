package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.Serializable;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.utils.ProductCarb;

public class BaseDialogFragment extends DialogFragment implements View.OnClickListener{
    protected static final String KEY_ARGUMENT = "ArgumentObject";
    public static final int DIALOG_MEAL=0;
    public static final int DIALOG_INSULIN=1;
    public static final int DIALOG_BLOOD=2;
    public static final int DIALOG_ACTIVITY=3;

    private MainActivity mainActivity;
    private View mainViw;
    private View saveView;
    private View cancelView;

    private OnDialogFragmentDismissListener dismissListener;
    private LogBookEntry entry;
    private boolean isUpdateFlow;

    public void setSaveView(View saveView) {
        this.saveView = saveView;
        this.saveView.setOnClickListener(this);
    }

    public void setCancelView(View cancelView) {
        this.cancelView = cancelView;
        this.cancelView.setOnClickListener(this);
    }
    public boolean isUpdateFlow() {
        return isUpdateFlow;
    }
    public View getMainViw() {
        return mainViw;
    }

    public void setMainViw(View mainViw) {
        this.mainViw = mainViw;
    }

    public LogBookEntry getEntry() {
        return entry;
    }

    public void setDismissListener(OnDialogFragmentDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }


    protected String getStringTag(){
        return "Base Dialog";
    }

    public void showFragmentDialog(FragmentManager fm){
        show(fm,getStringTag());
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity=(MainActivity)activity;
        if(null!=getArguments() && getArguments().containsKey(KEY_ARGUMENT)){
            entry = (LogBookEntry) getArguments().getSerializable(KEY_ARGUMENT);
        }else{
            entry=new LogBookEntry();
        }

        isUpdateFlow=!(entry.getEntryId()<0);

    }

    protected void inflateMainView(LayoutInflater inflater,int resource, ViewGroup container,boolean attachToRoot){
        setMainViw(inflater.inflate(resource, container, attachToRoot));
    }

    protected void addTextWatcher(View view){
        if(null!=view && view instanceof EditText){
            EditText editText=(EditText)view;
            editText.addTextChangedListener(new DialogTextWatcher(editText));
        }

    }

    protected void afterDialogTextChanged(EditText editText, Editable s){}
    protected void handleCancel(){
        ProductCarb.ProductInfo ="";
        ProductCarb.ProductName ="";
        ProductCarb.ProductQty ="";
        ProductCarb.ProductPortion ="";
        notifyDismiss();
        getDialog().dismiss();
    }
    protected void handleSave(){
        ProductCarb.ProductInfo ="";
        ProductCarb.ProductName ="";
        ProductCarb.ProductQty ="";
        ProductCarb.ProductPortion ="";
        BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().insert(getEntry());
        notifyDataSave();
        getDialog().dismiss();
    }

    protected void notifyDismiss(){
        if(null!=dismissListener){
            dismissListener.onDismiss(this);
        }

    }

    protected void notifyDataSave(){
        if(null!=dismissListener){
            dismissListener.onDismissWithDataSaved(this);
        }

    }

    @Override
    public void onClick(View v) {
        if(null!=cancelView && v.getId()==cancelView.getId()){
            handleCancel();
        }else if(null!=saveView && v.getId()==saveView.getId()){
            handleSave();
        }

    }

    public static BaseDialogFragment newInstance(int dialog,Serializable argument){
        BaseDialogFragment dialogInstance=null;
        Bundle args = new Bundle();
        args.putSerializable(KEY_ARGUMENT, argument);
        switch (dialog){
            case DIALOG_MEAL:
                dialogInstance=new MealDialogFragment();
                break;
            case DIALOG_INSULIN:
                dialogInstance=new InsulinDialogFragment();
                break;
            case DIALOG_BLOOD:
                dialogInstance=new BloodDialogFragment();
                break;
            case DIALOG_ACTIVITY:
                dialogInstance=new ActivityDialogFragment();
                break;

        }
        if(null!=dialogInstance){
            dialogInstance.setArguments(args);
        }

        return dialogInstance;
    }

    private class DialogTextWatcher implements TextWatcher {
        private View view;
        public DialogTextWatcher(View view){
            this.view=view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterDialogTextChanged((EditText)view,s);
        }
    }

    public static interface OnDialogFragmentDismissListener{
        public void onDismiss(DialogFragment dialogFragment);
        public void onDismissWithDataSaved(DialogFragment dialogFragment);
    }


}

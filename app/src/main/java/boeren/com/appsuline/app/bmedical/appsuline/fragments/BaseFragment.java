package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;


public class BaseFragment  extends android.support.v4.app.Fragment{
    private MainActivity mainActivity;

    public View getMainView() {
        return mainView;
    }

    private void setMainView(View mainView) {
        this.mainView = mainView;
    }

    private View mainView;

    protected void inflateMainView(LayoutInflater inflater,int resourceId,ViewGroup container,boolean attachToRoot){
        setMainView(inflater.inflate(resourceId,container,attachToRoot));
    }

    protected void addTextWatcher(View view){
        if(null!=view && view instanceof EditText){
            EditText editText=(EditText)view;
            editText.addTextChangedListener(new FragmentTextWatcher(editText));
        }

    }
    protected void afterFragmentTextChanged(EditText editText, Editable s){}
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

package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * Created by admin on 3/8/2015.
 */
public class FullScreenDialogFragmentBase extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }
}

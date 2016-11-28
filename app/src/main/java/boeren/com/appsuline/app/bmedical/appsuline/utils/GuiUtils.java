package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.PCLinkLibraryDemoActivity;

/**
 * Created by admin on 3/8/2015.
 */
public class GuiUtils {
    /**

     *
     * @param sourceActivityInstance
     *            source activity instance
     */
    public static void goToPCLinkLibraryHomeActivity(Activity sourceActivityInstance) {
        goToSpecifiedActivity(sourceActivityInstance, PCLinkLibraryDemoActivity.class, null);
    }

    public static void goToSpecifiedActivity(Activity sourceActivityInstance,
                                             Class<? extends Activity> targetActivityClass) {
        goToSpecifiedActivity(sourceActivityInstance, targetActivityClass, null);
    }

    /**
     * 跳轉到特定的Activity
     *
     * @param sourceActivityInstance
     *            source activity instance
     * @param targetActivityClass
     *            target activity class
     * @param bundleOfSourceActivity
     *            bundle content of source activity
     */
    public static void goToSpecifiedActivity(Activity sourceActivityInstance,
                                             Class<? extends Activity> targetActivityClass, Bundle bundleOfSourceActivity) {
        Intent intent = new Intent();
        if (bundleOfSourceActivity != null) {
            intent.putExtras(bundleOfSourceActivity);
        }
        intent.setClass(sourceActivityInstance, targetActivityClass);
        sourceActivityInstance.startActivity(intent);
        sourceActivityInstance.finish();
    }

    public static float convertTypeValueToPixel(Context context, float value, int typedValue) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(typedValue, value, r.getDisplayMetrics());
        return px;
    }

    public static void setKeypadVisibility(Context context, EditText inputNote, int visibility) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (visibility) {
            case View.VISIBLE:
                imm.showSoftInput(inputNote, InputMethodManager.SHOW_IMPLICIT);
                break;
            case View.GONE:
            case View.INVISIBLE:
                imm.hideSoftInputFromWindow(inputNote.getWindowToken(), 0);
                break;
        } /* end of switch */
    }
}


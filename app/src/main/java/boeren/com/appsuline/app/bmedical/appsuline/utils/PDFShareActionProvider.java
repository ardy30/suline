package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.ActivityChooserView;
import android.view.View;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * Created by Jamil on 14-1-2015.
 */
public class PDFShareActionProvider extends android.support.v7.widget.ShareActionProvider{
  private final Context mContext;

    public PDFShareActionProvider(Context context)
    {
        super(context);
        mContext = context;
    }
   @Override
    public View onCreateActionView() {
       ActivityChooserView chooserView = (ActivityChooserView) super.onCreateActionView();

       // Set drawable icon
       Drawable icon = mContext.getResources().getDrawable(R.drawable.ic_action_sharepdf);
       chooserView.setExpandActivityOverflowButtonDrawable(icon);
       return  chooserView;
    }



}

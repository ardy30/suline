package boeren.com.appsuline.app.bmedical.appsuline.viewholders;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.views.AnimatingProgressBar;

public class DateInfoViewHolder extends BaseViewHolder{
    private LinearLayout llMonthInfo;
    private LinearLayout llDateInfo;
    private AnimatingProgressBar progressBar;
    private TextView tvMonth;
    private TextView tvDayOfWeek;
    private TextView tvDayOfMonth;
    private TextView tvProgressText;

    public DateInfoViewHolder(View inflatedView) {
        super(inflatedView);
        llMonthInfo=(LinearLayout)getMainView().findViewById(R.id.llMonthInfo);
        llDateInfo=(LinearLayout)getMainView().findViewById(R.id.llDateInfo);
        progressBar=(AnimatingProgressBar)getMainView().findViewById(R.id.progressBar);
        tvMonth=(TextView)getMainView().findViewById(R.id.tvMonth);
        tvDayOfWeek=(TextView)getMainView().findViewById(R.id.tvDayOfWeek);
        tvDayOfMonth=(TextView)getMainView().findViewById(R.id.tvDayOfMonth);
        tvProgressText=(TextView)getMainView().findViewById(R.id.tvProgressText);
        tvDayOfMonth.setEnabled(false);
        tvDayOfMonth.setClickable(false);
        tvDayOfMonth.setOnClickListener(null);
    }


    public TextView getTvProgressText() {
        return tvProgressText;
    }

    public LinearLayout getLlDateInfo() {
        return llDateInfo;
    }

    public LinearLayout getLlMonthInfo() {
        return llMonthInfo;
    }


    public ProgressBar getProgressBar() {

        return progressBar;
    }


    public TextView getTvMonth() {
        return tvMonth;
    }


    public TextView getTvDayOfWeek() {
        return tvDayOfWeek;
    }


    public TextView getTvDayOfMonth() {
        return tvDayOfMonth;
    }

}

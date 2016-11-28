package boeren.com.appsuline.app.bmedical.appsuline.viewholders;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

public class LogBookEntryViewHolder extends BaseViewHolder{
    private TextView entryTime;
    private ImageView entryIcon;
    private TextView entryName;
    private TextView entryAmount;
    private View alertIcon;

    public LogBookEntryViewHolder(View inflatedView) {
        super(inflatedView);
        this.entryTime=(TextView)getMainView().findViewById(R.id.tvAtTime);
        this.entryIcon=(ImageView)getMainView().findViewById(R.id.entryIcon);
        this.entryName=(TextView)getMainView().findViewById(R.id.entryName);
        this.entryAmount=(TextView)getMainView().findViewById(R.id.entryAmount);
        this.alertIcon =(View)getMainView().findViewById(R.id.alertIcon);
    }


    public TextView getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(TextView entryAmount) {
        this.entryAmount = entryAmount;
    }

    public TextView getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(TextView entryTime) {
        this.entryTime = entryTime;
    }

    public ImageView getEntryIcon() {
        return entryIcon;
    }

    public void setEntryIcon(ImageView entryIcon) {
        this.entryIcon = entryIcon;
    }

    public View getAlertIcon() {
        return alertIcon;
    }

    public void setAlertIcon(View alertIcon) {
        this.alertIcon = alertIcon;
    }

    public TextView getEntryName() {
        return entryName;
    }

    public void setEntryName(TextView entryName) {
        this.entryName = entryName;
    }

}

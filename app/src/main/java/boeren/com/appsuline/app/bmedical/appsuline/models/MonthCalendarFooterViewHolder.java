package boeren.com.appsuline.app.bmedical.appsuline.models;

import android.view.View;
import android.widget.Button;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.BaseViewHolder;

public class MonthCalendarFooterViewHolder extends BaseViewHolder{
    private Button btnLoadMore;

    public MonthCalendarFooterViewHolder(View inflatedView) {
        super(inflatedView);
        btnLoadMore=(Button)getMainView().findViewById(R.id.btnLoadMore);
    }

    public Button getBtnLoadMore() {
        return btnLoadMore;
    }
}

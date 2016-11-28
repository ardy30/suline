package boeren.com.appsuline.app.bmedical.appsuline.viewholders;


import android.view.View;

public class BaseViewHolder {
    private View mainView;
    public BaseViewHolder(View inflatedView){
        setMainView(inflatedView);
    }

    public View getMainView() {
        return mainView;
    }

    private void setMainView(View mainView) {
        this.mainView = mainView;
    }

}

package boeren.com.appsuline.app.bmedical.appsuline.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.utils.UICircularImage;

public class UserViewHolder extends BaseViewHolder{
    private TextView title;
    private TextView description;
    private UICircularImage image;
    private CheckBox isActive;

    public UserViewHolder(View inflatedView) {
        super(inflatedView);
        title=(TextView)getMainView().findViewById(R.id.title);
       // description=(TextView)getMainView().findViewById(R.id.description);
        image=(UICircularImage) getMainView().findViewById(R.id.image);
        isActive=(CheckBox)getMainView().findViewById(R.id.isActive);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public UICircularImage getImage() {
        return image;
    }

    public CheckBox getIsActive() {
        return isActive;
    }
}

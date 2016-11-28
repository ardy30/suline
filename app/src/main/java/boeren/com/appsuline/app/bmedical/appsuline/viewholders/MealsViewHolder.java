package boeren.com.appsuline.app.bmedical.appsuline.viewholders;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

public class MealsViewHolder extends BaseViewHolder{
    private ImageView mealIcon;
    private TextView mealType;

    public MealsViewHolder(View inflatedView) {
        super(inflatedView);
        setMealIcon((ImageView)getMainView().findViewById(R.id.icon));
        setMealType((TextView)getMainView().findViewById(R.id.type));
    }

    public TextView getMealType() {
        return mealType;
    }

    private void setMealType(TextView mealType) {
        this.mealType = mealType;
    }

    public ImageView getMealIcon() {
        return mealIcon;
    }

    private void setMealIcon(ImageView mealIcon) {
        this.mealIcon = mealIcon;
    }


}

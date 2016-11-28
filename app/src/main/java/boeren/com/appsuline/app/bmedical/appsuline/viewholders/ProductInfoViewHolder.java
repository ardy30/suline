package boeren.com.appsuline.app.bmedical.appsuline.viewholders;

import android.view.View;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * Created by Jamil on 18-2-2015.
 */
public class ProductInfoViewHolder extends BaseViewHolder{

    private TextView productName;
    private TextView productQuantity;
    private TextView productCarbohydrates;

    public ProductInfoViewHolder(View inflatedView) {
        super(inflatedView);
        setProductName((TextView) getMainView().findViewById(R.id.tv_product_name));
        setProductQuantity((TextView) getMainView().findViewById(R.id.tv_productquantity));
        setProductCarbohydrates((TextView) getMainView().findViewById(R.id.tv_productcarbohydrates));
    }

    public TextView getProductName() {
        return productName;
    }

    public void setProductName(TextView productName) {
        this.productName = productName;
    }

    public TextView getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(TextView productQuantity) {
        this.productQuantity = productQuantity;
    }

    public TextView getProductCarbohydrates() {
        return productCarbohydrates;
    }

    public void setProductCarbohydrates(TextView productCarbohydrates) {
        this.productCarbohydrates = productCarbohydrates;
    }

}

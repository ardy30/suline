package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.models.ProductInfo;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.ProductInfoViewHolder;

/**
 * Created by Jamil on 18-2-2015.
 */
public class ProductsAdapter extends BaseListAdapter<ProductInfo> implements Filterable{

    String units ="kh";

    private ArrayList<ProductInfo> productInfoCopy=new ArrayList<ProductInfo>();

    public ProductsAdapter(Context context, ArrayList<ProductInfo> objects) {
        super(context, objects);
        productInfoCopy.addAll(objects);
    }

    @Override
    public View inflateView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.row_carbohydrate_item,null);
    }


    @Override
    public Object getViewHolder(View convertView,int position) {
        return new ProductInfoViewHolder(convertView);
    }

    @Override
    public void populateView(Object viewHolder, int position) {
        ProductInfo object=getItem(position);
        ProductInfoViewHolder productInfoViewHolder =(ProductInfoViewHolder)viewHolder;
        productInfoViewHolder.getProductName().setText(object.getName());
        productInfoViewHolder.getProductQuantity().setText(object.getQuantity());
        productInfoViewHolder.getProductCarbohydrates().setText(object.getCarbohydrates()+" "+units);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new ProductInfoFilter();
    }

    private class ProductInfoFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            {
                FilterResults results=new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                }
                else {
                    getObjects().clear();
                    constraint=constraint.toString().toLowerCase();
                    for (int i = 0; i < productInfoCopy.size(); i++) {
                        ProductInfo productInfo=productInfoCopy.get(i);
                        String productName = productInfo.getName();
                        if (productName.toLowerCase().contains(constraint.toString()))  {
                            getObjects().add(productInfo);
                        }
                    }

                }
                return results;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}

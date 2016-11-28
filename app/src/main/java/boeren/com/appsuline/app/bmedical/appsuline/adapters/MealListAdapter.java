package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.models.Meal;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.MealsViewHolder;

/**
 * Created by Jamil on 5-2-2015.
 */
public class MealListAdapter extends BaseListAdapter<Meal> {

    public MealListAdapter(Context context, ArrayList<Meal> objects) {
        super(context, objects);
    }

    @Override
    public View inflateView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.row_spinner_item,null);
    }


    @Override
    public Object getViewHolder(View convertView,int position) {
        return new MealsViewHolder(convertView);
    }

    @Override
    public void populateView(Object viewHolder, int position) {
        Meal object=getItem(position);
        MealsViewHolder mealsViewHolder=(MealsViewHolder)viewHolder;
        mealsViewHolder.getMealIcon().setImageResource(object.getIconResource());
        mealsViewHolder.getMealType().setText(object.getName());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.models.NavDrawerItem;

/**
 * Created by Jamil on 6-1-2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    Typeface typeDinProRegular;

    public     NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        typeDinProRegular = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Regular.otf");
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_item_text);
        txtTitle.setTypeface(typeDinProRegular);
        //TextView txtTag = (TextView) convertView.findViewById(R.id.tag);
        //TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        //txtTag.setText(navDrawerItems.get(position).getTag());

        // displaying count
        // check whether it set visible or not
       /* if(navDrawerItems.get(position).getCounterVisibility()){
           // txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
           // txtCount.setVisibility(View.GONE);
        }*/

        return convertView;
    }

}

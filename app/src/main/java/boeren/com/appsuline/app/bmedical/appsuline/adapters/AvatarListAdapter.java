package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * Created by Jamil on 3/7/2015.
 */
public class AvatarListAdapter extends ArrayAdapter {
    private final Context context;


    public AvatarListAdapter(Context context) {
        super(context, R.layout.list_avatar_item);
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_avatar_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

        if (position==0) {
            imageView.setImageResource(R.drawable.avatars1);
        }
        if (position==1){
            imageView.setImageResource(R.drawable.avatars2);
        }
        else
            imageView.setImageResource(R.drawable.avatars3);
        return rowView;
    }
}

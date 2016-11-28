package boeren.com.appsuline.app.bmedical.appsuline.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.UICircularImage;

/**
 * Created by Jamil on 28-1-2015.
 */
public class AccountListAdapter extends BaseAdapter {

    ViewHolder viewHolder;

    private ArrayList<User> mItems = new ArrayList<User>();
    private Context mContext;

    public AccountListAdapter(Context context, ArrayList<User> list) {
        mContext = context;
        mItems = list;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(convertView==null){

            // inflate the layout
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_user, null);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) v.findViewById(R.id.title);
           // viewHolder.descr = (TextView) v.findViewById(R.id.description);
            viewHolder.image = (UICircularImage) v.findViewById(R.id.image);

            // store the holder with the view.
            v.setTag(viewHolder);

        }else{
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String item = mItems.get(position).getName();
        final String desc = mItems.get(position).getName();
        final String imageSrc = mItems.get(position).getImageSource();
        if(imageSrc!=null) {
            File f = new File(imageSrc);
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            viewHolder.image.setImageBitmap(bmp);
        }
        else {
            viewHolder.image.setImageResource(R.drawable.ic_nopic);
        }
        viewHolder.title.setText(item);
        viewHolder.descr.setText(desc);
        TextView hiddenView = (TextView) v.findViewById(R.id.hiddenView);
        hiddenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Toast.makeText(mContext, item + " hidden view clicked",
                        Toast.LENGTH_SHORT).show();*/

            }
        });
        viewHolder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Toast.makeText(mContext, item + " icon clicked",
                        Toast.LENGTH_SHORT).show();*/

            }
        });
        return v;
    }
    public ArrayList<User>  getAllValues() {
        return mItems;
    }
    static class ViewHolder {
        TextView title;
        TextView descr;
        UICircularImage image;
        int position;
    }

}

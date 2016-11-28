package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import boeren.com.appsuline.app.bmedical.appsuline.R;

/**
 * Created by Jamil on 8-1-2015.
 */
public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;

    int[] flag;
    LayoutInflater inflater;
    private View.OnClickListener clickListener;

    public ViewPagerAdapter(Context context, int[] flag) {
        this.context = context;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return flag.length;
    }

    @Override

    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imgflag;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;
        if ((getCount() - 1) != position) {
            itemView = inflater.inflate(R.layout.viewpager_item, container, false);
            // Locate the ImageView in viewpager_item.xml
            imgflag = (ImageView) itemView.findViewById(R.id.flag);
            // Capture position and set to the ImageView
            imgflag.setBackgroundResource(flag[position]);
        } else {
            itemView = inflater.inflate(R.layout.viewpager_button, container, false);
            if (clickListener != null)
                itemView.findViewById(R.id.btnEndTutorial).setOnClickListener(clickListener);
        }
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}


package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.utils.DateUtils;


public class CalendarEventAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CalendarEvent> mList;
    private LayoutInflater inflater;
    private RelativeLayout productOrderInfo;

    public CalendarEventAdapter(Context context, ArrayList<CalendarEvent> list) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mList = list;
    }

    public void setList(ArrayList<CalendarEvent> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        Type1ViewHolder holder1 = new Type1ViewHolder();
        Type2ViewHolder holder2 = new Type2ViewHolder();
        switch (type) {
            case 0:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.row_reminder_events, parent, false);
                    holder1.reminderTypeTv = (TextView) convertView.findViewById(R.id.tv_type_reminder);
                    holder1.reminderTimeTv = (TextView) convertView.findViewById(R.id.tvAtTime);
                    holder1.tvMonday = (TextView) convertView.findViewById(R.id.tvMonday);
                    holder1.tvTuesday = (TextView) convertView.findViewById(R.id.tvTuesday);
                    holder1.tvWednesday = (TextView) convertView.findViewById(R.id.tvWednesday);
                    holder1.tvThursday = (TextView) convertView.findViewById(R.id.tvThursday);
                    holder1.tvFriday = (TextView) convertView.findViewById(R.id.tvFriday);
                    holder1.tvSaturday = (TextView) convertView.findViewById(R.id.tvSaturday);
                    holder1.tvSunday = (TextView) convertView.findViewById(R.id.tvSunday);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (Type1ViewHolder) convertView.getTag();
                }
                if (mList.get(position) != null) {
                    holder1.reminderTypeTv.setText(mList.get(position).getEventTitle());
                    holder1.reminderTypeTv.setOnClickListener(null);
                    holder1.reminderTimeTv.setText(mList.get(position).getEventEndTime());
                    holder1.tvMonday.setText("ma");
                    setBackgroundOfTv(holder1.tvMonday, mList.get(position).isMandage());
                    holder1.tvTuesday.setText("di");
                    setBackgroundOfTv(holder1.tvTuesday, mList.get(position).isDinsdag());
                    holder1.tvWednesday.setText("wo");
                    setBackgroundOfTv(holder1.tvWednesday, mList.get(position).isWoensdag());
                    holder1.tvThursday.setText("do");
                    setBackgroundOfTv(holder1.tvThursday, mList.get(position).isDonderdag());
                    holder1.tvFriday.setText("vr");
                    setBackgroundOfTv(holder1.tvFriday, mList.get(position).isVrijdag());
                    holder1.tvSaturday.setText("za");
                    setBackgroundOfTv(holder1.tvSaturday, mList.get(position).isZaterdag());
                    holder1.tvSunday.setText("zo");
                    setBackgroundOfTv(holder1.tvSunday, mList.get(position).isZondage());
                }
                break;
            case 1:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.row_reminder_productorder, parent, false);
                    productOrderInfo=(RelativeLayout)convertView.findViewById(R.id.llDateInfo);
                    holder2.reminderProductTitleTv = (TextView) convertView.findViewById(R.id.tvProductTitle);
                    holder2.reminderTimeTv = (TextView) convertView.findViewById(R.id.entryAmountTv);
                    holder2.reminderTypeTv = (TextView) convertView.findViewById(R.id.tv_type_reminder);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (Type2ViewHolder) convertView.getTag();
                }
                if (mList.get(position) != null) {
                    holder2.reminderProductTitleTv.setText(mList.get(position).getEventTitle());
                    holder2.reminderTypeTv.setText("Product bestellen");
                    holder2.reminderProductTitleTv.setOnClickListener(null);
                    String days = DateUtils.getDateDiffString(DateUtils.getDateFromString(DateUtils.getCurrentDate()), DateUtils.getDateFromString(mList.get(position).getEventEndDate()));
                    String daysRemaining = days.substring(0,1).trim();
                    if(daysRemaining.compareTo("0")==0) {
                        productOrderInfo.setBackgroundColor(mContext.getResources().getColor(R.color.theme_sub_color));
                        holder2.reminderTimeTv.setText(R.string.buynow);
                    }
                    else {
                        productOrderInfo.setBackgroundColor(mContext.getResources().getColor(R.color.theme_color));
                        holder2.reminderTimeTv.setText(DateUtils.getDateDiffString(DateUtils.getDateFromString(DateUtils.getCurrentDate()), DateUtils.getDateFromString(mList.get(position).getEventEndDate())));

                    }
                }
                break;
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getEventCategory() == 3 ? 1 : 0;
    }

    public class Type1ViewHolder {
        TextView reminderTypeTv, reminderTimeTv, tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;
    }

    public class Type2ViewHolder {
        TextView reminderProductTitleTv, reminderTimeTv, reminderTypeTv;
    }

    private void setBackgroundOfTv(TextView tv, boolean isSelected) {
        if (isSelected) {
            tv.setBackgroundResource(R.drawable.dialog_background);
            tv.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        } else {
            tv.setBackgroundResource(R.color.theme_color);
            tv.setTextColor(mContext.getResources().getColor(R.color.white));
        }
    }
}

package boeren.com.appsuline.app.bmedical.appsuline.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.database.DatabaseManager;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarData;
import boeren.com.appsuline.app.bmedical.appsuline.models.DateInfo;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Constants;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.DateInfoViewHolder;

public class MonthCalendarAdapter extends BaseListAdapter<CalendarData>{


    private Calendar currentDate;
    private ListView listView;
    private Calendar calendar;
    private Locale dutchLocal;
    private SimpleDateFormat simpleMonthFormat;
    private SimpleDateFormat simpleDayFormat;
    private SimpleDateFormat simpleDbDateFormat;
    private String units="";
    private String kh ="kh";
    private String mmmol= "mmol/L";
    private String eh ="EH";

    private LogBookEntry.Type type= LogBookEntry.Type.MEAL;

    private User activeUser;
    public MonthCalendarAdapter(Context context,ListView listView,LogBookEntry.Type type) {
        super(context, new ArrayList<CalendarData>());

        this.listView=listView;
        this.dutchLocal=new Locale(Constants.DUTCH_LOCAL);
        this.calendar =Calendar.getInstance();
        this.currentDate=Calendar.getInstance();

        this.simpleMonthFormat=new SimpleDateFormat(Constants.CALENDAR_MONTH_FORMAT);
        this.simpleDayFormat=new SimpleDateFormat(Constants.CALENDAR_DAY_FORMAT);
        this.simpleDbDateFormat=new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        this.type=type;
        this.activeUser =BaseController.getInstance().getActiveUser();
        BaseController.getInstance().setSelectedCalendarType(type);
        loadFirsPageDate(this.type);

    }



    public void changeType(LogBookEntry.Type type) {
        this.type = type;

    }

    public LogBookEntry.Type getType() {
        return type;
    }

    public void refresh()
    {
        //this.notifyDataSetChanged();
       // super.notifyDataSetChanged();
        //super.requestRefresh();
        notifyDataSetChanged();
    }

    private void loadFirsPageDate(LogBookEntry.Type type){
        getObjects().clear();
        DatabaseManager dbManager= BaseController.getInstance().getDbManager(getContext());
//      Load all dates of current month
        getObjects().addAll(getDataOfMonth(type, dbManager));
//      Move back one mont
        this.calendar.add(Calendar.MONTH, -1);
//      Load data of month
        this.calendar.set(Calendar.DAY_OF_MONTH,this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        getObjects().addAll(getDataOfMonth(type, dbManager));
        dbManager.close();

//        notifyDataSetChanged();

    }

    private ArrayList<CalendarData> getDataOfMonth(LogBookEntry.Type type, DatabaseManager dbManager) {
        ArrayList<CalendarData> calendarDataList=new ArrayList<CalendarData>();
        int currentDay=this.calendar.get(Calendar.DAY_OF_MONTH);
        int totalBloodEntries =0;
        for(int i=currentDay;i>=1;i--){

            CalendarData calendarData=new CalendarData();
            this.calendar.set(Calendar.DAY_OF_MONTH,i);
            calendarData.setCalendarDate(this.calendar.getTime());
            String dateString=simpleDbDateFormat.format(calendarData.getCalendarDate());
            DateInfo dateInfo= dbManager.getLogBookTable().getDateInfo(activeUser.getUserId(),dateString,type);
            totalBloodEntries =  dbManager.getLogBookTable().getLogBookBloodCount(activeUser.getUserId(),LogBookEntry.Type.BLOOD,dateString);
            //System.out.println("Total blood "+totalBloodEntries);
            if(type.equals(LogBookEntry.Type.BLOOD)){
                if(dateInfo.getAmount()==0)
                    dateInfo.setAmount(0);
                else
                    dateInfo.setAmount(dateInfo.getAmount());

            }

            populateMonthData(calendarDataList);

            dateInfo.setDateString(dateString);
            calendarData.setDateInfo(dateInfo);
            calendarDataList.add(calendarData);
        }

        return calendarDataList;
    }

    @Override
    public View inflateView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.row_month_calendar,null);
    }

    @Override
    public Object getViewHolder(View convertView,int position) {
        return new DateInfoViewHolder(convertView);

    }

    @Override
    public void populateView(Object viewHolder, int position) {

        populateDateInfoData((DateInfoViewHolder)viewHolder,position);


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void populateDateInfoData(DateInfoViewHolder viewHolder,int position){
        CalendarData object=getItem(position);

        viewHolder.getProgressBar().setProgress(0);
        viewHolder.getTvMonth().setEnabled(false);
        viewHolder.getTvMonth().setOnClickListener(null);
        this.calendar.setTime(object.getCalendarDate());
        viewHolder.getTvDayOfMonth().setText(simpleDayFormat.format(this.calendar.getTime()));
        String dayOfWeek=this.calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.dutchLocal);
        dayOfWeek = dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
        viewHolder.getTvDayOfWeek().setText(dayOfWeek);
        if(object.getDateInfo().getAmount()>0.0) {
            viewHolder.getProgressBar().setVisibility(View.VISIBLE);
            viewHolder.getProgressBar().setProgress((int) object.getDateInfo().getAmount());

            if(!isValueInUserBoundary(object.getDateInfo().getAmount())){

                viewHolder.getProgressBar().setProgressDrawable(getContext().getResources().getDrawable(R.drawable.progress_red));

            }else{
                viewHolder.getProgressBar().setProgressDrawable(getContext().getResources().getDrawable(R.drawable.progress));
            }

        }else {
            viewHolder.getProgressBar().setVisibility(View.GONE);
        }
        String progressText="";
        if(object.getDateInfo().getAmount()!=0){

            progressText = String.valueOf(object.getDateInfo().getAmount());
        }
        if(!progressText.equals("")) {
            if (this.type == LogBookEntry.Type.MEAL) {
                units = kh;
                viewHolder.getTvProgressText().setText(progressText + " " + units);
            }
            if (this.type == LogBookEntry.Type.INSULIN) {
                units = eh;
                viewHolder.getTvProgressText().setText(progressText + " " + units);
            }
            if (this.type == LogBookEntry.Type.BLOOD) {
                units = mmmol;
                viewHolder.getTvProgressText().setText(progressText + " " + units);
            }
        }
        else { viewHolder.getTvProgressText().setText("");}

        if(object.getType().equals(CalendarData.DataType.MONTH)){
            viewHolder.getMainView().setEnabled(false);
            viewHolder.getLlMonthInfo().setEnabled(false);
            viewHolder.getLlDateInfo().setVisibility(View.GONE);
            viewHolder.getLlMonthInfo().setVisibility(View.VISIBLE);

            String month = simpleMonthFormat.format(this.calendar.getTime());
            month = month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
            viewHolder.getTvMonth().setText(month);
            viewHolder.getTvMonth().setEnabled(false);
            viewHolder.getTvMonth().setOnClickListener(null);

        }else{
            viewHolder.getMainView().setEnabled(true);
            viewHolder.getLlMonthInfo().setEnabled(true);
            viewHolder.getLlDateInfo().setVisibility(View.VISIBLE);
            viewHolder.getLlMonthInfo().setVisibility(View.GONE);


            int selectedPosition=this.listView.getCheckedItemPosition();
//            selectedPosition==position
            String date=null!=BaseController.getInstance().getSelectedCalendarData()?BaseController.getInstance().getSelectedCalendarData().getDateInfo().getDateString():null;
            if(null!=date && object.getDateInfo().getDateString().equals(date)){
                viewHolder.getLlDateInfo().setBackgroundColor(getContext().getResources().getColor(R.color.title_color));
            }else if(object.getDateInfo().isFavourite()){
                viewHolder.getLlDateInfo().setBackgroundColor(getContext().getResources().getColor(R.color.theme_sub_color));
            }else{
                viewHolder.getLlDateInfo().setBackgroundColor(getContext().getResources().getColor(R.color.theme_color));
            }

        }
    }

    private boolean isValueInUserBoundary(float amount){
        if(type.equals(LogBookEntry.Type.BLOOD)){
            return amount>=activeUser.getMinBloodLevel() && amount<=activeUser.getMaxBloodLevel();
        }
        return true;
    }
    public void loadMoreData() {
        DatabaseManager dbManager= BaseController.getInstance().getDbManager(getContext());
        this.calendar.add(Calendar.MONTH, -1);
        this.calendar.set(Calendar.DAY_OF_MONTH,this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //      Load data of month
        getObjects().addAll(getDataOfMonth(type, dbManager));
        dbManager.close();
        notifyDataSetChanged();
    }

    private void populateMonthData(ArrayList<CalendarData> dataList){
        boolean isCurrentDay=  (this.calendar.get(Calendar.YEAR)==this.currentDate.get(Calendar.YEAR) &&
                this.calendar.get(Calendar.DAY_OF_YEAR)==this.currentDate.get(Calendar.DAY_OF_YEAR));

        boolean isEndOfMonth=this.calendar.get(Calendar.DAY_OF_MONTH)==this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(isEndOfMonth || isCurrentDay){
            CalendarData calendarData=new CalendarData();
            calendarData.setType(CalendarData.DataType.MONTH);
            calendarData.setCalendarDate(this.calendar.getTime());
            String dateString=simpleDbDateFormat.format(calendarData.getCalendarDate());
            DateInfo dateInfo=new DateInfo();

            dateInfo.setDateString(dateString);
            calendarData.setDateInfo(dateInfo);
            dataList.add(calendarData);
        }
    }
}
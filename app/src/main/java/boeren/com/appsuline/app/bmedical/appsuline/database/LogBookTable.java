package boeren.com.appsuline.app.bmedical.appsuline.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.models.DateInfo;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;

public class LogBookTable extends Table<LogBookEntry>{
    private final String TABLE_NAME="LogBook";

    private final String COL_ENTRY_ID="EntryID";
    private final String COL_USER_ID="UserID";
    private final String COL_ENTRY_NAME="EntryName";
    private final String COL_ENTRY_DATE="EntryDate";
    private final String COL_ENTRY_TIME="EntryTime";
    private final String COL_ENTRY_DURATION="EntryDuration";
    private final String COL_ENTRY_AMOUNT="EntryAmount";
    private final String COL_ENTRY_TYPE="EntryType";
    private final String COL_IS_FAVOURITE="IsFavourite";
    private final String COL_ENTRY_COMMENT="EntryComment";
    private final String COL_DATE_COMMENT="DateComment";
    private final String COL_IS_LONG_INSULIN="IsLongInsulin";

    private final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME + "("
            + COL_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL_USER_ID + " INTEGER,"+ COL_ENTRY_NAME + " TEXT,"
            + COL_ENTRY_DATE + " TEXT,"
            + COL_ENTRY_TIME + " TEXT," + COL_ENTRY_DURATION + " TEXT," + COL_ENTRY_AMOUNT + " DOUBLE DEFAULT 0.0,"
            +COL_IS_FAVOURITE+" INTEGER,"
            +COL_IS_LONG_INSULIN+" INTEGER,"
            +COL_ENTRY_TYPE+" TEXT,"
            +COL_ENTRY_COMMENT+" TEXT,"
            +COL_DATE_COMMENT+" TEXT)";
    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db) {

    }

    @Override
    public long insert(LogBookEntry logBookEntry) {
        SQLiteDatabase db = getDb();
//        First try to update, if no row is updated then it is a new entry so insert it
//        We always call insert method from UI

        int updatedRows=update(logBookEntry);
        if(updatedRows==0){
            return db.insert(TABLE_NAME, null, populateContentValues(logBookEntry));
        }
        return updatedRows;
    }

    @Override
    public int update(LogBookEntry logBookEntry) {
        SQLiteDatabase db = getDb();

        ContentValues values = populateContentValues(logBookEntry);

        // updating row
        return db.update(TABLE_NAME, values, COL_ENTRY_ID + " = ?",
                new String[] { String.valueOf(logBookEntry.getEntryId()) });
    }

    @Override
    public int delete(LogBookEntry logBookEntry) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_NAME, COL_ENTRY_ID + " = ?",
                new String[] { String.valueOf(logBookEntry.getEntryId()) });
    }

    public int deleteUserEntries(long userId) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_NAME, COL_USER_ID + " = ?",
                new String[] { String.valueOf(userId) });
    }


    @Override
    public LogBookEntry populateObject(Cursor cursor) {
        LogBookEntry logEntry=new LogBookEntry();
        logEntry.setEntryId(cursor.getLong(cursor.getColumnIndex(COL_ENTRY_ID)));
        logEntry.setUserId(cursor.getLong(cursor.getColumnIndex(COL_USER_ID)));
        logEntry.setEntryName(cursor.getString(cursor.getColumnIndex(COL_ENTRY_NAME)));
        logEntry.setEntryDate(cursor.getString(cursor.getColumnIndex(COL_ENTRY_DATE)));
        logEntry.setEntryTime(cursor.getString(cursor.getColumnIndex(COL_ENTRY_TIME)));
        logEntry.setEntryAmount(cursor.getDouble(cursor.getColumnIndex(COL_ENTRY_AMOUNT)));
        logEntry.setEntryDuration(cursor.getString(cursor.getColumnIndex(COL_ENTRY_DURATION)));
        logEntry.setEntryType(LogBookEntry.Type.valueOf(cursor.getString(cursor.getColumnIndex(COL_ENTRY_TYPE))));
        logEntry.setFavourite(cursor.getInt(cursor.getColumnIndex(COL_IS_FAVOURITE))>0);
        logEntry.setLongInsulin(cursor.getInt(cursor.getColumnIndex(COL_IS_LONG_INSULIN))>0);

        logEntry.setEntryComment(cursor.getString(cursor.getColumnIndex(COL_ENTRY_COMMENT)));
        return logEntry;
    }

    @Override
    public ContentValues populateContentValues(LogBookEntry logBookEntry) {
        ContentValues values = new ContentValues();

        values.put(COL_USER_ID, logBookEntry.getUserId());
        values.put(COL_ENTRY_NAME, logBookEntry.getEntryName());
        values.put(COL_ENTRY_DATE, logBookEntry.getEntryDate());
        values.put(COL_ENTRY_TIME,logBookEntry.getEntryTime());
        values.put(COL_ENTRY_AMOUNT,logBookEntry.getEntryAmount());
        values.put(COL_ENTRY_DURATION,logBookEntry.getEntryDuration());
        values.put(COL_ENTRY_TYPE,logBookEntry.getEntryType().name());
        values.put(COL_IS_FAVOURITE,logBookEntry.isFavourite()?1:0);
        values.put(COL_IS_LONG_INSULIN,logBookEntry.isLongInsulin()?1:0);

        values.put(COL_ENTRY_COMMENT,logBookEntry.getEntryComment());
        return values;

    }

    private Cursor getAllLogsCursor(long userId,String date) {
        SQLiteDatabase db=getDb();
        return db.query(TABLE_NAME,null,COL_USER_ID+"=? AND "+COL_ENTRY_DATE+"=? AND "+COL_ENTRY_TYPE+"!=?",new String[]{String.valueOf(userId), date, LogBookEntry.Type.DATE_INFO.name()},null,null,null,null);
    }

    public ArrayList<String> getFavouriteDates(long userId) {
        return getFavouriteDates(getFavouriteLogsCursor(userId));
    }
    private Cursor getFavouriteLogsCursor(long userId) {
        SQLiteDatabase db=getDb();
        return db.query(TABLE_NAME,new String[]{COL_ENTRY_DATE},COL_USER_ID+"=? AND "+COL_IS_FAVOURITE+"=?",new String[]{String.valueOf(userId),String.valueOf(1)},null,null,null,null);
    }
    private ArrayList<String> getFavouriteDates(Cursor cursor) {
        ArrayList<String> favouriteDates=new ArrayList<String>();

        if(null!=cursor){

            if(cursor.moveToFirst()){

                for(int i=0;i<cursor.getCount();i++){

                    favouriteDates.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return favouriteDates;
    }


    private Cursor getCursorForCalendarData(long userId,String date,LogBookEntry.Type type) {
        SQLiteDatabase db=getDb();
        if(type== LogBookEntry.Type.NON){
            return db.query(TABLE_NAME,new String[]{COL_ENTRY_AMOUNT,COL_IS_FAVOURITE},COL_USER_ID+"=? AND "+COL_ENTRY_DATE+"=?",new String[]{String.valueOf(userId), date},null,null,null,null);
        }

        String[] whereArgs=null;
        String whereClause="";

        if(type.equals(LogBookEntry.Type.MEAL)){
            whereClause=COL_USER_ID+"=? AND "+COL_ENTRY_DATE+"=? AND (" +COL_ENTRY_TYPE+"=? OR "+COL_ENTRY_TYPE+"=? OR "+COL_ENTRY_TYPE+"=? OR "
                    +COL_ENTRY_TYPE+"=? OR "
                    +COL_ENTRY_TYPE+"=? OR "
                    +COL_ENTRY_TYPE+"=?)";
            whereArgs=new String[]{
                    String.valueOf(userId),
                    date,
                    LogBookEntry.Type.DINNER.name(),
                    LogBookEntry.Type.BREAK_FAST.name(),
                    LogBookEntry.Type.LUNCH.name(),
                    LogBookEntry.Type.SNACK.name(),
                    LogBookEntry.Type.DRINK.name(),
                    LogBookEntry.Type.DATE_INFO.name()
            };
        }else{
            whereClause=COL_USER_ID+"=? AND "+COL_ENTRY_DATE+"=? AND (" +COL_ENTRY_TYPE+"=? OR "+COL_ENTRY_TYPE+"=?)";
            whereArgs=new String[]{String.valueOf(userId), date,type.name(),LogBookEntry.Type.DATE_INFO.name()};


        }
        return db.query(TABLE_NAME,new String[]{COL_ENTRY_AMOUNT,COL_IS_FAVOURITE},whereClause,whereArgs,null,null,null,null);
    }

    private Cursor getCursorForDateInfoEntry(long userId,String date) {
        SQLiteDatabase db=getDb();
        String[] whereArgs=null;
        String whereClause="";

        whereClause=COL_USER_ID+"=? AND "+COL_ENTRY_DATE+"=? AND " +COL_ENTRY_TYPE+"=?";
        whereArgs=new String[]{String.valueOf(userId),date,LogBookEntry.Type.DATE_INFO.name()};
        return db.query(TABLE_NAME,null,whereClause,whereArgs,null,null,null,null);
    }
    private Cursor getAllTypeBloodCursor(long userId, LogBookEntry.Type type, String date) {
        SQLiteDatabase db = getDb();
        return db.query(TABLE_NAME, null, COL_USER_ID + " = ? AND " + COL_ENTRY_TYPE + " = ? AND " + COL_ENTRY_DATE + " = ?", new String[]{String.valueOf(userId),String.valueOf(type),date}, null, null, null, null);
    }
    public LogBookEntry getDateInfoEntry(long userId,String date){
        Cursor cursor=getCursorForDateInfoEntry(userId,date);
        LogBookEntry entry=new LogBookEntry();
        if(null!=cursor){
            if(cursor.moveToFirst()){
                entry=populateObject(cursor);
            }
            cursor.close();
        }
        return entry;
    }
    public DateInfo getDateInfo(long userId,String date,LogBookEntry.Type type){
        Cursor cursor =getCursorForCalendarData(userId,date,type);
        if(type== LogBookEntry.Type.BLOOD)
            return populateBloodDateInfo(cursor);
        else
            return populateDateInfo(cursor);
    }

    private DateInfo populateDateInfo(Cursor cursor) {
        DateInfo dateInfo=new DateInfo();

        if(null!=cursor){

            if(cursor.moveToFirst()){
                dateInfo.setTotalEntries(cursor.getCount());
                for(int i=0;i<cursor.getCount();i++){
                    dateInfo.setAmount(dateInfo.getAmount()+cursor.getInt(0));
                    if(cursor.getInt(1)>0){
                        dateInfo.setFavourite(true);
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return dateInfo;
    }
    private DateInfo populateBloodDateInfo(Cursor cursor) {
        DateInfo dateInfo=new DateInfo();

        if(null!=cursor){

            if(cursor.moveToFirst()){
                dateInfo.setTotalEntries(cursor.getCount());
                for(int i=0;i<cursor.getCount();i++){
                    dateInfo.setAmount(cursor.getInt(0));
                    if(cursor.getInt(1)>0){
                        dateInfo.setFavourite(true);
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return dateInfo;
    }

    public ArrayList<LogBookEntry> getAllLogEntries(long userId,String date){
        return populateLogsList(getAllLogsCursor(userId,date));
    }
//    public ArrayList<LogBookEntry> getAllLogEntriesOfType(LogBookEntry.Type type){
//        return populateLogsList(getAllLogsCursorOfType(type));
//    }

    public ArrayList<LogBookEntry> populateLogsList(Cursor cursor){
        ArrayList<LogBookEntry> objList=new ArrayList<LogBookEntry>();;
        if(cursor!=null && cursor.moveToFirst()){
            do {
                objList.add(populateObject(cursor));
            }while (cursor.moveToNext());
        }

        if(null != cursor){
            cursor.close();
        }
        return objList;
    }

    // Getting event Count
    public int getLogBookBloodCount(long userId, LogBookEntry.Type type,String date) {

        int count=0;
        Cursor cursor = getAllTypeBloodCursor(userId,type,date);
        count=cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
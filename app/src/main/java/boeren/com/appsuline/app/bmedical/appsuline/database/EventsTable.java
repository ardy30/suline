package boeren.com.appsuline.app.bmedical.appsuline.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;


public class EventsTable extends Table<CalendarEvent> {

    private final String TABLE_EVENTS = "Events";
    private final String COL_EVENTS_ID = "ID";
    // User Table Columns names
    private final String COL_USER_ID = "User_Id";
    private final String COL_CALENDER_EVENT_ID = "calender_event_id";
    private final String COL_TITLE = "event_title";
    private final String COL_EVENT_CAT = "event_category";
    private final String COL_END_TIME = "event_end_time";
    private final String COL_END_DATE = "event_end_date";
    private final String COL_MANDAGE = "event_day_mandage";
    private final String COL_DINSDAG = "event_day_dinsdag";
    private final String COL_WOENSDAG = "event_day_woensdag";
    private final String COL_DONDERDAG = "event_day_donderdag";
    private final String COL_VRIJDAG = "event_day_vrijdag";
    private final String COL_ZATERDAG = "event_day_zaterdag";
    private final String COL_ZONDAG = "event_zondag";

    private final String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
            + COL_EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_USER_ID + " INTEGER,"
            + COL_CALENDER_EVENT_ID + " INTEGER,"
            + COL_TITLE + " TEXT," + COL_EVENT_CAT + " TEXT,"
            + COL_END_TIME + " TEXT," + COL_END_DATE + " TEXT ," + COL_MANDAGE + " BOOLEAN DEFAULT 0,"
            + COL_DINSDAG + " BOOLEAN DEFAULT 0,"
            + COL_WOENSDAG + " BOOLEAN DEFAULT 0,"
            + COL_DONDERDAG + " BOOLEAN DEFAULT 0,"
            + COL_VRIJDAG + " BOOLEAN DEFAULT 0,"
            + COL_ZATERDAG + " BOOLEAN DEFAULT 0,"
            + COL_ZONDAG + " BOOLEAN DEFAULT 0)";


    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db) {

    }

    @Override
    public long insert(CalendarEvent calendarEvent) {
        SQLiteDatabase db = getDb();
        return db.insert(TABLE_EVENTS, null, populateContentValues(calendarEvent));
    }

    @Override
    public int update(CalendarEvent calendarEvent) {
        SQLiteDatabase db = getDb();

        ContentValues values = populateContentValues(calendarEvent);

        // updating row
        return db.update(TABLE_EVENTS, values, COL_EVENTS_ID + " = ?",
                new String[]{String.valueOf(calendarEvent.getEventID())});
    }

    @Override
    public int delete(CalendarEvent calendarEvent) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_EVENTS, COL_EVENTS_ID + " = ?",
                new String[]{String.valueOf(calendarEvent.getEventID())});
    }

    @Override
    public CalendarEvent populateObject(Cursor cursor) {
        CalendarEvent cEvent = new CalendarEvent();
        cEvent.setEventID(cursor.getLong(cursor.getColumnIndex(COL_EVENTS_ID)));
        cEvent.setUserID(cursor.getLong(cursor.getColumnIndex(COL_USER_ID)));
        cEvent.setCalenderEventID(cursor.getLong(cursor.getColumnIndex(COL_CALENDER_EVENT_ID)));
        cEvent.setEventCategory(cursor.getInt(cursor.getColumnIndex(COL_EVENT_CAT)));
        cEvent.setEventTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
        cEvent.setEventEndTime(cursor.getString(cursor.getColumnIndex(COL_END_TIME)));
        cEvent.setEventEndDate(cursor.getString(cursor.getColumnIndex(COL_END_DATE)));
        cEvent.setMandage(cursor.getInt(cursor.getColumnIndex(COL_MANDAGE)) > 0 ? true : false);
        cEvent.setDinsdag(cursor.getInt(cursor.getColumnIndex(COL_DINSDAG)) > 0 ? true : false);
        cEvent.setWoensdag(cursor.getInt(cursor.getColumnIndex(COL_WOENSDAG)) > 0 ? true : false);
        cEvent.setDonderdag(cursor.getInt(cursor.getColumnIndex(COL_DONDERDAG)) > 0 ? true : false);
        cEvent.setVrijdag(cursor.getInt(cursor.getColumnIndex(COL_VRIJDAG)) > 0 ? true : false);
        cEvent.setZaterdag(cursor.getInt(cursor.getColumnIndex(COL_ZATERDAG)) > 0 ? true : false);
        cEvent.setZondage(cursor.getInt(cursor.getColumnIndex(COL_ZONDAG)) > 0 ? true : false);
        return cEvent;
    }

    @Override
    public ContentValues populateContentValues(CalendarEvent calendarEvent) {
        ContentValues cValues = new ContentValues();
        cValues.put(COL_USER_ID, calendarEvent.getUserID());
        cValues.put(COL_CALENDER_EVENT_ID, calendarEvent.getCalenderEventID());
        cValues.put(COL_EVENT_CAT, calendarEvent.getEventCategory());
        cValues.put(COL_TITLE, calendarEvent.getEventTitle());
        cValues.put(COL_END_TIME, calendarEvent.getEventEndTime());
        cValues.put(COL_END_DATE, calendarEvent.getEventEndDate());
        cValues.put(COL_MANDAGE, calendarEvent.isMandage());
        cValues.put(COL_DINSDAG, calendarEvent.isDinsdag());
        cValues.put(COL_WOENSDAG, calendarEvent.isWoensdag());
        cValues.put(COL_DONDERDAG, calendarEvent.isDonderdag());
        cValues.put(COL_VRIJDAG, calendarEvent.isVrijdag());
        cValues.put(COL_ZATERDAG, calendarEvent.isZaterdag());
        cValues.put(COL_ZONDAG, calendarEvent.isZondage());
        return cValues;
    }

    public ArrayList<CalendarEvent> getAllCalenderEvents(long userId) {
        Cursor cursor = getAllAccountCursor(userId);
        return populateAccountsList(cursor);
    }

    public CalendarEvent getCalenderEvent(long userId, long eventId) {
        Cursor cursor = getCursor(userId, eventId);
        return populateEvent(cursor);
    }

    private Cursor getAllAccountCursor(long userId) {
        SQLiteDatabase db = getDb();
        return db.query(TABLE_EVENTS, null, COL_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null, null);
    }

    private Cursor getCursor(long userId, long eventId) {
        SQLiteDatabase db = getDb();
        return db.query(TABLE_EVENTS, null, COL_USER_ID + " = ? AND " + COL_EVENTS_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(eventId)}, null, null, null, null);
    }

    public ArrayList<CalendarEvent> populateAccountsList(Cursor cursor) {
        ArrayList<CalendarEvent> objList = new ArrayList<CalendarEvent>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                objList.add(populateObject(cursor));
            } while (cursor.moveToNext());
        }

        if (null != cursor) {
            cursor.close();
        }
        return objList;
    }

    public CalendarEvent populateEvent(Cursor cursor) {
        CalendarEvent obj = new CalendarEvent();
        if (cursor != null && cursor.moveToFirst()) {
            obj = populateObject(cursor);
        }
        if (null != cursor) {
            cursor.close();
        }
        return obj;
    }
    // Getting event Count
    public int getEventCount(long userId) {

        int count=0;
        Cursor cursor = getAllAccountCursor(userId);
        count=cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}

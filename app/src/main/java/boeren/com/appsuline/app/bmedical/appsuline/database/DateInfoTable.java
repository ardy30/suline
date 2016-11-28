package boeren.com.appsuline.app.bmedical.appsuline.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import boeren.com.appsuline.app.bmedical.appsuline.models.DateInfo;

public class DateInfoTable extends Table<DateInfo> {
    private final String TABLE_NAME="DateInfo";

    private final String COL_DATE="Date";
    private final String COL_IS_FAVOURITE="IsFavourite";
    private final String COL_COMMENT="Comment";
    private final String COL_CARBOHYDRATE_AMOUNT="CarbohydrateAmount";
    private final String COL_INSULIN_AMOUNT="InsulinAmount";
    private final String COL_BLOOD_AMOUNT="BloodAmount";

    private final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME + "("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_IS_FAVOURITE + " INTEGER,"
            + COL_CARBOHYDRATE_AMOUNT + " INTEGER,"
            + COL_INSULIN_AMOUNT + " INTEGER,"
            + COL_BLOOD_AMOUNT + " INTEGER,"
            + COL_COMMENT + " TEXT)";
    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db) {

    }

    @Override
    public long insert(DateInfo dateInfo) {
        SQLiteDatabase db = getDb();
//        First try to update, if no row is updated then it is a new entry so insert it
//        We always call insert method from UI
        int updatedRows=update(dateInfo);
        if(updatedRows==0){
            return db.insert(TABLE_NAME, null, populateContentValues(dateInfo));
        }
        return updatedRows;
    }

    @Override
    public int update(DateInfo dateInfo) {
        SQLiteDatabase db = getDb();

        ContentValues values = populateContentValues(dateInfo);

        // updating row
        return db.update(TABLE_NAME, values, COL_DATE + " = ?",
                new String[] { String.valueOf(dateInfo.getDateString()) });
    }

    @Override
    public int delete(DateInfo dateInfo) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_NAME, COL_DATE + " = ?",
                new String[] { String.valueOf(dateInfo.getDateString()) });
    }

    @Override
    public DateInfo populateObject(Cursor cursor) {
        DateInfo dateInfo=new DateInfo();
        dateInfo.setDateString(cursor.getString(cursor.getColumnIndex(COL_DATE)));
        dateInfo.setFavourite(cursor.getInt(cursor.getColumnIndex(COL_IS_FAVOURITE)) > 0);
        dateInfo.setComment(cursor.getString(cursor.getColumnIndex(COL_COMMENT)));

//        dateInfo.setCarbohydrateAmount(cursor.getInt(cursor.getColumnIndex(COL_CARBOHYDRATE_AMOUNT)));
//        dateInfo.setInsulinAmount(cursor.getInt(cursor.getColumnIndex(COL_INSULIN_AMOUNT)));
//        dateInfo.setBloodAmount(cursor.getInt(cursor.getColumnIndex(COL_BLOOD_AMOUNT)));

        return dateInfo;
    }

    @Override
    public ContentValues populateContentValues(DateInfo dateInfo) {
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_DATE,dateInfo.getDateString());
        contentValues.put(COL_IS_FAVOURITE,dateInfo.isFavourite()?1:0);
        contentValues.put(COL_COMMENT,dateInfo.getComment());

//        contentValues.put(COL_CARBOHYDRATE_AMOUNT,dateInfo.getCarbohydrateAmount());
//        contentValues.put(COL_INSULIN_AMOUNT,dateInfo.getInsulinAmount());
//        contentValues.put(COL_BLOOD_AMOUNT,dateInfo.getBloodAmount());
        return contentValues;
    }

    public DateInfo getDateInfo(String date){
        DateInfo dateInfo=new DateInfo();
        Cursor cursor=getSingleDateCursor(date);
        if(cursor.moveToFirst()){
            dateInfo= populateObject(cursor);
        }
        if(null!=cursor){
            cursor.close();
        }
        return dateInfo;
    }

    private Cursor getSingleDateCursor(String date) {
        SQLiteDatabase db=getDb();
        return db.query(TABLE_NAME,null,COL_DATE+"=?",new String[]{date},null,null,null,null);
    }

}

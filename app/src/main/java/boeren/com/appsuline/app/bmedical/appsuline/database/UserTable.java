package boeren.com.appsuline.app.bmedical.appsuline.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.models.User;

public class UserTable extends Table<User>{
    // Accounts table name
    private final String TABLE_ACCOUNTS = "Users";

    // User Table Columns names
    private final String COL_USER_ID = "ID";

    private final String COL_NAME = "Name";
    private final String COL_IMAGE_SOURCE = "ImageSource";
    private final String COL_AGE = "DateOfBirth";
    private final String COL_IS_MALE = "IsMale";
    private final String COL_IS_NEEDLE = "IsNeedle";
    private final String COL_NURSE_EMAIL = "NurseEmail";
    private final String COL_NURSE_NAME = "NurseName";
    private final String COL_INSULIN_SPUIT_INFO = "SpuitInfo";
    private final String COL_INSULIN_POMP_INFO = "PompInfo";

    private final String COL_MIN_CARBOHYDRATE = "MinCarbohydrate";
    private final String COL_MAX_CARBOHYDRATE = "MaxCarbohydrate";

    private final String COL_MIN_INSULIN = "MinInsulin";
    private final String COL_MAX_INSULIN = "MaxInsulin";

    private final String COL_MIN_BLOOD_LEVEL = "MinBloodLevel";
    private final String COL_MAX_BLOOD_LEVEL = "MaxBloodLevel";
    private final String COL_IS_ACTIVE = "IsActive";
    private final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
            + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " TEXT,"
            + COL_IMAGE_SOURCE + " TEXT," + COL_AGE + " TEXT," + COL_IS_MALE + " INTEGER DEFAULT 0,"
            + COL_IS_NEEDLE +" INTEGER DEFAULT 0,"+ COL_NURSE_EMAIL +" TEXT,"+ COL_NURSE_NAME +" TEXT,"
            + COL_INSULIN_SPUIT_INFO +" TEXT,"
            + COL_INSULIN_POMP_INFO +" TEXT,"
            + COL_MIN_CARBOHYDRATE +" INTEGER DEFAULT 0,"
            + COL_MAX_CARBOHYDRATE +" INTEGER DEFAULT 0,"
            + COL_MIN_INSULIN +" INTEGER DEFAULT 0,"
            + COL_MAX_INSULIN +" INTEGER DEFAULT 0,"
            + COL_MIN_BLOOD_LEVEL +" INTEGER DEFAULT 0,"
            + COL_MAX_BLOOD_LEVEL +" INTEGER DEFAULT 0,"
            + COL_IS_ACTIVE +" INTEGER DEFAULT 0)";

    @Override
    public void create(SQLiteDatabase db) {

        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db) {

    }

    @Override
    public ContentValues populateContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, user.getName()); // User Name
        values.put(COL_IMAGE_SOURCE, user.getImageSource()); // User ImageSRC Path
        values.put(COL_AGE, user.getDateOfBirth());
//        values.put(COL_USER_ID, user.getUserId());
        values.put(COL_IS_MALE, user.isMale()?1:0);
        values.put(COL_IS_NEEDLE, user.isNeedle()?1:0);
        values.put(COL_NURSE_EMAIL, user.getNurseEmail());
        values.put(COL_NURSE_NAME, user.getNurseName());
        values.put(COL_INSULIN_SPUIT_INFO, user.getInsulinSpuitInfo());
        values.put(COL_INSULIN_POMP_INFO, user.getInsulinPompInfo());

        values.put(COL_MIN_CARBOHYDRATE, user.getMinCarbohydrate());
        values.put(COL_MAX_CARBOHYDRATE, user.getMaxCarbohydrate());
        values.put(COL_MIN_INSULIN, user.getMinInsulin());
        values.put(COL_MAX_INSULIN, user.getMaxInsulin());
        values.put(COL_MIN_BLOOD_LEVEL, user.getMinBloodLevel());
        values.put(COL_MAX_BLOOD_LEVEL, user.getMaxBloodLevel());
        values.put(COL_IS_ACTIVE, user.isActiveUser()?1:0);
        return values;
    }

    @Override
    public long insert(User user) {
        SQLiteDatabase db = getDb();
        int rowsUpdated=update(user);

        if(rowsUpdated==0){
            return db.insert(TABLE_ACCOUNTS, null, populateContentValues(user));
        }
        return rowsUpdated;
    }

    @Override
    public int update(User user) {

            SQLiteDatabase db = getDb();

            ContentValues values = populateContentValues(user);

            // updating row
            return db.update(TABLE_ACCOUNTS, values, COL_USER_ID + " = ?",
                    new String[] { String.valueOf(user.getUserId()) });


    }

    @Override
    public int delete(User user) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_ACCOUNTS, COL_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });

    }

    @Override
    public User populateObject(Cursor cursor) {
        User user =new User();

        user.setUserId(cursor.getLong(cursor.getColumnIndex(COL_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
        user.setImageSource(cursor.getString(cursor.getColumnIndex(COL_IMAGE_SOURCE)));
        user.setNurseEmail(cursor.getString(cursor.getColumnIndex(COL_NURSE_EMAIL)));
        user.setNurseName(cursor.getString(cursor.getColumnIndex(COL_NURSE_NAME)));
        user.setInsulinSpuitInfo(cursor.getString(cursor.getColumnIndex(COL_INSULIN_SPUIT_INFO)));
        user.setInsulinPompInfo(cursor.getString(cursor.getColumnIndex(COL_INSULIN_POMP_INFO)));

        user.setMale(cursor.getInt(cursor.getColumnIndex(COL_IS_MALE)) > 0);
        user.setNeedle(cursor.getInt(cursor.getColumnIndex(COL_IS_NEEDLE)) > 0);
        user.setActiveUser(cursor.getInt(cursor.getColumnIndex(COL_IS_ACTIVE))>0);

        user.setDateOfBirth(cursor.getString(cursor.getColumnIndex(COL_AGE)));

        user.setMinCarbohydrate(cursor.getFloat(cursor.getColumnIndex(COL_MIN_CARBOHYDRATE)));
        user.setMaxCarbohydrate(cursor.getFloat(cursor.getColumnIndex(COL_MAX_CARBOHYDRATE)));
        user.setMinInsulin(cursor.getFloat(cursor.getColumnIndex(COL_MIN_INSULIN)));
        user.setMaxInsulin(cursor.getFloat(cursor.getColumnIndex(COL_MAX_INSULIN)));
        user.setMinBloodLevel(cursor.getFloat(cursor.getColumnIndex(COL_MIN_BLOOD_LEVEL)));
        user.setMaxBloodLevel(cursor.getFloat(cursor.getColumnIndex(COL_MAX_BLOOD_LEVEL)));

        return user;
    }

    private Cursor getActiveUserCursor(){
        SQLiteDatabase db=getDb();
        return db.query(TABLE_ACCOUNTS,null,COL_IS_ACTIVE+"=?",new String[]{String.valueOf(1)},null,null,null,null);
    }

    public User getActiveUser(){
        Cursor cursor=getActiveUserCursor();
        User user=null;
        if(null!=cursor){
            if(cursor.moveToFirst()){
                user=populateObject(cursor);
            }
            cursor.close();
        }
        return user;
    }
    public ArrayList<User> getAllUsers(){
        Cursor cursor = getAllAccountCursor();
        return populateAccountsList(cursor);
    }

    private Cursor getAllAccountCursor() {
        SQLiteDatabase db=getDb();
        return db.query(TABLE_ACCOUNTS,null,null,null,null,null,null,null);
    }

    public ArrayList<User> populateAccountsList(Cursor cursor){
        ArrayList<User> objList=new ArrayList<User>();
        if(cursor!=null && cursor.moveToFirst()){
            objList=new ArrayList<User>();
            do {
                objList.add(populateObject(cursor));
            }while (cursor.moveToNext());
        }

        if(null != cursor){
            cursor.close();
        }
        return objList;
    }

    // Getting user Count
    public int getUserCount() {

        int count=0;
        Cursor cursor = getAllAccountCursor();
        count=cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}

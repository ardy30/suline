package boeren.com.appsuline.app.bmedical.appsuline.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Table<T> {
    private SQLiteDatabase db;

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public abstract void create(SQLiteDatabase db);
    public abstract void upgrade(SQLiteDatabase db);

    public abstract long insert(T t);
    public abstract int update(T t);
    public abstract int delete(T t);

    public abstract T populateObject(Cursor cursor);
    public abstract ContentValues populateContentValues(T t);
}

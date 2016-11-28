package boeren.com.appsuline.app.bmedical.appsuline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jamil on 4-2-2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "Appsuline.db";
    private SQLiteDatabase sqliteDatabase;

    private UserTable userTable;
    private LogBookTable logBookTable;
    private DateInfoTable dateInfoTable;
    private ProductInfoTable productInfoTable;
    private EventsTable eventsTable;

    private Context context;
    private File dbFile;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.dbFile = new File("data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME);
        userTable = new UserTable();
        logBookTable = new LogBookTable();
        dateInfoTable = new DateInfoTable();
        productInfoTable = new ProductInfoTable(context);
        eventsTable = new EventsTable();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        try{
//        If database file already exists the no need to copy it
//            if(!this.dbFile.exists()){
//                copy();
//            }
//        }catch (IOException ex){

//        }

        userTable.create(db);
        logBookTable.create(db);
        dateInfoTable.create(db);
//        No need to create product table it already exists in db dump file
        productInfoTable.create(db);
        eventsTable.create(db);
//        data/data/appsuline.com.appsuline.app.bmedical.appsuline/databases/Appsuline.db
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        May be we have changes in dump as well so copy it also
//        try{
//             copy();
//        }catch (IOException ex){

//        }
        userTable.upgrade(db);
        logBookTable.upgrade(db);
        dateInfoTable.upgrade(db);
//        Products table updates are handled in DB dump file
        productInfoTable.upgrade(db);
        eventsTable.upgrade(db);
    }

    public SQLiteDatabase opeDB() {
//        if(!dbFile.exists()){
//            try{
//                copy();
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//
//        }
        if (sqliteDatabase == null || !sqliteDatabase.isOpen()) {
            sqliteDatabase = getWritableDatabase();
//            sqliteDatabase=SQLiteDatabase.openDatabase(dbFile.getPath(),null,SQLiteDatabase.OPEN_READWRITE);
        }

        return sqliteDatabase;
    }

    public void closeDB() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }

    }

    void copy() throws IOException {

        InputStream in = context.getAssets().open("database/" + DATABASE_NAME);
        OutputStream out = new FileOutputStream(this.dbFile);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public UserTable getUserTable() {
        userTable.setDb(opeDB());
        return userTable;
    }

    public LogBookTable getLogBookTable() {
        logBookTable.setDb(opeDB());
        return logBookTable;
    }

    public ProductInfoTable getProductInfoTable() {
        productInfoTable.setDb(opeDB());
        return productInfoTable;
    }

    public EventsTable getEventsTable(){
        eventsTable.setDb(opeDB());
        return eventsTable;
    }

    public DateInfoTable getDateInfoTable() {
        return dateInfoTable;
    }

}


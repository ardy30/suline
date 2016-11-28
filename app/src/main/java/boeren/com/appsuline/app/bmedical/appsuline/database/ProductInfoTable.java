package boeren.com.appsuline.app.bmedical.appsuline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.models.ProductInfo;

/**
 * Created by Jamil on 18-2-2015.
 */
public class ProductInfoTable extends Table<ProductInfo>{

    private final String PRODUCTS_DATA="products_data.txt";
    // Products table name
    private final String TABLE_PRODUCTS = "Products";
    // Product Table Columns names
    private final String COL_PRODUCT_ID = "ID";

    private final String COL_NAME = "Name";
    private final String COL_QUANTITY = "Quantity";
    private final String COL_CARBOHYDRATES = "Carbohydrates";

    private final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " TEXT,"
            + COL_QUANTITY + " TEXT," + COL_CARBOHYDRATES + " TEXT)";


    private Context context;
    public ProductInfoTable(Context context){
        this.context=context;
    }
    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
        try {
            insertData(db);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void upgrade(SQLiteDatabase db) {

    }

    @Override
    public ContentValues populateContentValues(ProductInfo productInfo) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, productInfo.getName()); // Product Name
        values.put(COL_QUANTITY, productInfo.getQuantity()); // Product Quantity dosage
        values.put(COL_CARBOHYDRATES, productInfo.getCarbohydrates()); //Product Carbohydrate amount
        return values;
    }

    @Override
    public long insert(ProductInfo productInfo) {
        SQLiteDatabase db = getDb();
        int rowsUpdated=update(productInfo);

        if(rowsUpdated==0){
            return db.insert(TABLE_PRODUCTS, null, populateContentValues(productInfo));
        }
        return rowsUpdated;
    }

    @Override
    public int update(ProductInfo productInfo) {

        SQLiteDatabase db = getDb();

        ContentValues values = populateContentValues(productInfo);

        // updating row
        return db.update(TABLE_PRODUCTS, values, COL_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(productInfo.getProductId()) });
    }

    @Override
    public int delete(ProductInfo productInfo) {
        SQLiteDatabase db = getDb();
        return db.delete(TABLE_PRODUCTS, COL_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(productInfo.getProductId()) });

    }

    @Override
    public ProductInfo populateObject(Cursor cursor) {
        ProductInfo productInfo =new ProductInfo();
        productInfo.setProductId(cursor.getLong(cursor.getColumnIndex(COL_PRODUCT_ID)));
        productInfo.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
        productInfo.setQuantity(cursor.getString(cursor.getColumnIndex(COL_QUANTITY)));
        productInfo.setCarbohydrates(cursor.getString(cursor.getColumnIndex(COL_CARBOHYDRATES)));
        return productInfo;
    }
    public ArrayList<ProductInfo> getAllProducts(){
        Cursor cursor = getAllProductCursor();
        return populateProductsList(cursor);
    }
    private Cursor getAllProductCursor() {
        SQLiteDatabase db=getDb();
        return db.query(TABLE_PRODUCTS,null,null,null,null,null,null,null);
    }
    public ArrayList<ProductInfo> populateProductsList(Cursor cursor){
        ArrayList<ProductInfo> objList=new ArrayList<ProductInfo>();
        if(cursor!=null && cursor.moveToFirst()){
            objList=new ArrayList<ProductInfo>();
            do {
                objList.add(populateObject(cursor));
            }while (cursor.moveToNext());
        }

        if(null != cursor){
            cursor.close();
        }
        return objList;
    }

    private void insertData(SQLiteDatabase db) throws IOException {
        InputStream in =this.context.getAssets().open("database/"+PRODUCTS_DATA);
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        String stringLine=null;
        while (null!=(stringLine=reader.readLine())) {
            executeInsertRawQuery(stringLine,db);
        }
        in.close();
    }

    private void executeInsertRawQuery(String query,SQLiteDatabase db){
        db.execSQL(query);
    }
}

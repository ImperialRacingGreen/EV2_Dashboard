package imperial.racinggreen;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.sql.SQLException;

/**
 * Created by JohnD on 9/28/13.
 */
public class DbAdapter {
    private static final String DATABASE_NAME ="data";
    private static final String DATABASE_TABLE = "telemetry";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_SIGNAL_1  = "Signal_1";
    public static final String KEY_SIGNAL_2  = "Signal_2";
    public static final String KEY_SIGNAL_3  = "Signal_3";
    public static final String KEY_SIGNAL_4  = "Signal_4";
    public static final String KEY_SIGNAL_5  = "Signal_5";
    public static final String KEY_SIGNAL_6  = "Signal_6";
    public static final String KEY_SIGNAL_7  = "Signal_7";
    public static final String KEY_SIGNAL_8  = "Signal_8";
    public static final String KEY_SIGNAL_9  = "Signal_9";
    public static final String KEY_SIGNAL_10 = "Signal_10";
    public static final String KEY_DATE_TIME = "Log_Date";
    public static final String KEY_ROW_ID = "id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                + KEY_ROW_ID + " integer primary key autoincrement, "
                + KEY_SIGNAL_1 + " text not null, "
                + KEY_SIGNAL_2 + " text not null, "
                + KEY_SIGNAL_3 + " text not null, "
                + KEY_SIGNAL_4 + " text not null, "
                + KEY_SIGNAL_5 + " text not null, "
                + KEY_SIGNAL_6 + " text not null, "
                + KEY_SIGNAL_7 + " text not null, "
                + KEY_SIGNAL_8 + " text not null, "
                + KEY_SIGNAL_9 + " text not null, "
                + KEY_SIGNAL_10 + " text not null, "
                + KEY_DATE_TIME + " text not null);";

    private final Context mCtx;

    public DbAdapter(Context ctx){
        this .mCtx = ctx;
    }

    public DbAdapter open() throws SQLException{
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDbHelper.close();
    }

    public long createListEntry(String Signal_1, String Signal_2, String Signal_3, String Signal_4, String Signal_5,
                                String Signal_6, String Signal_7, String Signal_8, String Signal_9, String Signal_10,
                                String Log_Date){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SIGNAL_1, Signal_1);
        initialValues.put(KEY_SIGNAL_2, Signal_2);
        initialValues.put(KEY_SIGNAL_3, Signal_3);
        initialValues.put(KEY_SIGNAL_4, Signal_4);
        initialValues.put(KEY_SIGNAL_5, Signal_5);
        initialValues.put(KEY_SIGNAL_6, Signal_6);
        initialValues.put(KEY_SIGNAL_7, Signal_7);
        initialValues.put(KEY_SIGNAL_8, Signal_8);
        initialValues.put(KEY_SIGNAL_9, Signal_9);
        initialValues.put(KEY_SIGNAL_10,Signal_10);
        initialValues.put(KEY_DATE_TIME,Log_Date);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /*public Boolean deleteListEntry(String dateLogged){
        return
                mDb.delete(DATABASE_TABLE, KEY_DATE_TIME + "=" + dateLogged, null)>0;
    }*/

    /*public Cursor fetchAllEntries(){
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID, KEY_ITEM, KEY_PRICE,
                         KEY_NUMBER, KEY_DATE_TIME}, null, null, null, null,null);
    }*/

    public Cursor fetchEntry(String dateLogged)throws SQLException{
        String[] selectionArgs = new String[]{dateLogged};
        String[] receiptData = new String[]{KEY_SIGNAL_1, KEY_SIGNAL_2,KEY_SIGNAL_3,KEY_SIGNAL_4,KEY_SIGNAL_5
                                            ,KEY_SIGNAL_6,KEY_SIGNAL_7,KEY_SIGNAL_8,KEY_SIGNAL_9,KEY_SIGNAL_10};
        return mDb.query(DATABASE_TABLE, receiptData, KEY_DATE_TIME + "=?", selectionArgs,
                                   null, null, null, null);
    }


    public boolean updateList(long rowId, String Signal_1, String Signal_2, String Signal_3,
                              String Signal_4, String Signal_5,String Signal_6, String Signal_7,
                              String Signal_8, String Signal_9, String Signal_10,String Log_Date){
        ContentValues args = new ContentValues();
        args.put(KEY_SIGNAL_1, Signal_1);
        args.put(KEY_SIGNAL_2, Signal_2);
        args.put(KEY_SIGNAL_3, Signal_3);
        args.put(KEY_SIGNAL_4, Signal_4);
        args.put(KEY_SIGNAL_5, Signal_5);
        args.put(KEY_SIGNAL_6, Signal_6);
        args.put(KEY_SIGNAL_7, Signal_7);
        args.put(KEY_SIGNAL_8, Signal_8);
        args.put(KEY_SIGNAL_9, Signal_9);
        args.put(KEY_SIGNAL_10, Signal_10);
        args.put(KEY_DATE_TIME,Log_Date);

        return
                mDb.update(DATABASE_TABLE, args, KEY_ROW_ID + "=" + rowId, null)>0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public  void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }


}

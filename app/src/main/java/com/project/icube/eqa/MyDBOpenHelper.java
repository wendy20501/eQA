package com.project.icube.eqa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yiwenwang on 2017/4/11.
 */

public class MyDBOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydata.db";

    private static final String SQL_CREATE_TASK_ENTRIES = "CREATE TABLE " + DataColumns.TASK_TABLE_NAME + " (" +
            DataColumns.TASK_NO + " NVARCHAR(25), " + DataColumns.TASK_DESCRIPTION + " NVARCHAR(300), " +
            DataColumns.TASK_CATEGORY + " NVARCHAR(25), " + DataColumns.TASK_TYPE + " NVARCHAR(25), " +
            DataColumns.TASK_STATUS + " CHAR(1), " + DataColumns.TASK_OBJECT + " NVARCHAR(25), " +
            DataColumns.TASK_ERP + " NVARCHAR(12), " + DataColumns.TASK_PRIORITY + " NVARCHAR(3), " +
            DataColumns.TASK_NUM_DOC + " NVARCHAR(3), " + DataColumns.TASK_NUM_ACT + " NVARCHAR(3) ," +
            DataColumns.TASK_ETD + " DATETIME, " + DataColumns.TASK_DAY_LEFT + " INT, " +
            DataColumns.TASK_DAY_END + " DATETIME, " + DataColumns.TASK_ALERT + " BIT, " +
            DataColumns.TASK_NOTE + " TEXT, " + DataColumns.TASK_ISSUE + " TEXT, " +
            DataColumns.TASK_DADDED + " DATETIME, " + DataColumns.TASK_UADDED + " NVARCHAR(25), " +
            DataColumns.TASK_DEDITED + " DATETIME ," + DataColumns.TASK_UEDITED + " NVARCHAR(25), " +
            DataColumns.TASK_ALERT_DAYS + " INT)";

    private static final String SQL_DELETE_TASK_ENTRIES = "DROP TABLE IF EXISTS " + DataColumns.TASK_TABLE_NAME;

    private static final String SQL_CREATE_ACTION_ENTRIES = "CREATE TABLE " + DataColumns.ACTION_TABLE_NAME + " (" +
            DataColumns.TASK_NO + " NVARCHAR(25), " + DataColumns.ACTION_NO + " NVARCHAR(25), " +
            DataColumns.ACTION_DESC + " NVARCHAR(300), " + DataColumns.ACTION_OWNER + " NVARCHAR(25), " +
            DataColumns.ACTION_STATUS + " CHAR(1)," + DataColumns.ACTION_ETD + " DATETIME, " +
            DataColumns.ACTION_DAY_LATE + " INT, " + DataColumns.ACTION_ATD + " DATETIME, " +
            DataColumns.ACTION_DAY_WORK + " INT, " + DataColumns.ACTION_NOTE + " TEXT, " +
            DataColumns.ACTION_ISSUE + " TEXT, " + DataColumns.ACTION_DADDED + " DATETIME, " +
            DataColumns.ACTION_UADDED + " NVARCHAR(25), " + DataColumns.ACTION_DEDITED + " DATETIME, " +
            DataColumns.ACTION_UEDITED + " NVARCHAR(25), " + DataColumns.ACTION_CATEGORY_ID + " NVARCHAR(25), " +
            DataColumns.ACTION_TYPE_ID + " NVARCHAR(25))";

    private static final String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + DataColumns.ACTION_TABLE_NAME;

    public MyDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_ENTRIES);
        db.execSQL(SQL_CREATE_ACTION_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TASK_ENTRIES);
        db.execSQL(SQL_DELETE_ACTION_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

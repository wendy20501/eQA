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

    private static final String SQL_CREATE_TASK_ENTRIES = "CREATE TABLE " + TaskMgr.TaskEntry.TASK_TABLE_NAME + " (" +
            TaskMgr.TaskEntry.TASK_NO + " NVARCHAR(25), " + TaskMgr.TaskEntry.TASK_DESCRIPTION + " NVARCHAR(300), " +
            TaskMgr.TaskEntry.TASK_CATEGORY + " NVARCHAR(25), " + TaskMgr.TaskEntry.TASK_TYPE + " NVARCHAR(25), " +
            TaskMgr.TaskEntry.TASK_STATUS + " CHAR(1), " + TaskMgr.TaskEntry.TASK_OBJECT + " NVARCHAR(25), " +
            TaskMgr.TaskEntry.TASK_ERP + " NVARCHAR(12), " + TaskMgr.TaskEntry.TASK_PRIORITY + " NVARCHAR(3), " +
            TaskMgr.TaskEntry.TASK_NUM_DOC + " NVARCHAR(3), " + TaskMgr.TaskEntry.TASK_NUM_ACT + " NVARCHAR(3) ," +
            TaskMgr.TaskEntry.TASK_ETD + " DATETIME, " + TaskMgr.TaskEntry.TASK_DAY_LEFT + " INT, " +
            TaskMgr.TaskEntry.TASK_DAY_END + " DATETIME, " + TaskMgr.TaskEntry.TASK_ALERT + " BIT, " +
            TaskMgr.TaskEntry.TASK_NOTE + " TEXT, " + TaskMgr.TaskEntry.TASK_ISSUE + " TEXT, " +
            TaskMgr.TaskEntry.TASK_DADDED + " DATETIME, " + TaskMgr.TaskEntry.TASK_UADDED + " NVARCHAR(25), " +
            TaskMgr.TaskEntry.TASK_DEDITED + " DATETIME ," + TaskMgr.TaskEntry.TASK_UEDITED + " NVARCHAR(25), " +
            TaskMgr.TaskEntry.TASK_ALERT_DAYS + " INT)";

    private static final String SQL_DELETE_TASK_ENTRIES = "DROP TABLE IF EXISTS " + TaskMgr.TaskEntry.TASK_TABLE_NAME;

    private static final String SQL_CREATE_ACTION_ENTRIES = "CREATE TABLE " + TaskMgr.ActionEntry.ACTION_TABLE_NAME + " (" +
            TaskMgr.TaskEntry.TASK_NO + " NVARCHAR(25), " + TaskMgr.ActionEntry.ACTION_NO + " NVARCHAR(25), " +
            TaskMgr.ActionEntry.ACTION_DESC + " NVARCHAR(300), " + TaskMgr.ActionEntry.ACTION_OWNER + " NVARCHAR(25), " +
            TaskMgr.ActionEntry.ACTION_STATUS + " CHAR(1)," + TaskMgr.ActionEntry.ACTION_ETD + " DATETIME, " +
            TaskMgr.ActionEntry.ACTION_DAY_LATE + " INT, " + TaskMgr.ActionEntry.ACTION_ATD + " DATETIME, " +
            TaskMgr.ActionEntry.ACTION_DAY_WORK + " INT, " + TaskMgr.ActionEntry.ACTION_NOTE + " TEXT, " +
            TaskMgr.ActionEntry.ACTION_ISSUE + " TEXT, " + TaskMgr.ActionEntry.ACTION_DADDED + " DATETIME, " +
            TaskMgr.ActionEntry.ACTION_UADDED + " NVARCHAR(25), " + TaskMgr.ActionEntry.ACTION_DEDITED + " DATETIME, " +
            TaskMgr.ActionEntry.ACTION_UEDITED + " NVARCHAR(25), " + TaskMgr.ActionEntry.ACTION_CATEGORY_ID + " NVARCHAR(25), " +
            TaskMgr.ActionEntry.ACTION_TYPE_ID + " NVARCHAR(25))";

    private static final String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + TaskMgr.ActionEntry.ACTION_TABLE_NAME;

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

package com.project.icube.eqa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yiwenwang on 2016/10/3.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements DBHelper {
    private DataMgr dataMgr;
    private static SQLiteDatabase database;

    public SQLiteHelper(Context context, DataMgr dataMgr) {
        super(context, dataMgr.getDBName(), null, dataMgr.getDBVersion());
        this.dataMgr = dataMgr;
        if (database == null || !database.isOpen()) {
            database = this.getWritableDatabase();
        }
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        if (database == null || !database.isOpen()) {
            database = this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        database.execSQL(dataMgr.getDeleteTaskTableCmd());
        database.execSQL(dataMgr.getDeleteActionTableCmd());
        onCreate(database);
    }

    @Override
    public void CreateTable() {
        database.execSQL(dataMgr.getCreateTaskTableCmd());
        database.execSQL(dataMgr.getCreateActionTableCmd());
    }

    @Override
    public void DropTable() {
        database.execSQL(dataMgr.getDeleteTaskTableCmd());
        database.execSQL(dataMgr.getDeleteActionTableCmd());
    }

    @Override
    public long InsertDB(String table_name, String col_name, ContentValues content) {
        return database.insert(table_name, col_name, content);
    }

    @Override
    public void UpdateDB(String table_name, ContentValues content, String selection, String[] selectionArgs) {
        database.update(table_name, content, selection, selectionArgs);
    }

    @Override
    public void DeleteDB(String table_name, String whereClause, String[] whereArgs) {
        database.delete(table_name, whereClause, whereArgs);
    }

    @Override
    public Cursor QueryDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @Override
    public Cursor RawQuery(String sql, String[] selecArgs) {
        return database.rawQuery(sql, selecArgs);
    }


}

package com.project.icube.eqa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yiwenwang on 2016/10/3.
 */
public class SQLiteHelper implements DBHelper {
    private SQLiteDatabase db;


    public SQLiteHelper(Context context) {
        SQLiteOpenHelper mDBHelper = new MyDBOpenHelper(context);
        db = mDBHelper.getWritableDatabase();
    }

    @Override
    public long InsertDB(String table_name, String col_name, ContentValues content) {
        return db.insert(table_name, col_name, content);
    }

    @Override
    public void UpdateDB(String table_name, ContentValues content, String selection, String[] selectionArgs) {
        db.update(table_name, content, selection, selectionArgs);
    }

    @Override
    public void DeleteDB(String table_name, String whereClause, String[] whereArgs) {
        db.delete(table_name, whereClause, whereArgs);
    }

    @Override
    public Cursor QueryDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @Override
    public Cursor RawQuery(String sql, String[] selecArgs) {
        return db.rawQuery(sql, selecArgs);
    }


}

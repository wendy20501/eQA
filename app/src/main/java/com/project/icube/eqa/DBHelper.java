package com.project.icube.eqa;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by yiwenwang on 2016/10/8.
 */
public interface DBHelper {
    long InsertDB(String table_name, String col_name, ContentValues content);
    void UpdateDB(String table_name, ContentValues content, String selection, String[] selectionArgs);
    void DeleteDB(String table_name, String whereClause, String[] whereArgs);
    Cursor QueryDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
    Cursor RawQuery(String sql, String[] selecArgs);


}

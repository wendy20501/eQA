package com.project.icube.eqa;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by yiwenwang on 2016/10/3.
 */
public class SQLiteHelper extends ContentProvider {
    private SQLiteOpenHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new MyDBOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor result = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, null);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long rowId = db.insert(uri.getLastPathSegment(), TaskMgr.TaskEntry.TASK_NO, values);
        Uri newUri = ContentUris.withAppendedId(TaskMgr.TASK_URI, rowId);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int result = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int result = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    public Cursor rowquery(String sql, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        return db.rawQuery(sql, selectionArgs);
    }
}

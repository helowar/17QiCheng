package com.qicheng.business.persistor;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.qicheng.util.QiChengDb;

/**
 * Created by NO3 on 2015/2/5.
 */
public class DBTaskProvider extends ContentProvider {

    public static final String TAG = "DBTaskProvider";

    public QiChengDbHelper qiChengDbHelper;

    public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(QiChengDb.AUTOHORITY, QiChengDb.TNAME, QiChengDb.ITEM);
        uriMatcher.addURI(QiChengDb.AUTOHORITY, QiChengDb.TNAME + "/#", QiChengDb.ITEM_ID);
    }

    public static final String CREATE_TAG_TABLE = "create table " + QiChengDb.TNAME + "(" +
            QiChengDb.TID + " integer primary key autoincrement not null," +
            QiChengDb.NAME + " DEFAULT null," +
            QiChengDb.PRIORITY + "interger DEFAULT 1," +
            QiChengDb.VALID + " interger DEFAULT 1," +
            QiChengDb.HOT + " interger DEFAULT 1);";


    @Override
    public boolean onCreate() {
        qiChengDbHelper = new QiChengDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

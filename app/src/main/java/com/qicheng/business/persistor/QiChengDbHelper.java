package com.qicheng.business.persistor;

import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qicheng.util.QiChengDb;

/**
 * Created by NO3 on 2015/2/5.
 */
 public class QiChengDbHelper extends SQLiteOpenHelper {

    public  QiChengDbHelper(Context context){
        super(context, QiChengDb.DBNAME,null,QiChengDb.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTaskProvider.CREATE_TAG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.ui.chat.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.qicheng.business.ui.chat.utils.Constant;
import com.qicheng.business.module.User;
import com.easemob.util.HanziToPinyin;
import com.qicheng.framework.util.StringUtil;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "user_im_id";
    public static final String COLUMN_NAME_AVATAR="avatar_url";
    public static final String COLUMN_NAME_GENDER="gender";
	public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_HEADER = "header";
	public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";

	private DbOpenHelper dbHelper;

	public UserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (User user : contactList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getUserImId());
			    values.put(COLUMN_NAME_NICK, user.getNickName());
                if(!StringUtil.isEmpty(user.getPortraitURL())){
                    values.put(COLUMN_NAME_AVATAR, user.getPortraitURL());
                }
                values.put(COLUMN_NAME_GENDER,user.getGender());
				db.replace(TABLE_NAME, null, values);
			}
		}
        dbHelper.closeDB();
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String useImId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                String header = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_HEADER));
                String avatar = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));
                int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
				User user = new User();
				user.setUserImId(useImId);
				user.setNickName(nick);
                user.setPortraitURL(avatar);
				user.setHeader(header);
                user.setGender(gender);
				users.put(useImId, user);
			}
			cursor.close();
            dbHelper.closeDB();
		}
		return users;
	}

    public User getContact(String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + "='" + id + "'"/* + " desc" */, null);
            while (cursor.moveToNext()) {
                String useImId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                String header = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_HEADER));
                String avatar = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));
                int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
                user = new User();
                user.setUserImId(useImId);
                user.setNickName(nick);
                user.setPortraitURL(avatar);
                user.setHeader(header);
                user.setGender(gender);
            }
            cursor.close();
            dbHelper.closeDB();
        }
        return user;
    }
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
		}
        dbHelper.closeDB();
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(User user){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getUserImId());
		if(user.getNickName() != null)
			values.put(COLUMN_NAME_NICK, user.getNickName());
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
        dbHelper.closeDB();
	}
}

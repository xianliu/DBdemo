package com.sicau.Dao;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 13-6-15.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     *
     * @param contactList
     */
    public void create(List<Contact> contactList) {
        db.beginTransaction();	//开始事务
        try {
            for (Contact contact : contactList) {
                db.execSQL("INSERT INTO contact VALUES(null, ?, ?)", new Object[]{contact.name, contact.phone});
            }
            db.setTransactionSuccessful();	//设置事务成功完成
        } finally {
            db.endTransaction();	//结束事务
        }
    }


    public void update(Contact contact) {
        ContentValues cv = new ContentValues();
        cv.put("name", contact.name);
        cv.put("phone", contact.phone);
        db.update("contact", cv, "_id = ?", new String[]{String.valueOf(contact._id)});
    }


    public void delete(Contact contact) {
        db.delete("contact", "_id= ?", new String[]{String.valueOf(contact._id)});
    }


    public List<Contact> query() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Contact contact = new Contact();
            contact._id = c.getInt(c.getColumnIndex("_id"));
            contact.name = c.getString(c.getColumnIndex("name"));
            contact.phone = c.getString(c.getColumnIndex("phone"));

            contactList.add(contact);

        }
        c.close();
        return contactList;
    }

    /**
     * query all persons, return cursor
     * @return	Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM contact", null);
        return c;
    }

    /**
     * close database
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void closeDB() {
        db.close();
    }
}

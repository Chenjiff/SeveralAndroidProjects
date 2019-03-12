package com.code.chenjifff.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class appDAO extends SQLiteOpenHelper {
    private static final String DB_NAME = "app.db";
    private static final String TABLE_NAME = "data";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase defaultWritableDB;

    public appDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        defaultWritableDB = db;
        String createTableSql = "CREATE TABLE " + TABLE_NAME
                + "(id PRIMARY KEY, year INTEGER, month INTEGER, " +
                "day INTEGER, week INTEGER, todo TEXT)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //暂时不需实现
    }

    //使用SQLiteStatement实现数据库的insert,update,delete。
    //return the row ID of the last row inserted, if this insert is successful. -1 otherwise.
    public long insert(CustomDate date, String todo) {
        SQLiteDatabase db = getWritableDatabase();
        String insertSql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(insertSql);
        int index = 1;
        statement.bindLong(index++, date.getYear()*366+date.getMonth()*31+ date.getDay());
        statement.bindLong(index++, date.getYear());
        statement.bindLong(index++, date.getMonth());
        statement.bindLong(index++, date.getDay());
        statement.bindLong(index++, date.getWeek());
        statement.bindString(index++, todo);
        long rowID = statement.executeInsert();
        return rowID;
    }

    //return the number of rows affected by this SQL statement execution.
    public int delele(CustomDate date) {
        SQLiteDatabase db = getWritableDatabase();
        String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        SQLiteStatement statement = db.compileStatement(deleteSql);
        statement.bindLong(1, date.getYear()*366+date.getMonth()*31+ date.getDay());
        int affectedRows = statement.executeUpdateDelete();
        return affectedRows;
    }

    //通过name查询
    public Cursor query(String name) {
        //暂时不需要实现
        return null;
    }

    //查询所有characters表的数据项
    public Cursor queryAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

}


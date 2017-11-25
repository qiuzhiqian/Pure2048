package com.mycode.xml.pure2048;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xia_m on 2017/11/12/0012.
 */

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context,String path) {
        super(context, path, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + DB_Table + " (Id integer primary key AUTOINCREMENT, rankdate text, Scole integer, useTime integer,mode integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + DB_Table;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    private static String DB_Name="rank.db";
    private static int DB_Version=1;
    public static String DB_Table="ScoleRank";



}

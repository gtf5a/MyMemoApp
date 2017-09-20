package com.example.shohei.mymemoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBを使用するためのOpenHelperを作る
 * Created by shohei on 2017/08/11.
 */

public class MemoOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 1;

    // デフォルトで現在日時と時刻を入れる
    public static final String CREATE_TABLE =
            "create table memos (" +
                    "_id integer primary key autoincrement, " +
                    "title text, " +
                    "body text, " +
                    "created datetime default current_timestamp, " +
                    "updated datetime default current_timestamp)";

    // 適当にデータ入れる
    public static final String INIT_TABLE =
            "insert into memos (title, body) values " +
                    "('t1', 'b1'), " +
                    "('t2', 'b2'), " +
                    "('t3', 'b3')";

    public static final String DROP_TABLE =
            "drop table if exists " + MemoContract.Memos.TABLE_NAME;


    public MemoOpenHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    //
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 削除する
        db.execSQL(DROP_TABLE);
        // 初期化する
        onCreate(db);
    }
}

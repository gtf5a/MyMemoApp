package com.example.shohei.mymemoapp;

import android.provider.BaseColumns;

/**
 * インスタンス化されない
 * Created by shohei on 2017/08/11.
 */

public final class MemoContract {

    public MemoContract() {}

    public static abstract class Memos implements BaseColumns {
        public static final String TABLE_NAME = "memos";
        public static final String COL_TITLE = "title";
        public static final String COL_BODY = "body";
        // 何かと便利なので [作成日時] と [更新日時] は把持しておくと良い
        public static final String COL_CREATE = "created";
        public static final String COL_UPDATED = "updated";
    }
}

package com.example.shohei.mymemoapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * ContentProviderとは…
 * 元々アプリ間でデータを共有するための仕組み（マクロを使えばできるらしい）
 * アプリ内で完結するため今回マクロは使わない
 * CursorLoaderから呼び出してDBにアクセスできる
 * 定数CONTENT_URIでどのテーブルのどのアイテムを使うか識別する
 * UriMatcherはCONTENT_URIをチェック/処理を分岐する
 *
 * Created by shohei on 2017/08/11.
 */

public class MemoContentProvider extends ContentProvider {
    public static final String AUTHORITY =
            "com.example.shohei.mymemoapp.MemoContentProvider";
    public static final Uri CONTENT_URI =
            // CONTENT_URIは被らないようにする必要がある（長いけどAUTHORITY使う）
            Uri.parse("content://" + AUTHORITY + "/" + MemoContract.Memos.TABLE_NAME);

    // UriMatcher
    private static final int MEMOS = 1;
    private static final int MEMO_ITEM = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME, MEMOS);
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME + "/#", MEMO_ITEM);
    }

    private MemoOpenHelper memoOpenHelper;

    public MemoContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != MEMO_ITEM) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        int deletedCount = db.delete(
                MemoContract.Memos.TABLE_NAME,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != MEMOS) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        long newId = db.insert(
                MemoContract.Memos.TABLE_NAME,
                null,
                values
        );
        Uri newUri = ContentUris.withAppendedId(
                MemoContentProvider.CONTENT_URI,
                newId
        );
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public boolean onCreate() {
        memoOpenHelper = new MemoOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {
        switch (uriMatcher.match(uri)) {
            case MEMOS:
            case MEMO_ITEM:
                break;
            default:
                // 正常処理以外は例外
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getReadableDatabase();
        // Cursorで渡ってきた色々なものを使用する
        Cursor c = db.query(
                MemoContract.Memos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        c.setNotificationUri(getContext().getContentResolver(), uri);
        // ContentProviderの中ではクローズする必要がない
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uriMatcher.match(uri) != MEMO_ITEM) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        int updatedCount = db.update(
                MemoContract.Memos.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedCount;
    }
}

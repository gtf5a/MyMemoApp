package com.example.shohei.mymemoapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // 使用するのにアダプターが必要
    private SimpleCursorAdapter adapter;


    public final static String EXTRA_MYID = "com.example.shohei.mymemoapp.MYID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = {
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED
        };

        int[] to = {
                android.R.id.text1,
                android.R.id.text2
        };

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                from,
                to,
                0
        );

        ListView myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra(EXTRA_MYID, id);
                startActivity(intent);
            }
        });

        // Loaderを初期化
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, FormActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 引っ張ってくるものを配列に
        String[] projection = {
                MemoContract.Memos._ID,
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED
        };

        // 実行されるクエリを返す
        return new CursorLoader(
                this,
                MemoContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                MemoContract.Memos.COL_UPDATED + " DESC"
        );
    }

    // 渡ってきたデータでアダプターを更新
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    // リセットされて帰ってきた時実行される
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

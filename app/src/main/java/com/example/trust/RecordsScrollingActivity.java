package com.example.trust;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecordsScrollingActivity extends AppCompatActivity {

    private final String TAG = "RecordsScr_Activity";
    public ListView mLvDatabase = null;
    SQLiteDatabase db = null;
    DBHelper helper = null;
    Cursor cursor = null;
    CursorAdapter cursorAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLvDatabase = findViewById(R.id.lv_database);
        mLvDatabase.setOnItemLongClickListener(mLongClickListener);
        mLvDatabase.setOnItemClickListener(mClickListener);


        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("select * from log", null);
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_activated_2,
                cursor,
                new String[]{"title", "cal"},
                new int[]{android.R.id.text1, android.R.id.text2},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        cursorAdapter.notifyDataSetChanged();
        mLvDatabase.setAdapter(cursorAdapter);
        //insertDB("상도동", "123.4444", "987.7777", "1762");

    }

    AdapterView.OnItemLongClickListener mLongClickListener = new AdapterView.OnItemLongClickListener() {
        //길게 누르면 기록에서 삭제
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            cursor.moveToPosition(position);
            db = helper.getWritableDatabase();
            db.delete("log", "_id=?", new String[]{cursor.getString(0)}); // delete

            db = helper.getReadableDatabase();
            cursor = db.query("log", null, null, null, null, null, null);
            cursorAdapter.changeCursor(cursor);
            cursorAdapter.notifyDataSetChanged();
            return true;
        }
    };

    AdapterView.OnItemClickListener mClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            Log.d("title : ", cursor.getString(1));
            Log.d("latitude : ", cursor.getString(2));
            Log.d("longitude : ", cursor.getString(3));
            Log.d("speed : ", cursor.getString(4));
            Log.d("cal", cursor.getString(5));
            Toast.makeText(getApplicationContext(), "title :"+cursor.getString(1), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "lati :"+cursor.getString(2), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "longi :"+cursor.getString(3), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "speed :"+cursor.getString(4), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "cal :"+cursor.getString(5), Toast.LENGTH_SHORT).show();
        }
    };
}

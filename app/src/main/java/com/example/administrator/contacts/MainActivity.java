package com.example.administrator.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private ListView listData;
    private TextView noContact;
    private DbAdapter dbAdapter;
    private SimpleCursorAdapter dataAdapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listData = findViewById(R.id.list_contacts);
        noContact = findViewById(R.id.no_contact);
        dbAdapter = new DbAdapter(this);
        Log.i("dbAdapter=", String.valueOf(dbAdapter.listContacts().getCount()));
        if(dbAdapter.listContacts().getCount() == 0){
            listData.setVisibility(View.INVISIBLE);
            noContact.setVisibility(View.VISIBLE);
        }else{
            listData.setVisibility(View.VISIBLE);
            noContact.setVisibility(View.INVISIBLE);
        }
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //取得點下去當下的資料列
                Cursor item_cursor = (Cursor) listData.getItemAtPosition(i);
                String item_name = item_cursor.getString(item_cursor.getColumnIndexOrThrow("name"));
                Log.i("item_name=",item_name);
                intent = new Intent();
                intent.putExtra("type","edit");
                intent.putExtra("item_name",item_name);
                intent.setClass(MainActivity.this, ShowActivity.class );
                startActivity(intent);
                finish();
            }
        });
        displaylistView();
    }
    private void displaylistView(){
        Cursor cursor = dbAdapter.listContacts();
        String[] columns = new String[]{
                dbAdapter.KEY_NAME,
                dbAdapter.KEY_PHONE,
                dbAdapter.KEY_EMAIL
        };
        int[] to = new int[]{
                R.id.lbl_name,
                R.id.lbl_phone,
                R.id.lbl_mail
        };

        dataAdapter = new SimpleCursorAdapter(this,R.layout.item_view, cursor, columns, to, 0);
        listData.setAdapter(dataAdapter);
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor current_cursor = (Cursor) listData.getItemAtPosition(position);
                int item_id = current_cursor.getInt(current_cursor.getColumnIndex("_id"));
                intent = new Intent();
                intent.putExtra("item_id", item_id);
                Log.i("item_id",String.valueOf(item_id));
                intent.setClass(MainActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                intent = new Intent();
                intent.putExtra("type","add");
                intent.setClass(MainActivity.this, EditActivity.class );
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}


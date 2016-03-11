package keegancampbell.photonotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

// FOR THE WHOLE APP:
// TODO(kfcampbell):
// 3. Display picture and caption in ViewPhoto Activity.


public class ListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // test of looking up database to display in listView:
        final List<String> items = new ArrayList<String>();
        SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
        items.clear();

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = { DBHelper.ID_COLUMN, DBHelper.CAPTION_COLUMN, DBHelper.FILE_PATH_COLUMN };
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);

        while (cursor.moveToNext()) {

            // int id = cursor.getInt(0);

            String note = cursor.getString(1);
            items.add(note);

        }

        cursor.close();
        db.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ListActivity.this, ViewPhoto.class);

                // load up bundle to pass to ViewPhoto Activity
                Bundle extras = new Bundle();
                extras.putInt("ID", position);
                intent.putExtras(extras);
                startActivity(intent);
            }

        });
        registerForContextMenu(listView);

        // test values for listView
        /*String[] items = {"bat", "cat", "rat"};
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ListActivity.this, ViewPhoto.class);

                // bundle goes here to pass ID column number to ViewPhoto Activity
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_uninstall)
        {

            Intent intent = new Intent(ListActivity.this, Uninstall.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.takePhoto)
        {
            Intent intent = new Intent(ListActivity.this, AddPhoto.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

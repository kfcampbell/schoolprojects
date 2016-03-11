package keegancampbell.photonotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewPhoto extends ActionBarActivity {

    // had to make a new class (Triple) to hold the id, caption, and filepath as
    // a hashmap can only hold a key and value pair.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        // get ID number from the database
        Integer value = getIntent().getExtras().getInt("ID");

        // put database into list:
        HashMap map = new HashMap<Integer, Triple>();
        SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
        map.clear();

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = { DBHelper.ID_COLUMN, DBHelper.CAPTION_COLUMN,
                DBHelper.FILE_PATH_COLUMN };
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where,
                whereArgs, groupBy, having, order);

        while (cursor.moveToNext())
        {

            // int id = cursor.getInt(0);

            // cursor.getString(0): id?
            // cursor.getString(1): caption
            // cursor.getString(2): filepath
            Integer id = cursor.getInt(0);
            String cap = cursor.getString(1);
            String fp = cursor.getString(2);
            map.put(id, new Triple(id, fp, cap));

        }

        // get specific item using ID number from intent
        Triple item = (Triple) map.get(value + 1);
        String caption = item.cap;
        String filepath = item.fp;

        // set the caption
        TextView textView = (TextView) findViewById(R.id.captionDisplay);
        textView.setText(caption);

        // get the image to be displayed
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;  // Experiment with different sizes
        Bitmap bm = BitmapFactory.decodeFile(filepath, options);

        // set the image to be displayed
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_uninstall)
        {

            Intent intent = new Intent(ViewPhoto.this, Uninstall.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.takePhoto)
        {
            Intent intent = new Intent(ViewPhoto.this, AddPhoto.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

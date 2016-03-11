package keegancampbell.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Operations
{
    private void update(String caption, String filepath)
    {
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
        String whereClause = DBHelper.ID_COLUMN + "= ?";
        String[] whereArgs = {"1"};
        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.CAPTION_COLUMN, caption);
        newValues.put(DBHelper.FILE_PATH_COLUMN, filepath);
        db.update(DBHelper.DATABASE_TABLE, newValues, whereClause, whereArgs);
    }

    private void delete()
    {
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
        String whereClause = DBHelper.ID_COLUMN + "=?";
        String[] whereArgs = {"2"};
        db.delete(DBHelper.DATABASE_TABLE, whereClause, whereArgs);
    }

    private void query()
    {
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {DBHelper.ID_COLUMN, DBHelper.CAPTION_COLUMN, DBHelper.FILE_PATH_COLUMN};
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String filepath = cursor.getString(3);
            Log.d("Photo", String.format("%s,%s,%s,%s", id, name, description, filepath));
        }
    }
    private void insert(String caption, String filepath)
    {
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.CAPTION_COLUMN, caption);
        newValues.put(DBHelper.FILE_PATH_COLUMN, filepath);
        db.insert(DBHelper.DATABASE_TABLE, null, newValues);
    }

    // Kseniya's query method

    private void query1()
    {
        List<String> test = new ArrayList<String>();
        SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
        test.clear();

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = { DBHelper.ID_COLUMN, DBHelper.CAPTION_COLUMN,
                DBHelper.FILE_PATH_COLUMN };
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where,
                whereArgs, groupBy, having, order);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String note = cursor.getString(1);
            test.add(note);

        }

        cursor.close();
        db.close();

    }

}

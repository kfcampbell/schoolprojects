package keegancampbell.photonotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class AddPhoto extends ActionBarActivity {

    public File getAlbumStorageDir(String albumName) {
// Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("TAG", "Directory not created");
        }
        return file;
    }

    private File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getAlbumStorageDir("Zoo");
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return image;
    }

    private static final int RESULT_CODE = 0;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
        // Error occurred while creating the File
            }
        // Continue only if the File was successfully created
            if (photoFile != null) {
                // get filepath and caption
                TextView caption = (TextView) findViewById(R.id.caption);
                String photoTag = caption.getText().toString();
                String path = photoFile.getAbsolutePath();

                // store filepath and caption in database
                SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
                ContentValues newValues = new ContentValues();
                newValues.put(DBHelper.CAPTION_COLUMN, photoTag);
                newValues.put(DBHelper.FILE_PATH_COLUMN, path);
                db.insert(DBHelper.DATABASE_TABLE, null, newValues);

                // take the picture associated with the filepath and caption
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, RESULT_CODE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Button takePicture = (Button) findViewById(R.id.takePhotoButton);
        Button savePicture = (Button) findViewById(R.id.saveButton);
        //TextView caption = (TextView) findViewById(R.id.caption);
        //ImageView imageView = (ImageView) findViewById(R.id.imageView);


        takePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

        savePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_photo, menu);
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

            Intent intent = new Intent(AddPhoto.this, Uninstall.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package keegancampbell.tentativefinal;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


// TODO(kcampbell):
// 2. Allow filtering of API calls by user (by location, etc.)
// 7. Save scrolling position when returning to app.
// 8. Switch to browsing by category?

// API KEY: 54d14bc043df675534f5bfed


public class MainActivity extends ActionBarActivity {

    // set URL for testing purposes
    // extra URLs are for demonstrations/testing different types of data
    // in future versions of the app, the user will be able to specify what they want to look at.

    // very twitter/facebook-heavy url
    //String url = "http://api.crisis.net/item?after=2015-02-10T10:50:42.389Z&limit=500&apikey=54d14bc043df675534f5bfed";

    // separate twitter/facebook-heavy url
    String url = "http://api.crisis.net/item?categories=disaster,conflict,ethnicity,knowngroup,religion&apikey=54d14bc043df675534f5bfed";

    // url from only reputable sources
    //String url = "http://api.crisis.net/item?sources=ushahidi_v2%2Cvdc_syria%2Creliefweb&apikey=54d14bc043df675534f5bfed";

    // some variable declarations to detect shaking
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set title of activity to something descriptive
        setTitle("Recent Activity");

        // stuff for detecting acceleration
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        // allow network stuff in main thread just for ease of development. will be changed in production version
        // for best practice and to speed up delays in loading app activities.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final List<Crisis> crises = loadData(url);

        displayData(crises);
    }

    private void displayData(final List<Crisis> crises) {
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(this, R.layout.custom_row, crises));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // passes crisis details to the pop-up from DetailActivity
                Crisis crisis = crises.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("id", crisis.getId());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private List<Crisis> loadData(String url) {

        // put information into string
        String input = readCrisis(url);

        // initialize list of Crises
        final List<Crisis> crises = new ArrayList<>();

        // put information into JSONObject
        try {

            // get whole package of information into a single JSONObject
            JSONObject json = new JSONObject(input);
            Log.i(MainActivity.class.getName(), json.toString());

            // put object into an array, iterate through it and withdraw relevant information
            JSONArray jsonarr = json.getJSONArray("data");
            for (int i = 0; i < jsonarr.length(); i++)
            {
                // look inside string for name
                JSONObject nameJson = new JSONObject(jsonarr.getString(i));

                // put in custom Crisis class. name and id (for name) will be displayed in rows of MainActivity
                crises.add(new Crisis(nameJson.getString("summary"), nameJson.getString("id")));

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return crises;
    }

    // API call using HttpGet. Read basic data from all stories
    public String readCrisis(String url) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e("Main Activity error: ", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    // for shaking to refresh stories
    private final SensorEventListener mSensorListener = new SensorEventListener()
    {
        public void onSensorChanged(SensorEvent se)
        {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            // test to see if phone is shaken
            int accel = Math.abs((int) mAccel);
            if(accel > .99)
            {
                // refresh by reloading activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onResume()
    {
        super.onResume();
        final List<Crisis> crises = loadData(url);
        displayData(crises);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause()
    {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.infoApp:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;
            case R.id.uninstallApp:
                Intent intent3 = new Intent(this, Uninstall.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
    }

}

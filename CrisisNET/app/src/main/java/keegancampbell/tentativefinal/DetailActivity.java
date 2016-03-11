package keegancampbell.tentativefinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // find TextView header and body fields to be assigned later
        TextView header = (TextView) findViewById(R.id.title);
        TextView body = (TextView) findViewById(R.id.summary);

        // receive bundle
        String id = getIntent().getExtras().getString("id");
        // log received id
        Log.d("Received id", id);

        // store apikey here; cannot store it in strings.xml or some weird things happen and it can't be called correctly
        String API_KEY = "54d14bc043df675534f5bfed";

        // generate url for API call
        String url = "http://api.crisis.net/item?ids=" + id + "&apikey=" + API_KEY;

        // log url used for testing purposes
        Log.d("DA URL used: ", url);

        // read crisis into string
        String item = readCrisis(url);

        try {

            // get whole package of information into a single JSONObject
            JSONObject json = new JSONObject(item);
            Log.i(MainActivity.class.getName(), json.toString());

            // put object into an array, iterate through it and withdraw relevant information
            JSONArray jsonarr = json.getJSONArray("data");
            for (int i = 0; i < jsonarr.length(); i++)
            {
                // look inside string for new object
                JSONObject nameJson = new JSONObject(jsonarr.getString(i));

                // set title of page here
                setTitle(nameJson.getString("summary"));

                // initialize basic variables: coordinates and content
                JSONObject geo = new JSONObject(nameJson.getString("geo"));

                // check to see if coordinates present
                if(geo.has("coords"))
                {
                    String coordinates = geo.getString("coords");

                    // time to make coordinates usable in Google Maps
                    // remove brackets here
                    final String brackstring = coordinates.replace("[", "").replace("]", "");

                    // log brackstring just to make sure
                    Log.d("brackstring: ", brackstring);

                    // redundant but using to test
                    // 7z seems to be an appropriate level of zoom
                    String maps = "https://www.google.com/maps/@" + brackstring + ",7z";
                    Log.d("map used: ", maps);

                    // find button for maps. option to open in browser or maps.
                    Button map = (Button) findViewById(R.id.mapsButton);
                    map.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // this isn't being logged for some reason.
                            // probably contributes to the error in viewing the location in Maps.
                            String maps = "http://maps.google.com/maps?q=" + brackstring + "&z=7";
                            Log.d("map used (button ver): ", maps);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(maps));
                            startActivity(intent);
                        }
                    });

                    // display coordinate location in header. not super attached to this
                    header.setText("Coordinates: [" + brackstring + "]");
                }
                else
                {
                    // set header to display the lack of coordinates
                    header.setText(R.string.no_coords);

                    // display toast message upon maps button click
                    Button map = (Button) findViewById(R.id.mapsButton);
                    map.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(getApplicationContext(), R.string.no_coords, Toast.LENGTH_LONG).show();
                        }
                    });

                }

                // check to see if content present
                if(nameJson.has("contentEnglish"))
                {
                    String content = nameJson.getString("contentEnglish");
                    body.setText(content);
                }
                else
                {
                    if(nameJson.has("content"))
                    {
                        body.setText(nameJson.getString("content"));
                    }
                    else
                    {
                        body.setText(R.string.no_body);
                    }
                }

                if(nameJson.has("image"))
                {
                    // download image from web in order to display in-app
                    ImageView webImage = (ImageView) findViewById(R.id.webImage);
                    ImageDownloader pic = new ImageDownloader();
                    pic.download(nameJson.getString("image"), webImage);
                }
                else
                {
                    ImageView noimage = (ImageView) findViewById(R.id.webImage);
                    noimage.setImageResource(R.drawable.noimage);
                }

                // get url to display in browser
                if(nameJson.has("fromURL"))
                {
                    final String storyUrl = nameJson.getString("fromURL");

                    // find button and initialize browser view
                    Button web = (Button) findViewById(R.id.webButton);
                    web.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // if clicked, open link up in browser
                            Uri uri = Uri.parse(storyUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);

                        }
                    });
                }

                // otherwise display a toast message because no URL
                else
                {
                    Button web = (Button) findViewById(R.id.webButton);
                    web.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(getApplicationContext(), R.string.no_url, Toast.LENGTH_LONG).show();
                        }
                    });
                }


                // hopefully this won't have to be instantiated.
                // if there's actual coordinates for the activity clicked, then we're good.
                // if not, this'll get activated i think.
                // button for maps. option to open in browser or maps.
                Button map = (Button) findViewById(R.id.mapsButton);
                map.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=" + "47.6588889" + "," + "-117.425"));
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Error: ", "Exception in DetailActivity");
        }


    }

    public String readCrisis(String url)
    {
        // Log url just to make sure
        Log.i("and the url is: ", url);

        // HttpGet process to read data from individual stories
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
                Log.e("Detail Activity error: ", "Failed to download file");
            }
        /*} catch (ClientProtocolException e) { // not sure why this is here.
            e.printStackTrace();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
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
}

package keegancampbell.zoodirectory;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import static keegancampbell.zoodirectory.R.drawable.*;

// TODO (kfcampbell)
// none

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get intent and set name and filename variables
        String value = getIntent().getExtras().getString("name");

        if(value != null) {

            // set name of person
            TextView title = (TextView) findViewById(R.id.largeName);
            title.setText(value);

            // find description tag
            TextView description = (TextView) findViewById(R.id.description);

            // find picture tag. fill in picture and description.
            ImageView picture = (ImageView) findViewById(R.id.picture);
            if(value.equals("Ron Swanson")) {
                picture.setImageResource(R.drawable.swanson);
                description.setText("'When people get a little too chummy with me I like to call them by the wrong name to let them know I don’t really care about them.'");
            }
            else if(value.equals("Leslie Knope")) {
                picture.setImageResource(R.drawable.knope);
                description.setText("'There are very few things I have asked for in this world. To build a new park from scratch, to eventually become president and to one day solve a murder on a train.'");
            }
            else if(value.equals("Tom Haverford"))
            {
                picture.setImageResource(R.drawable.haverford);
                description.setText("'At the risk of bragging, one of the things I’m best at is riding coattails. Behind every successful man is me. Smiling and taking partial credit.'");
            }
            else if(value.equals("April Ludgate")) {
                picture.setImageResource(R.drawable.ludgate);
                description.setText("'I’ll have a glass of your most expensive red wine mixed with a glass of your cheapest white wine served in a dog bowl. Silly straws all around, please.'");
            }
            else {
                picture.setImageResource(R.drawable.gergich);
                description.setText("'I think that comic sans always screams fun, right?'");
            }
        }

    }

    // boilerplate code

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, ListViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.zooInfo:
                Intent intent2 = new Intent(this, ZooInformation.class);
                startActivity(intent2);
                return true;
            case R.id.uninstall:
                Intent intent3 = new Intent(this, Uninstall.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

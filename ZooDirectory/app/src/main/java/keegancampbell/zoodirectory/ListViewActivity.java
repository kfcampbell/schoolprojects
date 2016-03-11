package keegancampbell.zoodirectory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


// NOTE: OPTION + RETURN IS HOW TO ADD MISSING IMPORTS


// TODO (kcampbell)
// none


// ListView id = listView

public class ListViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        final List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Ron Swanson", "ron.jpg"));
        animals.add(new Animal("Leslie Knope", "leslie.jpg"));
        animals.add(new Animal("Tom Haverford", "tom.jpg"));
        animals.add(new Animal("April Ludgate", "april.jpg"));
        animals.add(new Animal("Jerry Gergich", "jerry.jpg"));

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(this, R.layout.custom_row, animals));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // passes animal details to the pop-up from DetailActivity
                Animal animal = animals.get(position);
                Intent intent = new Intent(ListViewActivity.this, DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", animal.getName());
                intent.putExtras(extras);

                // check to see if Jerry was clicked
                // if so, pop up alert dialog box
                if(animal.getName() == "Jerry Gergich")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListViewActivity.this);
                    builder.setMessage("Are you sure you want to proceed?");
                    builder.setTitle("Warning: Jerry's the Worst");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // open up Jerry's DetailActivity
                            Animal animal = animals.get(position);
                            Intent intent = new Intent(ListViewActivity.this, DetailActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("name", animal.getName());
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // return to ListViewActivity
                            dialog.cancel();
                            Intent intent = new Intent(ListViewActivity.this, ListViewActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                if(animal.getName() != "Jerry Gergich")
                {
                    startActivity(intent); // making sure the bundle gets passed to the new activity
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        getMenuInflater().inflate(R.menu.menu_detail, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

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

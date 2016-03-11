package keegancampbell.tentativefinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// adapted version of Homework 2 CustomAdapter
public class CustomAdapter extends ArrayAdapter<Crisis> {

    private final List<Crisis> crises;

    public CustomAdapter(Context context, int resource, List<Crisis> crises)
    {
        super(context, resource, crises);
        this.crises = crises;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getCrisisView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCrisisView(position, convertView, parent);
    }

    public View getCrisisView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row, null);

        TextView textView = (TextView) row.findViewById(R.id.rowText);
        textView.setText(crises.get(position).getType());
        // if wanting to display id in row as well:
        // + " (" + crises.get(position).getId() + ")"

        try
        {
            // find rowImage to change icon to // commented out in here and in customRow.xml
            // ImageView imageView = (ImageView) row.findViewById(R.id.rowImage);

            // retrieve the type of crisis
            String filename = crises.get(position).getType();
        }

        catch (Exception e) // IOException e
        {
            e.printStackTrace();
        }

        return row;
    }
}

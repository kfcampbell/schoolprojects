package keegancampbell.mortgagecalculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

// TODO (kcampbell):
// 1. none

public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener
{

    // VARIABLES
    Double barAmount; // counter variable for SeekBar
    Double totalAmount; // amount to display in monthlyPayment
    Double borrowed; // initial amount borrowed (get from amountBorrowed XML)
    Double loanLength; // get from radioButtons
    Double plusTaxes = 0.0; // T value. Get from chkTaxes Checkbox
    Double monthlyInterest; // for calculating monthly payment

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find amountBorrowed field
        final EditText amountBorrowed = (EditText) findViewById(R.id.amountBorrowed);

        // find calculate button
        Button calc = (Button) findViewById(R.id.calculate);

        // find interestRateBar
        final SeekBar bar = (SeekBar)findViewById(R.id.interestRateBar);

        // find radioGroup and associated radioButtons
        // seven, fifteen, thirty, radioGroup
        final RadioGroup radLoanLength;
        final RadioButton radSeven;
        final RadioButton radFifteen;
        final RadioButton radThirty;

        radLoanLength = (RadioGroup) findViewById(R.id.radioGroup);

        radSeven = (RadioButton) findViewById(R.id.seven);
        radFifteen = (RadioButton) findViewById(R.id.fifteen);
        radThirty = (RadioButton) findViewById(R.id.thirty);

        // find CheckBox chkTaxes
        final CheckBox chkTaxes;
        chkTaxes = (CheckBox) findViewById(R.id.taxes);

        // find monthlyPayment TextView
        final TextView monthlyPayment = (TextView) findViewById(R.id.monthlyPayment);

        // find interestRateText to update with value of SeekBar bar
        final TextView interestRate = (TextView) findViewById(R.id.interestRateText);



        // Things to do when calculate button is pushed
        calc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // read from SeekBar bar here
                barAmount = (double)bar.getProgress();
                interestRate.setText("Interest Rate: " + barAmount);
                monthlyInterest = barAmount/1200;


                // read from XML amountBorrowed into variable, change to double
                // the following log for troubleshooting purposes:
                // Log.v("Amount Borrowed", amountBorrowed.getText().toString());
                borrowed = Double.valueOf(amountBorrowed.getText().toString());
                if(borrowed == 0.0)
                {
                    // throw exception here?
                    // to save the app from quitting when no value is inputted for loan amount
                    borrowed = 10.0;
                }

                // see if checkbox is checked
                if(chkTaxes.isChecked())
                {
                    // as described in homework:
                    // plusTaxes = (0.1)*borrowed;
                    // but that doesn't work on a month-by-month basis
                    // so I've audibled to the following, using the standard set by monthlyInterest:
                    plusTaxes = ((0.1)*borrowed)/1200;
                }

                // get Radio Buttons' Id number
                int radioID = radLoanLength.getCheckedRadioButtonId();
                if(radSeven.getId() == radioID)
                {
                    loanLength = 84.0;
                }
                if(radFifteen.getId() == radioID)
                {
                    loanLength = 180.0;
                }
                if(radThirty.getId() == radioID)
                {
                    loanLength = 360.0;
                }

                // perform totalAmount calculation and update monthlyPayment TextView
                if(barAmount != 0)
                {
                    // step-by-step, according to formula on class website
                    totalAmount = (1.0) - (1.0)*(Math.pow(1.0 + monthlyInterest, (-1.0) * loanLength)); // covers denominator
                    totalAmount = monthlyInterest/totalAmount; // covers fraction
                    totalAmount = borrowed*totalAmount; // covers principal multiplication
                    totalAmount = totalAmount + plusTaxes; // covers plusTaxes
                    totalAmount = (double)Math.round(totalAmount * 100) / 100; // quick/dirty format to two decimal places
                    monthlyPayment.setText("$" + Double.toString(totalAmount)); // displays monthlyPayment up top
                }
                else if(barAmount == 0)
                {
                    totalAmount = (borrowed/loanLength) + plusTaxes;
                    totalAmount = (double)Math.round(totalAmount * 100) / 100; // quick/dirty format to two decimal places
                    monthlyPayment.setText("$" + Double.toString(totalAmount)); // displays monthlyPayment up top
                }

            }

        });
    }

    // required SeekBar listener stuff here
    @Override
    public void onProgressChanged(SeekBar bar, int progress, boolean fromUser)
    {
        // update TextView with interestRate here?
        // final TextView interestRate = (TextView) findViewById(R.id.interestRateText);
        // barAmount = (double)bar.getProgress();
        // interestRate.setText("Interest Rate: " + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // placeholder (unnecessary)
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // placeholder (unnecessary)
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

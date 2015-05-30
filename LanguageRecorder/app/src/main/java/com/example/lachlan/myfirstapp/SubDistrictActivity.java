package com.example.lachlan.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lachlan.myfirstapp.R;

import java.util.ArrayList;
import java.util.List;

public class SubDistrictActivity extends ActionBarActivity {
    Spinner sd_spinner;
    private ArrayAdapter<String> aaSubDistricts;
    private List<String> subDistricts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Which district are you in?");
        setContentView(R.layout.activity_sub_district);
        //todo: How do we get the municipality from the previous screen?
        populateSubDistricts("blah");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_district, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private void populateSubDistricts(String _municipality) {
        String subDistrict, municipality;
        sd_spinner = (Spinner) findViewById(R.id.spinner);
        subDistricts = new ArrayList<String>();
        String[] locs = locations();
        for (int i = 0; i < locations().length; i++) {
            municipality = locs[i].split("|")[0];
            if (_municipality.equals(municipality)) {
                subDistrict = locs[i].split("|")[1];
                subDistricts.add(subDistrict);
            }
        }
        aaSubDistricts = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locs);
        aaSubDistricts
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sd_spinner.setAdapter(aaSubDistricts);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

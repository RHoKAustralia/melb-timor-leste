package com.example.lachlan.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lachlan.myfirstapp.R;
import com.example.lachlan.myfirstapp.code.Location;

import java.util.ArrayList;
import java.util.List;

public class VillageActivity extends ActionBarActivity {
    Spinner v_spinner;
    private ArrayAdapter<String> aaVillages;
    private List<String> villages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Which district are you in?");
        setContentView(R.layout.activity_village);
        //todo: How do we get the municipality and sub district from the previous screen?
        populateVillages("blah", "blah");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_village, menu);
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

    private void populateVillages(String _municipality, String _subDistrict) {
        v_spinner = (Spinner) findViewById(R.id.spinner);
        villages = new ArrayList<String>();
        String[] locs = locations();
        for (int i = 0; i < locs.length; i++) {
            Location loc = new Location(locs[i]);
            if ((_municipality.equals(loc.municipality)) && (_subDistrict.equals(loc.subdistrict))) {
                villages.add(loc.village);
            }
        }
        aaVillages = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locs);
        aaVillages
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        v_spinner.setAdapter(aaVillages);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

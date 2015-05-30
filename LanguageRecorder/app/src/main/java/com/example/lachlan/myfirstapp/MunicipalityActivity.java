package com.example.lachlan.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lachlan.myfirstapp.code.Location;

import java.util.ArrayList;
import java.util.List;


public class MunicipalityActivity extends ActionBarActivity {
    Spinner man_spinner;
    private ArrayAdapter<String> aaMunicipalities;
    private List<String> municipalities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Location");
        setContentView(R.layout.activity_municipality);
        populateMunicipalities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_municipality, menu);
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

    private void populateMunicipalities() {
        String municipality;
        man_spinner = (Spinner) findViewById(R.id.spinner);
        municipalities = new ArrayList<String>();
        String[] locs = locations();
        for (int i = 0; i < locations().length; i++) {
            municipality = locs[i].split("|")[0];
            municipalities.add(municipality);
        }
        aaMunicipalities = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locs);
        aaMunicipalities
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        man_spinner.setAdapter(aaMunicipalities);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

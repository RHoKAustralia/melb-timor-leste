package com.example.lachlan.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lachlan.myfirstapp.code.Location;
import com.example.lachlan.myfirstapp.code.Municipality;
import com.example.lachlan.myfirstapp.code.SubDistrict;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MunicipalityActivity extends ActionBarActivity {
    private String selectedMunicipality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select municipality");
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
        ListView lvMunicipalities = (ListView) findViewById(R.id.municipality_list);
        ArrayList<String> list = new ArrayList<String>();
        for (Municipality mun : municipalities()) {
            list.add(mun.name);
        }
        ArrayAdapter<String> aaMunicipalities = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        aaMunicipalities
                .setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        lvMunicipalities.setAdapter(aaMunicipalities);
        lvMunicipalities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMunicipality = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private Set<Municipality> municipalities() {
        Location l;
        Set<Municipality> m = new HashSet<Municipality>();
        for (String loc : locations()) {
            l = new Location(loc);
            m.add(new Municipality(this, l.municipality));
        }
        return m;
    }

    public void loadSubDistrictActivity(android.view.View view) {
        Intent intent = new Intent(this, SubDistrictActivity.class);
        intent.putExtra("MUNICIPALITY", selectedMunicipality);
        startActivity(intent);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

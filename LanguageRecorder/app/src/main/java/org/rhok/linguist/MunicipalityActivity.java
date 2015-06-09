package org.rhok.linguist;

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

import org.rhok.linguist.code.Location;
import org.rhok.linguist.code.Municipality;
import org.rhok.linguist.code.SubDistrict;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MunicipalityActivity extends ActionBarActivity {
    private String selectedMunicipality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select municipality for study " + selectedStudy());
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
            if (!list.contains(mun.name)) {
                list.add(mun.name);
            }
        }
        Collections.sort(list);
        ArrayAdapter<String> aaMunicipalities = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        aaMunicipalities
                .setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        lvMunicipalities.setAdapter(aaMunicipalities);
        lvMunicipalities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(!view.isSelected());
                selectedMunicipality = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private ArrayList<Municipality> municipalities() {
        Location l;
        ArrayList<Municipality> m = new ArrayList<Municipality>();
        for (String loc : locations()) {
            l = new Location(loc);
            m.add(new Municipality(this, l.municipality));
        }
        return m;
    }

    private String selectedStudy() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("STUDY");
        }
        return value;
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

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

import org.rhok.linguist.R;
import org.rhok.linguist.code.Location;
import org.rhok.linguist.code.Municipality;
import org.rhok.linguist.code.SubDistrict;
import org.rhok.linguist.code.Village;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubDistrictActivity extends ActionBarActivity {
    private String selectedSubDistrict = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select sub-district for " + selectedMunicipality());
        setContentView(R.layout.activity_sub_district);
        populateSubDistricts();
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

    private void populateSubDistricts() {
        ListView lvSubDistricts = (ListView) findViewById(R.id.subdistrict_list);
        ArrayList<String> list = new ArrayList<String>();
        for (SubDistrict sd : subDistricts()) {
            if (!list.contains(sd.name)) {
                list.add(sd.name);
            }
        }
        Collections.sort(list);
        ArrayAdapter<String> aaSubDistricts = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        aaSubDistricts
                .setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        lvSubDistricts.setAdapter(aaSubDistricts);
        lvSubDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSubDistrict = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private ArrayList<SubDistrict> subDistricts() {
        Location l;
        ArrayList<SubDistrict> m = new ArrayList<>();
        for (String loc : locations()) {
            l = new Location(loc);
            if (l.municipality.equals(selectedMunicipality())) {
                m.add(new SubDistrict(this, l.municipality, l.subdistrict));
            }
        }
        return m;
    }

    public void loadVillageActivity(android.view.View view) {
        Intent intent = new Intent(this, VillageActivity.class);
        intent.putExtra("SUB_DISTRICT", selectedSubDistrict);
        startActivity(intent);
    }

    private String selectedMunicipality() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("MUNICIPALITY");
        }
        return value;
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

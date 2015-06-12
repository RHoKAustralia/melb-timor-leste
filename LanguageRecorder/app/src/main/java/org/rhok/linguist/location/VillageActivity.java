package org.rhok.linguist.location;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.rhok.linguist.HomeNewActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Location;
import org.rhok.linguist.code.Village;

import java.util.ArrayList;

public class VillageActivity extends ActionBarActivity {
    private String selectedVillage = "";
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select village for " + selectedSubDistrict());
        setContentView(R.layout.activity_village);
        populateVillages();
        from = getIntent().getExtras().getString("from");
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

    private void populateVillages() {
        ListView lvVillages = (ListView) findViewById(R.id.village_list);
        ArrayList<String> list = new ArrayList<String>();
        for (Village v : villages()) {
            if (!list.contains(v.name)) {
                list.add(v.name);
            }
        }

        ListViewPopulator.populate(this, R.id.village_list, list, true,
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedVillage = (String) parent.getItemAtPosition(position);
            }
        } );


    }

    private ArrayList<Village> villages() {
        Location l;
        ArrayList<Village> m = new ArrayList<>();
        for (String loc : locations()) {
            l = new Location(loc);
            if (l.subdistrict.equals(selectedSubDistrict())) {
                m.add(new Village(this, l.municipality, l.subdistrict, l.village));
            }
        }
        return m;
    }

    public void loadVillageActivity(android.view.View view) {
        Intent intent = new Intent(this, VillageActivity.class);
        intent.putExtra("VILLAGE", selectedVillage);
        startActivity(intent);
    }

    private String selectedSubDistrict() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("SUB_DISTRICT");
        }
        return value;
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }


    public void nextButtonClick(View view) {

        if (from == null) {
            // we have just come from the Setup section, so let's
            // go back to the home page
            Intent intent = new Intent(this, HomeNewActivity.class);
            startActivity(intent);
        } else {

            // we have just come from the User Details section,
            // so save them and take them to the home page
            Intent intent = new Intent(this, HomeNewActivity.class);
            startActivity(intent);
        }
    }
}

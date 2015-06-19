package org.rhok.linguist.location;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Location;
import org.rhok.linguist.code.SubDistrict;
import org.rhok.linguist.interview.BaseInterviewActivity;

import java.util.ArrayList;

public class SubDistrictActivity extends BaseInterviewActivity {
    private String selectedSubDistrict = "";
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_district);

        TextView whichDistrictTextView = (TextView) findViewById(R.id.whichDistrictTextView);
        whichDistrictTextView.setText("Select sub-district for " + selectedMunicipality());

        populateSubDistricts();
        from = getIntent().getExtras().getString("from");

        setTitle("Interview - District");
    }


    private void populateSubDistricts() {
        ListView lvSubDistricts = (ListView) findViewById(R.id.subdistrict_list);
        ArrayList<String> list = new ArrayList<String>();
        for (SubDistrict sd : subDistricts()) {
            if (!list.contains(sd.name)) {
                list.add(sd.name);
            }
        }

        ListViewPopulator.populate(this, R.id.subdistrict_list, list, true,
                new AdapterView.OnItemClickListener() {
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

    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, VillageActivity.class);
        intent.putExtra("SUB_DISTRICT", selectedSubDistrict);
        intent.putExtra("from", from);
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

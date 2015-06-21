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
import org.rhok.linguist.code.Person;
import org.rhok.linguist.code.SubDistrict;
import org.rhok.linguist.interview.BaseInterviewActivity;

import java.util.ArrayList;

public class SubDistrictActivity extends BaseInterviewActivity {
    private String selectedMunicipality = "";
    private String selectedSubDistrict = "";
    private String mode = null;
    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_district);

        Bundle extras = getIntent().getExtras();

        mode = extras.getString("mode");
        _person = (Person) extras.getSerializable("Person");

        int questionTextId = 0;

        if (mode.equals("lives")) {
            selectedMunicipality = _person.livesMunicipality;
            questionTextId = R.string.interview_district_lives;
        }
        if (mode.equals("born")) {
            selectedMunicipality = _person.bornMunicipality;
            questionTextId = R.string.interview_district_born;
        }

        TextView whichDistrictTextView = (TextView) findViewById(R.id.whichDistrictTextView);

        String whichDistrict =
                getResources()
                        .getString(questionTextId)
                        .replace("##municipality##", selectedMunicipality );

        whichDistrictTextView.setText(whichDistrict);

        populateSubDistricts();
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
            if (l.municipality.equals(selectedMunicipality)) {
                m.add(new SubDistrict(this, l.municipality, l.subdistrict));
            }
        }
        return m;
    }

    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, VillageActivity.class);
        if (mode.equals("lives")) {
            _person.livesDistrict = selectedSubDistrict;
        }
        else {
            _person.bornDistrict = selectedSubDistrict;
        }
        intent.putExtra("mode", mode);
        intent.putExtra("Person", _person);
        startActivity(intent);
    }



    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

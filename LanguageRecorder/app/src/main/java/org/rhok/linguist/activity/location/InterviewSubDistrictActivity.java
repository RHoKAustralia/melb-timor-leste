package org.rhok.linguist.activity.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.entity.Location;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.code.entity.SubDistrict;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;

import java.util.ArrayList;

public class InterviewSubDistrictActivity extends BaseInterviewActivity {
    private String selectedMunicipality = null;
    private String selectedSubDistrict = null;
    private String mode = null;
    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_sub_district);

        Bundle extras = getIntent().getExtras();

        mode = extras.getString("mode");
        _person = (Person) extras.getSerializable(IntentUtil.ARG_PERSON);

        int questionTextId = 0;

        if (mode.equals("lives")) {
            selectedMunicipality = _person.livesInMunicipality;
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

        if (selectedSubDistrict == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a sub district", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            if (mode.equals("lives")) {
                _person.livesInDistrict = selectedSubDistrict;
                dbHelper.updatePersonLivesDistrict(_person.personid, selectedSubDistrict);
            } else {
                _person.bornDistrict = selectedSubDistrict;
                dbHelper.updatePersonBornDistrict(_person.personid, selectedSubDistrict);
            }

            Intent intent = new Intent(this, InterviewVillageActivity.class);
            intent.putExtra("mode", mode);
            intent.putExtra(IntentUtil.ARG_PERSON, _person);
            startActivity(intent);
        }
    }



    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

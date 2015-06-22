package org.rhok.linguist.activity.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.entity.Location;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.code.entity.Village;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.activity.interview.InterviewLivedLifeActivity;
import org.rhok.linguist.activity.recording.RecordingInstructionsActivity;

import java.util.ArrayList;

public class InterviewVillageActivity extends BaseInterviewActivity {

    private String selectedDistrict = null;
    private String selectedVillage = null;
    private String mode = null;
    private Person _person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_village);

        Bundle extras = getIntent().getExtras();

        mode = extras.getString("mode");
        _person = (Person) extras.getSerializable("Person");

        int questionTextId = 0;

        if (mode.equals("lives")) {
            selectedDistrict = _person.livesDistrict;
            questionTextId = R.string.interview_village_lives;
        }
        if (mode.equals("born")) {
            selectedDistrict = _person.bornDistrict;
            questionTextId = R.string.interview_village_born;
        }

        TextView whichVillageTextView = (TextView) findViewById(R.id.whichVillageTextView);

        String whichVillage =
                getResources()
                        .getString(questionTextId)
                        .replace("##district##", selectedDistrict );

        whichVillageTextView.setText(whichVillage);

        populateVillages();
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
            if (l.subdistrict.equals(selectedDistrict)) {
                m.add(new Village(this, l.municipality, l.subdistrict, l.village));
            }
        }
        return m;
    }


    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }


    public void nextButtonClick(View view) {

        Intent intent = null;

        if (mode.equals("lives")) {
            _person.livesVillage = selectedVillage;
            intent = new Intent(this, InterviewLivedLifeActivity.class);
        }
        else {
            _person.bornVillage = selectedVillage;
            intent = new Intent(this, RecordingInstructionsActivity.class);
        }

        intent.putExtra("Person", _person);

        startActivity(intent);
    }
}

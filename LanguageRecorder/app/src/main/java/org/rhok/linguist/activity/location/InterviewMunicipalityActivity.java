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
import org.rhok.linguist.activity.interview.InterviewNameActivity;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.entity.Location;
import org.rhok.linguist.code.entity.Municipality;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;

import java.util.ArrayList;


public class InterviewMunicipalityActivity extends BaseInterviewActivity {
    private String selectedMunicipality = null;
    private String mode = null;
    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_municipality);

        Bundle extras = getIntent().getExtras();

        mode = extras.getString("mode");
        _person = (Person) extras.getSerializable(IntentUtil.ARG_PERSON);

        TextView textView = (TextView) findViewById(R.id.municipalityQuestionTextView);

        String question = "";
        if (mode.equals("lives")) {
            question = getResources().getString(R.string.interview_municipality_lives);
        }
        else {
            question = getResources().getString(R.string.interview_municipality_born);
        }

        textView.setText(question);

        populateMunicipalities();
    }


    private void populateMunicipalities() {
        ListView lvMunicipalities = (ListView) findViewById(R.id.municipality_list);
        ArrayList<String> list = new ArrayList<String>();
        for (Municipality mun : municipalities()) {
            if (!list.contains(mun.name)) {
                list.add(mun.name);
            }
        }

        ListViewPopulator.populate(this, R.id.municipality_list, list, true, new AdapterView.OnItemClickListener() {
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

    public void nextButtonClick(android.view.View view) {
        if (selectedMunicipality == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a municipality", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

            if (mode.equals("lives")) {
                _person.livesInMunicipality = selectedMunicipality;
                dbHelper.updatePersonLivesMunicipality(_person.personid, selectedMunicipality);
            } else {
                _person.bornMunicipality = selectedMunicipality;
                dbHelper.updatePersonBornMunicipality(_person.personid, selectedMunicipality);
            }

            Intent intent = new Intent(this, InterviewSubDistrictActivity.class);
            intent.putExtra(IntentUtil.ARG_PERSON, _person);
            intent.putExtra("mode", mode);
            intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                    getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));
            startActivity(intent);
        }
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

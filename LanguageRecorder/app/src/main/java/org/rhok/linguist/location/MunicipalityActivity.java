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
import org.rhok.linguist.code.Municipality;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.interview.BaseInterviewActivity;

import java.util.ArrayList;


public class MunicipalityActivity extends BaseInterviewActivity {
    private String selectedMunicipality = "";
    private String mode = null;
    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        Bundle extras = getIntent().getExtras();

        mode = extras.getString("mode");
        _person = (Person) extras.getSerializable("Person");

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

        if (mode.equals("lives")) {
            _person.livesMunicipality = selectedMunicipality;
        }
        else {
            _person.bornMunicipality = selectedMunicipality;
        }

        Intent intent = new Intent(this, SubDistrictActivity.class);
        intent.putExtra("Person", _person);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

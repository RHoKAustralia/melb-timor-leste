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
import org.rhok.linguist.interview.BaseInterviewActivity;

import java.util.ArrayList;


public class MunicipalityActivity extends BaseInterviewActivity {
    private String selectedMunicipality = "";
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        Intent intent = getIntent();
        from = intent.getExtras().getString("from");

        TextView textView = (TextView) findViewById(R.id.municipalityQuestionTextView);

        String question = "";
        if (from == null) {
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

    private String selectedStudy() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("STUDY");
        }
        return value;
    }

    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, SubDistrictActivity.class);
        intent.putExtra("MUNICIPALITY", selectedMunicipality);
        intent.putExtra("from", from);
        startActivity(intent);
    }

    private String[] locations() {
        return getResources().getStringArray(R.array.locations);
    }
}

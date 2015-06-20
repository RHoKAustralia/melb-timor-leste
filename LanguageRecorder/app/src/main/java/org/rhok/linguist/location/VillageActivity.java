package org.rhok.linguist.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Location;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.code.Village;
import org.rhok.linguist.interview.BaseInterviewActivity;
import org.rhok.linguist.interview.InterviewLivedLifeActivity;
import org.rhok.linguist.recording.RecordingInstructionsActivity;

import java.util.ArrayList;

public class VillageActivity extends BaseInterviewActivity {
    private String selectedVillage = "";
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);

        TextView whichVillageTextView = (TextView) findViewById(R.id.whichVillageTextView);
        whichVillageTextView.setText("Select village for " + selectedSubDistrict());

        populateVillages();

        from = getIntent().getExtras().getString("from");

        //setTitle("Interview - Village");
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
            Intent intent = new Intent(this, InterviewLivedLifeActivity.class);
            //TODO fix this!
            intent.putExtra("Person", new Person());
            startActivity(intent);
        } else {

            // we have just come from the second interview section
            Intent intent = new Intent(this, RecordingInstructionsActivity.class);
            startActivity(intent);
        }
    }
}

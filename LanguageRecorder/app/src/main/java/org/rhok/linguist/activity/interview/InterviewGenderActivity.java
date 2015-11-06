package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.entity.Person;

public class InterviewGenderActivity extends BaseInterviewActivity {

    private Person _person;
    private String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_gender);

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra("person");

        populateGenders();
    }

    private void populateGenders() {

        ListViewPopulator.populate(this, R.id.genderListView, R.array.genders, true,
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = (String) parent.getItemAtPosition(position);
            }
        });
    }

    public void nextButtonClick(android.view.View view) {

        if (selectedGender == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a gender", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {

            _person.gender = selectedGender;

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.updatePersonGender(_person.personid, _person.gender );

            Intent intent = new Intent(this, InterviewOccupationActivity.class);
            intent.putExtra("person", _person);
            startActivity(intent);
        }

    }

}

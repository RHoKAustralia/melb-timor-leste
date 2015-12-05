package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.entity.Person;

public class InterviewAgeActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_age);

        EditText editText = (EditText)findViewById(R.id.ageEditText);
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra(IntentUtil.ARG_PERSON);
    }


    public void nextButtonClick(android.view.View view) {

        EditText editText = (EditText)findViewById(R.id.ageEditText);
        String age = editText.getText().toString();

        if (age.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter an age", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            _person.age = Integer.parseInt(age);

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.updatePersonAge(_person.personid, _person.age);

            Intent intent = new Intent(this, InterviewGenderActivity.class);
            intent.putExtra(IntentUtil.ARG_PERSON, _person);
            intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                    getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));

            startActivity(intent);
        }

    }
}

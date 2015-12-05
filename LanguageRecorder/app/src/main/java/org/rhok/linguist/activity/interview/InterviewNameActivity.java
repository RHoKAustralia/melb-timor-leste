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

public class InterviewNameActivity extends BaseInterviewActivity {

    /**
     * Put the intent for where you will go once the Interviewee flow is finished (in InterviewLivedLifeActivity)
     *
     * We will add IntentUtil.ARG_PERSON_ID to it.
     */
    public static final String ARG_FINAL_INTENT = "org.rhok.linguist.activity.interview.finalIntent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview_name);

        EditText editText = (EditText)findViewById(R.id.nameEditText);
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void nextButtonClick(android.view.View view) {

        EditText editText = (EditText)findViewById(R.id.nameEditText);
        String name = editText.getText().toString();

        if (name.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            Person newPerson = new Person(name);

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.insertPerson(newPerson);

            Intent intent = new Intent(this, InterviewAgeActivity.class);
            intent.putExtra(IntentUtil.ARG_PERSON, newPerson);
            intent.putExtra(ARG_FINAL_INTENT, getIntent().getParcelableExtra(ARG_FINAL_INTENT));
            startActivity(intent);
        }
    }
}

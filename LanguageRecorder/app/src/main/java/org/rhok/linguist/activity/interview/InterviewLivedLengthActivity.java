package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.location.InterviewMunicipalityActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.entity.Person;

public class InterviewLivedLengthActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_length);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable(IntentUtil.ARG_PERSON);

        String question = getResources()
                .getString(R.string.interview_lived_length)
                .replace("##village##", _person.livesInVillage);

        TextView livedLifeTextView = (TextView) findViewById(R.id.livedLengthQuestion);
        livedLifeTextView.setText(question);

        EditText editText = (EditText)findViewById(R.id.longLivedEditText);
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    public void nextButtonClick(View view) {
        EditText editText = (EditText)findViewById(R.id.longLivedEditText);
        String longLived = editText.getText().toString();

        if (longLived.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter number of years", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            _person.livedInYears = Integer.parseInt(longLived);

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.updatePersonLivedYears(_person.personid, _person.livedInYears);

            Intent intent = new Intent(this, InterviewMunicipalityActivity.class);
            intent.putExtra(IntentUtil.ARG_PERSON, _person);
            intent.putExtra("mode", "born");
            intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                    getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));
            startActivity(intent);
        }
    }

}

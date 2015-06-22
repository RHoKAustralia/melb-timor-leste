package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.rhok.linguist.activity.location.InterviewMunicipalityActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.entity.Person;

public class InterviewLivedLengthActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_length);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");

        String question = getResources()
                .getString(R.string.interview_lived_length)
                .replace("##village##", _person.livesVillage);

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
            _person.livesinyears = Integer.parseInt(longLived);

            Intent intent = new Intent(this, InterviewMunicipalityActivity.class);
            intent.putExtra("Person", _person);
            intent.putExtra("mode", "born");
            startActivity(intent);
        }
    }

}

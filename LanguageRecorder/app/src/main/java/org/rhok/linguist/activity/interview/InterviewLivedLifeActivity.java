package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.activity.recording.RecordingInstructionsActivity;

public class InterviewLivedLifeActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_life);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");

        String question =
                getResources()
                        .getString(R.string.interview_lived_life)
                        .replace("##village##", _person.livesInVillage);

        TextView livedLifeTextView = (TextView) findViewById(R.id.interview_lived_life);
        livedLifeTextView.setText(question);
    }

    public void noButtonClick(View view) {

        Intent intent = new Intent(this, InterviewLivedLengthActivity.class);
        intent.putExtra("Person", _person);
        startActivity(intent);
    }

    public void yesButtonClick(View view) {
        _person.livesInYears = null;

        Intent intent = new Intent(this, RecordingInstructionsActivity.class);
        intent.putExtra("Person", _person);
        intent.putExtra("mode", "born");
        startActivity(intent);
    }
}

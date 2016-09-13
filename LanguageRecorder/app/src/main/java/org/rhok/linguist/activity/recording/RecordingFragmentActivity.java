package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.common.SplashActivity;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Recording;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.util.StringUtils;

import java.util.Date;

/**
 * Record audio/text for Record objects, using fragments and remote images
 */
public class RecordingFragmentActivity extends BaseInterviewActivity {

    private static final String TAG = "RecFragActivity";

    public static final String ARG_PHRASE_INDEX = "rhok.phraseIndex";


    private Study study;
    private Interview interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_empty);
        Bundle restoreState = savedInstanceState!=null?savedInstanceState:getIntent().getExtras();
        study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
        interview = (Interview) restoreState.getSerializable(IntentUtil.ARG_INTERVIEW);
        if (getSupportFragmentManager().findFragmentById(getMainFragmentContainerId()) == null) {
            navigateNextPhrase(0);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(IntentUtil.ARG_INTERVIEW, interview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public Study getStudy() {
        return study;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_interview_recording, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_skip_question:
                int phraseIndex = getCurrentPhraseIndex();
                if(phraseIndex>=0) {
                    Recording recording = interview.getRecordings().get(phraseIndex);
                    if(StringUtils.isNullOrEmpty(recording.getText_response()))
                        recording.setText_response(Recording.SKIPPED_TEXT_RESPONSE);
                    recording.setRecorded(new Date());
                    navigateNextPhrase(phraseIndex + 1);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private int getCurrentPhraseIndex(){
        Fragment f = getMainFragment();
        if(f!=null&&f.getArguments()!=null) return f.getArguments().getInt(ARG_PHRASE_INDEX, -1);
        return -1;
    }

    private void finishInterview() {
        interview.set__completed(true);
        DatabaseHelper db = new DatabaseHelper(this);
        db.insertUpdateInterview(interview);
        Toast.makeText(this, "Interview completed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void navigateNextPhrase(int phraseIndex) {
        if (phraseIndex >= getStudy().getPhrases().size()) {
            finishInterview();
        } else {
            Bundle args = new Bundle();
            args.putInt(ARG_PHRASE_INDEX, phraseIndex);
            Phrase phrase = getStudy().getPhrases().get(phraseIndex);
            if(!StringUtils.isNullOrEmpty(phrase.getAudio())){
                Fragment fragment = new AudioPlaybackFragment();
                fragment.setArguments(args);
                transactTo(getMainFragmentContainerId(), fragment, AudioPlaybackFragment.TAG + "_" + phraseIndex);
            }
            else if (phrase.getResponse_type() == Phrase.TYPE_AUDIO || phrase.getResponse_type() == Phrase.TYPE_TEXT_AUDIO) {
                Fragment fragment = new RecordingAudioFragment();
                fragment.setArguments(args);
                transactTo(getMainFragmentContainerId(), fragment, RecordingAudioFragment.TAG + "_" + phraseIndex);
            } else {
                Fragment fragment = new RecordingTextFragment();
                fragment.setArguments(args);
                transactTo(getMainFragmentContainerId(), fragment, RecordingTextFragment.TAG + "_" + phraseIndex);
            }
        }
    }

    public void onRecordingAudioFinished(int phraseIndex, String audioFilename) {
        Recording recording = interview.getRecordings().get(phraseIndex);
        recording.set__audio_filename(audioFilename);
        recording.setRecorded(new Date());

        if (study.getPhrases().get(phraseIndex).getResponse_type() == Phrase.TYPE_TEXT_AUDIO) {
            //requires text as well as audio, go to the text fragment
            Bundle args = new Bundle();
            args.putInt(ARG_PHRASE_INDEX, phraseIndex);
            Fragment fragment = new RecordingTextFragment();
            fragment.setArguments(args);
            transactTo(getMainFragmentContainerId(), fragment, RecordingTextFragment.TAG + "_" + phraseIndex);
        } else {
            navigateNextPhrase(phraseIndex + 1);
        }
    }

    public void onRecordingTextFinished(int phraseIndex, String answer) {
        int interviewId = interview.get__appid();
        Recording recording = interview.getRecordings().get(phraseIndex);
        recording.setText_response(answer);
        recording.setRecorded(new Date());
        navigateNextPhrase(phraseIndex + 1);
    }

    /**
     * called by audioPlaybackFragment once the user has heard the question.
     * Go to RecordingAudioFragment or RecordingTextFragment
     *
     * @param phraseIndex
     */
    public void onAudioQuestionFinished(int phraseIndex) {

        Bundle args = new Bundle();
        args.putInt(ARG_PHRASE_INDEX, phraseIndex);
        Phrase phrase = getStudy().getPhrases().get(phraseIndex);
        if (phrase.getResponse_type() == Phrase.TYPE_AUDIO || phrase.getResponse_type() == Phrase.TYPE_TEXT_AUDIO) {
            Fragment fragment = new RecordingAudioFragment();
            fragment.setArguments(args);
            transactTo(getMainFragmentContainerId(), fragment, RecordingAudioFragment.TAG + "_" + phraseIndex);
        } else {
            Fragment fragment = new RecordingTextFragment();
            fragment.setArguments(args);
            transactTo(getMainFragmentContainerId(), fragment, RecordingTextFragment.TAG + "_" + phraseIndex);
        }
    }
}

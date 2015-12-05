package org.rhok.linguist.activity.recording;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.common.SplashActivity;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.util.StringUtils;

/**
 * Record audio/text for Record objects, using fragments and remote images
 */
public class RecordingFragmentActivity extends BaseInterviewActivity {

    public static final String ARG_PHRASE_INDEX = "rhok.phraseIndex";


    private Study study;
    private Interview interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
        interview = (Interview) getIntent().getSerializableExtra(IntentUtil.ARG_INTERVIEW);
        if (getSupportFragmentManager().findFragmentById(getMainFragmentContainerId()) == null) {
            navigateNextPhrase(0);
        }

    }

    public Study getStudy() {
        return study;
    }

    private void finishInterview() {
        interview.set__completed(true);
        //TODO save to db
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
            if(!StringUtils.isNullOrEmpty(phrase.getAudio_url())){
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
        DatabaseHelper helper = new DatabaseHelper(this);
        int interviewId = interview.get__appid();
        helper.insertUpdateRecording(interviewId, study.getPhrases().get(phraseIndex).getId(), null, audioFilename);
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
        DatabaseHelper helper = new DatabaseHelper(this);
        int interviewId = interview.get__appid();
        helper.insertUpdateRecording(interviewId, getStudy().getPhrases().get(phraseIndex).getId(), answer, null);
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
package org.rhok.linguist.activity.recording;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.application.LinguistApplication;
import org.rhok.linguist.util.StringUtils;

import java.io.File;

/**
 * Play an audio question
 */
public class AudioPlaybackFragment extends Fragment {

    public static final String TAG = "AudioPlaybackFragment";
    TextView recordingQuestionTextView;
    TextView recordingMessageTextView;
    TextView recordReplayTextView;
    Button yesButton;
    Button noButton;
    Animation anim;
    ImageView imageView;
    ProgressBar progressBar;
    private AQuery aq;


    private int phraseIndex;
    private boolean transcribing = false;
    private boolean playing = false;

    private AudioThread audioThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phraseIndex = getArguments().getInt(RecordingFragmentActivity.ARG_PHRASE_INDEX);
        startAudioThreadIfNull();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_audio_playback, container, false);
        aq=new AQuery(root);

        recordingQuestionTextView = (TextView) root.findViewById(R.id.recordingQuestionTextView);
        recordingMessageTextView  = (TextView) root.findViewById(R.id.recordingMessageTextView);
        recordReplayTextView = (TextView) root.findViewById(R.id.recordReplayTextView);
        progressBar = (ProgressBar) root.findViewById(R.id.progress);

        yesButton = aq.id(R.id.yesButton).clicked(this, "yesButtonClick").getButton();
        noButton = aq.id(R.id.noButton).clicked(this, "noButtonClick").getButton();

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        recordingMessageTextView.startAnimation(anim);

        Phrase phrase =getStudy().getPhrases().get(phraseIndex);
        String question = StringUtils.isNullOrEmpty(phrase.getEnglish_text(), getString(R.string.interview_audio_recording));
        recordingQuestionTextView.setText(question);

        startAudioThreadIfNull();

        return root;
    }

    private void startAudioThreadIfNull() {
        if (audioThread == null) {
            audioThread = new AudioThread();
            audioThread.start();
        }
    }

    private void releaseAudioThread() {
        if (audioThread != null) {
            audioThread.release();
            audioThread = null;
        }
    }


    private void startPlaying(File file)
    {
        Log.d(TAG, "startPlaying(): " + file.getAbsolutePath());
        audioThread.playFile(file.getAbsolutePath());
        playing = true;
    }
    private void stopPlaying()
    {
        audioThread.stopPlaying();
        playing = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playing) {
            stopPlaying();
            playing = false;
        }
        releaseAudioThread();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    public void onResume() {
        super.onResume();
        startAudioThreadIfNull();
        if (!playing) {
            loadAudioFile(getStudy().getPhrases().get(phraseIndex));
            playing = true;
        }
        // The activity has become visible (it is now "resumed").
    }

    private void loadAudioFile(Phrase phrase){
        onPlaybackStateChanged(STATE_LOADING);
        File dir = new File(getActivity().getFilesDir().getPath(), LinguistApplication.DIR_INTERVIEW_MEDIA);
        File audioFile = new File(dir, String.format("%d_audio.m4a", phrase.getId()));
        if(audioFile.exists() && audioFile.length()>0){
            startPlaying(audioFile);
        }
        else {
            aq.download(phrase.getAudio(), audioFile, new AjaxCallback<File>(){
                @Override
                public void callback(String url, File file, AjaxStatus status) {
                    if(file!=null && file.exists() && file.length()>0 ){
                        startPlaying(file);
                    }
                }
            });
        }
    }

    public Study getStudy(){
        return ((RecordingFragmentActivity)getActivity()).getStudy();
    }



    public void noButtonClick(View view) {

    //all done
        stopPlaying();
        ((RecordingFragmentActivity)getActivity()).onAudioQuestionFinished(phraseIndex);

    }

    public void yesButtonClick(View view) {
//TODO replay
        stopPlaying();

        startPlaying(null);
    }

    public static final int STATE_LOADING =0;
    public static final int STATE_PLAYING =1;
    public static final int STATE_FINISHED =2;

    public void onPlaybackStateChanged(int state){
        switch (state) {
            case STATE_LOADING:
                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.startAnimation(anim);
                recordingMessageTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                recordReplayTextView.setVisibility(View.GONE);

                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                break;
            case STATE_PLAYING:
            recordingQuestionTextView.setVisibility(View.VISIBLE);
            recordingMessageTextView.startAnimation(anim);
            recordingMessageTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            recordReplayTextView.setVisibility(View.GONE);

            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
                break;
            case STATE_FINISHED:
                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.clearAnimation();
                recordingMessageTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                recordReplayTextView.setVisibility(View.VISIBLE);

                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                break;
        }
    }
}

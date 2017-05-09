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
import android.widget.TextView;

import com.androidquery.AQuery;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.util.Reflect;
import org.rhok.linguist.util.StringUtils;

import java.io.File;
import java.util.UUID;

public class RecordingAudioFragment extends Fragment {

    public static final String TAG = "RecAudioFragment";
    TextView recordingQuestionTextView;
    TextView recordingMessageTextView;
    TextView recordOkTextView;
    Button nextButton;
    Button yesButton;
    Button noButton;
    Animation anim;
    ImageView imageView;
    private AQuery aq;

    private int interviewId;
    private int phraseIndex;
    private boolean transcribing = false;
    private boolean playing = false;

    private AudioThread audioThread;
    private String mAudioFilename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        phraseIndex = getArguments().getInt(RecordingFragmentActivity.ARG_PHRASE_INDEX);
        if(savedInstanceState!=null){
            mAudioFilename=savedInstanceState.getString("mAudioFilename");
        }
        if(mAudioFilename==null){
            mAudioFilename= UUID.randomUUID().toString().replaceAll("-", "").concat(".mp4");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mAudioFilename", mAudioFilename);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recording_audio, container, false);
        aq=new AQuery(root);
        imageView = (ImageView)root.findViewById(R.id.captureImageView);

        recordingQuestionTextView = (TextView) root.findViewById(R.id.recordingQuestionTextView);
        recordingMessageTextView  = (TextView) root.findViewById(R.id.recordingMessageTextView);
        recordOkTextView = (TextView) root.findViewById(R.id.recordOkTextView);

        nextButton = aq.id(R.id.nextRecordingButton).clicked(this, "nextButtonClick").getButton();
        yesButton = aq.id(R.id.yesButton).clicked(this, "yesButtonClick").getButton();
        noButton = aq.id(R.id.noButton).clicked(this, "noButtonClick").getButton();

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        Phrase phrase = getStudy().getPhrases().get(phraseIndex);
        String promptText = ResponseFragmentUtils.getPromptText(this.getActivity(), phrase);
        recordingQuestionTextView.setText(promptText);

        ResponseFragmentUtils.showImagePrompt(imageView, phrase);

       // startAudioThreadIfNull();
       // startRecording();

        return root;
    }

    private void startAudioThreadIfNull() {
        if (audioThread == null) {
            audioThread = AudioThread.getInstance();
        }
    }

    private void releaseAudioThread() {
        if (audioThread != null) {
            audioThread.release();
            audioThread = null;
        }
    }

    private String getAudioFilenameWithPath(){
        return DiskSpace.getInterviewRecording(mAudioFilename).getAbsolutePath();
    }
    private void startRecording()
    {
        audioThread.startRecording(getAudioFilenameWithPath());
        onPlaybackStateChanged(STATE_RECORDING);
    }
    private void stopRecording()
    {
        audioThread.stopRecording();
    }
    private void startPlaying()
    {
        audioThread.playFile(getAudioFilenameWithPath(), true);
        onPlaybackStateChanged(STATE_PLAYING);
        playing = true;
    }
    private void stopPlaying()
    {
        audioThread.stopPlaying();
        playing = false;
    }

    private boolean playingIsPaused = false;

    @Override
    public void onPause() {
        super.onPause();
        if (playing) {
            stopPlaying();
            playingIsPaused = true;
        }
        releaseAudioThread();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    public void onResume() {
        super.onResume();
        onPlaybackStateChanged(STATE_LOADING);
        //noinspection ConstantConditions
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //init audio thread 150ms after fragment resumes, instead of in onCreate.
                //allows time for any previous fragment's threads to be disposed first.
                startAudioThreadIfNull();
                File file = new File(getAudioFilenameWithPath());
                if (playingIsPaused || file.exists()) {
                    startPlaying();
                    playingIsPaused = false;
                } else {
                    startRecording();
                }

            }
        }, 150L);
    }

    public Study getStudy(){
        return ((RecordingFragmentActivity)getActivity()).getStudy();
    }

    public void nextButtonClick(View view) {

            stopRecording();
            startPlaying();

    }

    public void noButtonClick(View view) {

        stopPlaying();
        startRecording();
    }

    public void yesButtonClick(View view) {

        stopPlaying();

        transcribing = true;
        ((RecordingFragmentActivity)getActivity()).onRecordingAudioFinished(phraseIndex, mAudioFilename);
    }
    private static final int STATE_LOADING =0;
    private static final int STATE_RECORDING =1;
    private static final int STATE_PLAYING =2;
    private void onPlaybackStateChanged(int state){
        switch (state){
            case STATE_LOADING:
            case STATE_RECORDING:
                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.startAnimation(anim);
                recordingMessageTextView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                recordOkTextView.setVisibility(View.GONE);

                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);

                if(state==STATE_RECORDING){
                    recordingMessageTextView.startAnimation(anim);
                }
                nextButton.setEnabled(state==STATE_RECORDING);
                break;
            case STATE_PLAYING:
                recordingQuestionTextView.setVisibility(View.GONE);
                recordingMessageTextView.clearAnimation();
                recordingMessageTextView.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);

                recordOkTextView.setVisibility(View.VISIBLE);

                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                break;
        }
    }
}

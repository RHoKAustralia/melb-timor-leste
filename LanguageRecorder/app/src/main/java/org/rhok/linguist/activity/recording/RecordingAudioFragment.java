package org.rhok.linguist.activity.recording;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import org.rhok.linguist.util.Reflect;
import org.rhok.linguist.util.StringUtils;

public class RecordingAudioFragment extends Fragment {

    public static final String TAG = "RecordingAudioFragment";
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phraseIndex = getArguments().getInt(RecordingFragmentActivity.ARG_PHRASE_INDEX);
        audioThread=new AudioThread();
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
        recordingMessageTextView.startAnimation(anim);

        audioThread = new AudioThread();
        audioThread.start();

        Phrase phrase =getStudy().getPhrases().get(phraseIndex);
        String question = StringUtils.isNullOrEmpty(phrase.getEnglish_text(), getString(R.string.interview_audio_recording));
        recordingQuestionTextView.setText(question);
        if(StringUtils.isNullOrEmpty(phrase.getImage())){
            aq.id(imageView).gone();
        }
        else if (phrase.formatImageUrl().startsWith("http")){
            aq.id(imageView).image(phrase.formatImageUrl());
        }
        else{
            //in case it refers to a built-in image, eg "word4"
            int resId = Reflect.getImageResId(phrase.getImage());
            aq.id(imageView).image(resId);
        }

        return root;
    }


    private Message createMessage(String text) {
        Message msg = Message.obtain();
        msg.obj = text;
        return msg;
    }


    private void startRecording()
    {
        audioThread.mHandler.sendMessage(createMessage("startrecording"));
    }
    private void stopRecording()
    {
        audioThread.mHandler.sendMessage(createMessage("stoprecording"));
        playing = true;
    }
    private void startPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("startplaying"));
    }
    private void stopPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("stopplaying"));
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
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    public void onResume() {
        super.onResume();
        if (playingIsPaused) {
            startPlaying();
            playingIsPaused = false;
        }
        // The activity has become visible (it is now "resumed").
    }

    public Study getStudy(){
        return ((RecordingFragmentActivity)getActivity()).getStudy();
    }

    public void nextButtonClick(View view) {

        if (!transcribing) {

            stopRecording();
            //startPlaying();

            recordingQuestionTextView.setVisibility(View.GONE);
            recordingMessageTextView.clearAnimation();
            recordingMessageTextView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);

            recordOkTextView.setVisibility(View.VISIBLE);

            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);

        }

    }

    public void noButtonClick(View view) {
        recordingQuestionTextView.setVisibility(View.VISIBLE);
        recordingMessageTextView.startAnimation(anim);
        recordingMessageTextView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        recordOkTextView.setVisibility(View.GONE);

        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        stopPlaying();
        startRecording();
    }

    public void yesButtonClick(View view) {

        stopPlaying();

        recordOkTextView.setVisibility(View.GONE);

        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        nextButton.setVisibility(View.VISIBLE);



        transcribing = true;
        ((RecordingFragmentActivity)getActivity()).onRecordingAudioFinished(phraseIndex,  audioThread.audioFilename);
    }
}

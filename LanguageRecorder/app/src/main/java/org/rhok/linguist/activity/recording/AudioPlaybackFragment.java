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
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.util.StringUtils;

import java.io.File;

/**
 * Handles phrases/questions that contain audio prompts.
 */
public class AudioPlaybackFragment extends Fragment {

    public static final String TAG = "AudioPlaybackFragment";
    public static final int STATE_LOADING = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_ERROR = 3;

    // UI elements
    TextView recordingQuestionTextView;
    TextView recordingMessageTextView;
    TextView recordReplayTextView;
    ImageView imageView;
    Button yesButton;
    Button noButton;
    Animation anim;
    ProgressBar progressBar;

    private AQuery aq;
    private AudioThread audioThread;
    private int mPhraseIndex;
    private Phrase mPhrase;
    private boolean playing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAudioThreadIfNull();
        mPhraseIndex = getArguments().getInt(RecordingFragmentActivity.ARG_PHRASE_INDEX);
        mPhrase = getStudy().getPhrases().get(mPhraseIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio_playback, container, false);
        aq = new AQuery(root);
        imageView = (ImageView)root.findViewById(R.id.captureImageView);

        recordingQuestionTextView = (TextView) root.findViewById(R.id.recordingQuestionTextView);
        recordingMessageTextView = (TextView) root.findViewById(R.id.recordingMessageTextView);
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

        String question = StringUtils.isNullOrEmpty(mPhrase.getEnglish_text(), getString(R.string.interview_audio_recording));
        recordingQuestionTextView.setText(question);

        ResponseFragmentUtils.showImagePrompt(imageView, mPhrase);

        startAudioThreadIfNull();

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

    /**
     * Play the phrase's audio prompt
     */
    private void playPhraseAudio() {
        stopPlaying();
        File file = getPhraseAudioFile();
        Log.d(TAG, "playPhraseAudio: " + file.getAbsolutePath());
        audioThread.setPlaybackCompletionListener(new AudioThread.PlaybackCompletionListener() {
            @Override
            public void onPlaybackComplete() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPlaybackStateChanged(STATE_FINISHED);
                    }
                });
            }

            @Override
            public void onPlaybackError(Exception e) {
                onPlaybackStateChanged(STATE_ERROR);
            }
        });
        audioThread.playFile(file.getAbsolutePath());
        onPlaybackStateChanged(STATE_PLAYING);
        playing = true;
    }

    private void stopPlaying() {
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
            loadPhraseAudio(new Runnable() {
                @Override
                public void run() {
                    playPhraseAudio();
                }
            });
        }
    }

    /**
     * Get the audio file associated with this phrase
     */
    private File getPhraseAudioFile() {
        return DiskSpace.getPhraseAudio(mPhrase);
    }

    /**
     * Load audio prompt from file / web
     *
     * @param onLoadComplete Action to perform once loading is complete.
     */
    private void loadPhraseAudio(final Runnable onLoadComplete) {
        onPlaybackStateChanged(STATE_LOADING);
        File audioFile = getPhraseAudioFile();
        if (audioFile.exists() && audioFile.length() > 0) {
            onLoadComplete.run();
        } else {
            aq.download(mPhrase.formatAudioUrl(), audioFile, new AjaxCallback<File>() {
                @Override
                public void callback(String url, File file, AjaxStatus status) {
                    if (file != null && file.exists() && file.length() > 0) {
                        onLoadComplete.run();
                    }
                    else{
                        onPlaybackStateChanged(STATE_ERROR);
                    }
                }
            });
        }
    }

    public Study getStudy() {
        return ((RecordingFragmentActivity) getActivity()).getStudy();
    }

    @SuppressWarnings("unused")
    public void noButtonClick(View view) {
        stopPlaying();
        ((RecordingFragmentActivity) getActivity()).onAudioQuestionFinished(mPhraseIndex);
    }

    @SuppressWarnings("unused")
    public void yesButtonClick(View view) {
        playPhraseAudio();
    }

    /**
     * Update UI based on given state.
     */
    private void onPlaybackStateChanged(int state) {
        switch (state) {
            case STATE_LOADING:
                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.setVisibility(View.GONE);
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
                recordReplayTextView.setText(R.string.interview_audio_replay);

                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR: //error downloading/playing file. Still let them continue with other Qs.
                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.clearAnimation();
                recordingMessageTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                recordReplayTextView.setVisibility(View.VISIBLE);
                recordReplayTextView.setText(R.string.interview_audio_playback_error);

                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);

                break;
        }
    }
}

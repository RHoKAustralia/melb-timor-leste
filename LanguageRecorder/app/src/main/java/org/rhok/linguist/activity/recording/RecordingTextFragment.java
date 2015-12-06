package org.rhok.linguist.activity.recording;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.util.Reflect;
import org.rhok.linguist.util.StringUtils;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class RecordingTextFragment extends Fragment {

    public static final String TAG = "RecordingTextFragment";
    private AQuery aq;
    private int phraseIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phraseIndex=getArguments().getInt(RecordingFragmentActivity.ARG_PHRASE_INDEX);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recording_text, container, false);
        aq=new AQuery(root);
        aq.id(R.id.nextRecordingButton).clicked(this, "nextButtonClick");
        Study study = getStudy();
        Phrase phrase = study.getPhrases().get(phraseIndex);
        int responseType = phrase.getResponse_type();
        //will be either TYPE_TEXT_AUDIO or TEXT. Audio only won't come to this fragment.
        aq.id(R.id.recordingQuestionTextView).text(responseType == Phrase.TYPE_TEXT_AUDIO ? getString(R.string.interview_transcribe) : phrase.getEnglish_text());
        if(StringUtils.isNullOrEmpty(phrase.getImage())){
            aq.id(R.id.captureImageView).gone();
        }
        else if (phrase.formatImageUrl().toLowerCase().startsWith("http")){
            aq.id(R.id.captureImageView).image(phrase.formatImageUrl());
        }
        else{
            //in case it refers to a built-in image, eg "word4"
            int resId = Reflect.getImageResId(phrase.getImage());
            aq.id(R.id.captureImageView).image(resId);
        }
        if(StringUtils.isNullOrEmpty(phrase.getChoices())){
            aq.id(R.id.answerEditText).visible();
            aq.id(R.id.answersListView).gone();
        }
        else{
            aq.id(R.id.answerEditText).gone();
            aq.id(R.id.answersListView).visible()
                    .adapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, phrase.getChoices()))
                    .itemClicked(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mSelection= (String) parent.getItemAtPosition(position);
                            //parent.setSelection(position);
                        }
                    });
        }
        return root;
    }
    private String mSelection;
    public Study getStudy(){
        return ((RecordingFragmentActivity)getActivity()).getStudy();
    }
    public void nextButtonClick(View v){
        Study study = getStudy();
        Phrase phrase = study.getPhrases().get(phraseIndex);
        String answer;
        if(StringUtils.isNullOrEmpty(phrase.getChoices())){
            answer=aq.id(R.id.answerEditText).getText().toString();
        }
        else{
            answer= mSelection;
        }
        if(!StringUtils.isNullOrEmpty(answer)){
            ((RecordingFragmentActivity)getActivity()).onRecordingTextFinished(phraseIndex, answer);
        }
    }
}

package org.rhok.linguist.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.koushikdutta.ion.Response;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.interview.InterviewNameActivity;
import org.rhok.linguist.activity.recording.InterviewResponseLanguageActivity;
import org.rhok.linguist.api.OfflineStorageHelper;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.code.StudyDownloader;
import org.rhok.linguist.network.BaseIonCallback;
import org.rhok.linguist.network.IonHelper;
import org.rhok.linguist.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class StudyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Study> mStudies;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        listView= (ListView) findViewById(R.id.study_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        mProgressBar= (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mStudies==null){
            final OfflineStorageHelper helper = new OfflineStorageHelper(this);
            //mStudies=helper.getStudyListFromAssets(R.raw.test_study_list);
            //STOPSHIP quick hack to grab all /phrases.json from API for demo
            IonHelper ionHelper = new IonHelper();
            ionHelper.doGet(ionHelper.getIon().build(this), StudyList.class, "/studies.json")
                    .go()
                    .setCallback(new BaseIonCallback<StudyList>() {
                        @Override
                        public void onSuccess(StudyList result) {
                            /*for(Phrase phrase:result){
                                //response_type was missing
                                if(phrase.getResponse_type()==0)phrase.setResponse_type(Phrase.TYPE_TEXT);
                                phrase.setAudio(null);
                            }
                            Study apiStudy = new Study();
                            apiStudy.setName("API Study");
                            apiStudy.setId(-1);
                            apiStudy.setInstructions("Demo from API");
                            apiStudy.setPhrases(result);
                            mStudies.add(mStudies.size(), apiStudy);*/
                            mStudies=result;
                            helper.writeStudyList(result);
                            updateListView();
                        }

                        @Override
                        public void onError(Response<StudyList> response) {
                            //error loading from network, load from disk if avail
                            mStudies = helper.getSavedStudyList();
                            updateListView();
                            super.onError(response);
                        }
                    });

        }
        updateListView();
    }
    public static class StudyList extends ArrayList<Study>{

    }

    private void updateListView(){
        if(listView.getAdapter()==null){
            listView.setAdapter(new StudyAdapter(mStudies));
        }
        else ((StudyAdapter)listView.getAdapter()).updateData(mStudies);
        mProgressBar.setVisibility(mStudies==null?View.VISIBLE:View.GONE);
    }


    static final int REQUEST_YES_NO = 101;
    public void nextButtonClick(View view){
        Study study = mSelection;
        if(study!=null){
            Intent yesNo = YesNoActivity.makeYesNoIntent(getString(R.string.person_is_new_question), null);
            Bundle nextActivityArgs = new Bundle();
            nextActivityArgs.putSerializable(IntentUtil.ARG_STUDY, study);
            yesNo.putExtra(IntentUtil.ARG_NEXT_ACTIVITY_ARGS, nextActivityArgs);
            startActivityForResult(yesNo, REQUEST_YES_NO);
        }
    }

    public void downloadAllButtonClick(View view) {
        Log.d("StudyList", "download all");
        if (mSelection != null) {
            StudyDownloader downloader = new StudyDownloader(this);
            downloader.download(mSelection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_YES_NO && resultCode!=RESULT_CANCELED){
            Intent intent = null;
            Intent recordingStartIntent = new Intent(this, InterviewResponseLanguageActivity.class);
            Study study = (Study) data.getBundleExtra(IntentUtil.ARG_NEXT_ACTIVITY_ARGS).getSerializable(IntentUtil.ARG_STUDY);
            recordingStartIntent.putExtra(IntentUtil.ARG_STUDY, study);
            if(resultCode==YesNoActivity.RESULT_YES){
                intent=new Intent(this, PersonListActivity.class);
                intent.putExtra(IntentUtil.ARG_NEXT_INTENT, recordingStartIntent);
            }
            else if (resultCode==YesNoActivity.RESULT_NO){
                intent=new Intent(this, InterviewNameActivity.class);
                intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT, recordingStartIntent);
            }
            if(intent!=null){
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class StudyAdapter extends BaseAdapter {
        private List<Study> items;
        public StudyAdapter(List<Study> items) {
            updateData(items);
        }
        private void updateData(List<Study> items){
            if(items==null) items = new ArrayList<>();
            this.items= items;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView==null){
               convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_simple_2_line, parent, false);
           }
            Study study = getItem(position);
            UIUtil.doHeadingText(convertView, study.getName(), getString(R.string.study_question_count_format, study.getPhrases().size()));
            return convertView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Study getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
            //return getItem(position).getId();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelection= (Study) parent.getItemAtPosition(position);
        //parent.setSelection(position);
        //((StudyAdapter)parent.getAdapter()).notifyDataSetChanged();
    }
    private Study mSelection;
}

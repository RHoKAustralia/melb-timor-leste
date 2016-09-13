package org.rhok.linguist.activity.recording;

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

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class InterviewResponseLanguageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "RecInstructActivity";

    private ListView listView;
    private List<String> mLanguages;
    private String mSelectedLanguage;
    private int personId;
    private Interview interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_response_language);
        listView = (ListView) findViewById(R.id.language_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);

        personId = getIntent().getIntExtra(IntentUtil.ARG_PERSON_ID, -1);
        Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
        setTitle(study.getName());
        interview = new Interview(study);
        interview.set__intervieweeid(personId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLanguages == null) {
            mLanguages = getIntervieweeLanguages();
        }
        updateListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedLanguage = (String) parent.getItemAtPosition(position);
    }

    public void nextButtonClick(View view) {
        if (mSelectedLanguage != null) {
            interview.setResponse_language(mSelectedLanguage);
            Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
            Intent intent = new Intent(this, RecordingInstructionsActivity.class);
            intent.putExtra(IntentUtil.ARG_INTERVIEW, interview);
            intent.putExtra(IntentUtil.ARG_STUDY, study);
            startActivity(intent);
        }
    }

    private void updateListView(){
        if(listView.getAdapter()==null){
            listView.setAdapter(new StudyAdapter(mLanguages));
        }
        else ((StudyAdapter)listView.getAdapter()).updateData(mLanguages);
    }

    private class StudyAdapter extends BaseAdapter {

        private List<String> items;
        public StudyAdapter(List<String> items) {
            updateData(items);
        }

        private void updateData(List<String> items){
            if(items==null) items = new ArrayList<>();
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_simple_2_line, parent, false);
            }
            String item = getItem(position);
            UIUtil.doHeadingText(convertView, item, null);
            return convertView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private List<String> getIntervieweeLanguages() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Person person = dbHelper.getPerson(personId);
        List<String> languages = new ArrayList<>();
        languages.add(person.firstLanguage);
        languages.add(person.secondLanguage);
        languages.add(person.thirdLanguage);
        languages.add(person.fourthLanguage);
        return languages;
    }
}

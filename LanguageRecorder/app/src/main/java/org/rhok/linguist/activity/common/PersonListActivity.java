package org.rhok.linguist.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.util.UIUtil;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class PersonListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView= (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Person[] people = dbHelper.getPeople();
        PersonAdapter adapter = new PersonAdapter( people);
        listView.setAdapter(adapter);
    }

    public void nextButtonClick(View view){
        Person person = (Person) listView.getSelectedItem();
        if(person!=null){
            Intent intent = getIntent().getParcelableExtra(IntentUtil.ARG_NEXT_INTENT);
            intent.putExtra(IntentUtil.ARG_PERSON, person);
            intent.putExtra(IntentUtil.ARG_PERSON_ID, person.personid);
            startActivity(intent);
            //intent.setClassName(this, getIntent().getStringExtra(IntentUtil.ARG_NEXT_ACTIVITY_CLASS));
        }
    }
    private class PersonAdapter extends BaseAdapter {
        private Person[] items;
        public PersonAdapter( Person[] items) {
            this.items= items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView==null){
               convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_simple_2_line, parent, false);
           }
            Person person = getItem(position);
            UIUtil.doHeadingText(convertView, person.name, person.livesInVillage);
            return convertView;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Person getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).personid;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        parent.setSelection(position);
        ((PersonAdapter)parent.getAdapter()).notifyDataSetChanged();
    }
}

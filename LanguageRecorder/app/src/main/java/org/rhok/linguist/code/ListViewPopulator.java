package org.rhok.linguist.code;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lachlan on 10/06/2015.
 */
public class ListViewPopulator {

    public static void populate(Activity activity, int viewId, int arrayId, boolean sort, AdapterView.OnItemClickListener listener) {

        String[] items = activity.getResources().getStringArray(arrayId);

        populate(activity, viewId, items, sort, listener);
    }

    public static void populate(Activity activity, int viewId, ArrayList<String> list, boolean sort, AdapterView.OnItemClickListener listener) {

        String[] items = new String[list.size()];
        items = list.toArray(items);

        populate(activity, viewId, items, sort, listener);
    }


    public static void populate(Activity activity, int viewId, String[] items, boolean sort, AdapterView.OnItemClickListener listener) {
        ListView lvStudies = (ListView) activity.findViewById(viewId);

        if (sort) {
            Arrays.sort(items);
        }

        ArrayAdapter<String> aaStudies = new ArrayAdapter<String>(
                activity, android.R.layout.simple_list_item_1, items);

        aaStudies.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        lvStudies.setAdapter(aaStudies);
        lvStudies.setOnItemClickListener(listener);
    }
}

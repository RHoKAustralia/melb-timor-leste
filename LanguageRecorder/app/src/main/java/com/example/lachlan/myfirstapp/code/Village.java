package com.example.lachlan.myfirstapp.code;

import android.content.Context;

import com.example.lachlan.myfirstapp.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by craig on 30/05/15.
 */
public class Village {
    public String name;
    public Municipality municipality;
    public SubDistrict subDistrict;
    private Context context;

    public Village(Context current, Municipality _municipality, SubDistrict _subDistrict, String _name) {
        context = current;
        municipality = _municipality;
        subDistrict = _subDistrict;
        name = _name;
    }

    private Set<Location> locations() {
        Set<Location> set = new HashSet<Location>();
        String[] locs = context.getResources().getStringArray(R.array.locations);
        for (String loc : locs) {
            set.add(new Location(loc));
        }
        return set;
    }
}

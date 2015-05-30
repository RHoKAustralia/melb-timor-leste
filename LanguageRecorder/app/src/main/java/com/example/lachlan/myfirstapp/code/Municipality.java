package com.example.lachlan.myfirstapp.code;

import android.content.Context;

import com.example.lachlan.myfirstapp.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by craig on 30/05/15.
 */
public class Municipality {
    public String name;
    private Context context;

    public Municipality(Context current, String _name) {
        context = current;
        name = _name;
    }

    public Set<SubDistrict> subdistricts() {
        Set<SubDistrict> subs = new HashSet<SubDistrict>();
        for (Location loc : locations()) {
            if (loc.municipality.equals(name)) {
                subs.add(new SubDistrict(context, this, loc.subdistrict));
            }
        }
        return subs;
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

package com.example.lachlan.myfirstapp.code;

import android.content.Context;

import com.example.lachlan.myfirstapp.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by craig on 30/05/15.
 */
public class SubDistrict {
    public String name;
    public Municipality municipality;
    private Context context;

    public SubDistrict(Context current, Municipality _municipality, String _name) {
        context = current;
        municipality = _municipality;
        name = _name;
    }

    public Set<Village> villages() {
        Set<Village> vils = new HashSet<Village>();
        for (Location loc : locations()) {
            if ((loc.municipality.equals(municipality.name)) && (loc.subdistrict.equals(name))) {
                vils.add(new Village(context, this.municipality, this, loc.village));
            }
        }
        return vils;
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

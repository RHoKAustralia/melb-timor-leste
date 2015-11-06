package org.rhok.linguist.code.entity;

import android.content.Context;

import org.rhok.linguist.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by craig on 30/05/15.
 */
public class SubDistrict {
    public String name, municipality;
    private Context context;

    public SubDistrict(Context current, String _municipality, String _name) {
        context = current;
        municipality = _municipality;
        name = _name;
    }

    public Set<Village> villages() {
        Set<Village> vils = new HashSet<Village>();
        for (Location loc : locations()) {
            if ((loc.municipality.equals(municipality)) && (loc.subdistrict.equals(name))) {
                vils.add(new Village(context, loc.municipality, loc.subdistrict, loc.village));
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

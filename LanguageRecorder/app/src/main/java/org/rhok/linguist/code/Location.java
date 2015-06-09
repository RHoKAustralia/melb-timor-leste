package org.rhok.linguist.code;

/**
 * Created by craig on 30/05/15.
 */
public class Location {
    public String municipality = "";
    public String subdistrict = "";
    public String village = "";

    public Location() {

    }

    public Location(String _municipality, String _subdistrict, String _village) {
        municipality = _municipality;
        subdistrict = _subdistrict;
        village = _village;
    }

    public Location(String _loc) {
        String[] loc = _loc.split("\\|");
        municipality = loc[0];
        subdistrict = loc[1];
        village = loc[2];
    }

    public String toString() { return village + ", " + subdistrict + ", " + municipality; };
}

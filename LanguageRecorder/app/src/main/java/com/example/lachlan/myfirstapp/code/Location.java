package com.example.lachlan.myfirstapp.code;

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

    public String toString() { return village + ", " + subdistrict + ", " + municipality; };
}

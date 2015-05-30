package com.example.lachlan.myfirstapp.code;

/**
 * Created by craig on 30/05/15.
 */
public class Location {
    public String district = "";
    public String subdistrict = "";
    public String village = "";

    public Location() {

    }

    public Location(String _district, String _subdistrict, String _village) {
        district = _district;
        subdistrict = _subdistrict;
        village = _village;
    }

    public String toString() { return village + ", " + subdistrict + ", " + district; };
}

package org.rhok.linguist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.Person;

public class PersonActivity extends ActionBarActivity {

    public final static String INTENT_PERSONSAVED = "org.rhok.linguist.personsaved";
    public final static String INTENT_PERSONSAVEDID = "org.rhok.linguist.personsavedid";
    public final static String INTENT_PERSONID = "org.rhok.linguist.personid";

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText locationEditText;
    private EditText locationYearsEditText;
    private AutoCompleteTextView firstLanguageAutocomplete;
    private AutoCompleteTextView secondLanguageAutocomplete;
    private AutoCompleteTextView thirdLanguageAutocomplete;
    private AutoCompleteTextView otherLanguagesAutocomplete;
    private Spinner genderSpinner;
    private Spinner educatedToSpinner;
    private TextView mylocationTextView;
    private Button deleteButton;

    private int personId;
    private Boolean editMode = false;
    private Double latitude;
    private Double longitude;

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    String maleText;
    String femaleText;
    String primaryText;
    String secondaryText;
    String universityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        locationYearsEditText = (EditText) findViewById(R.id.locationYearsEditText);
        firstLanguageAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_first_language);
        secondLanguageAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_second_language);
        thirdLanguageAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_third_language);
        otherLanguagesAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_other_languages);
        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        educatedToSpinner = (Spinner) findViewById(R.id.educated_to_spinner);
        mylocationTextView = (TextView) findViewById(R.id.mylocation);
        deleteButton = (Button) findViewById(R.id.delete_button);

        maleText = getResources().getString(R.string.person_gender_male);
        femaleText = getResources().getString(R.string.person_gender_female);
        primaryText = getResources().getString(R.string.person_education_primary);
        secondaryText = getResources().getString(R.string.person_education_secondary);
        universityText = getResources().getString(R.string.person_education_university);

        populateReferenceData();

        populatePersonDetails();

        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void populateReferenceData() {


        String[] genders = {"", maleText, femaleText};
        String[] educatedTo = {"", primaryText, secondaryText, universityText};

        String[] languages = getResources().getStringArray(R.array.person_languages_array);

        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);

        ArrayAdapter<String> educatedToSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, educatedTo);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);

        genderSpinner.setAdapter(genderSpinnerAdapter);
        educatedToSpinner.setAdapter(educatedToSpinnerAdapter);

        firstLanguageAutocomplete.setAdapter(adapter);
        secondLanguageAutocomplete.setAdapter(adapter);
        thirdLanguageAutocomplete.setAdapter(adapter);
        otherLanguagesAutocomplete.setAdapter(adapter);

    }

    private void populatePersonDetails() {
        Intent intent = getIntent();
        personId = intent.getIntExtra(HomeActivity.INTENT_PERSONID, -1);
        String windowTitle = getResources().getString(R.string.person_title);

        if (personId != -1) {
            editMode = true;
            windowTitle = getResources().getString(R.string.person_title_edit);

            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Person person = db.getPerson(personId);
            if (person != null) {
                nameEditText.setText(person.name);

                if (person.age != null) {
                    ageEditText.setText(String.valueOf(person.age));
                }
                if (person.gender.equalsIgnoreCase(maleText)) {
                    genderSpinner.setSelection(1);
                }
                if (person.gender.equalsIgnoreCase(femaleText)) {
                    genderSpinner.setSelection(2);
                }
                locationEditText.setText(person.livesin);

                if (person.livesinyears != null) {
                    locationYearsEditText.setText(String.valueOf(person.livesinyears));
                }
                firstLanguageAutocomplete.setText(person.firstlanguage);
                secondLanguageAutocomplete.setText(person.secondlanguage);
                thirdLanguageAutocomplete.setText(person.thirdlanguage);
                otherLanguagesAutocomplete.setText(person.otherlanguages);
                if (person.education.equalsIgnoreCase(primaryText)) {
                    educatedToSpinner.setSelection(1);
                }
                if (person.education.equalsIgnoreCase(secondaryText)) {
                    educatedToSpinner.setSelection(2);
                }
                if (person.education.equalsIgnoreCase(universityText)) {
                    educatedToSpinner.setSelection(3);
                }
                if (person.latitude != null && person.longitude != null) {

                    String myLocation = getResources().getString(R.string.person_mylocation_label);

                    latitude = person.latitude;
                    longitude = person.longitude;

                    mylocationTextView.setText(myLocation + ": Lat "
                            + person.latitude + ", Long " + person.longitude);

                }
            }
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        this.setTitle(windowTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("editingperson", personId);
    }
*/


    public void deleteButton(android.view.View view) {
        if (editMode) {
            Intent intent = new Intent(this, PersonDeleteActivity.class);
            intent.putExtra(INTENT_PERSONID, personId);
            startActivity(intent);
        }

    }

    public void okButton(android.view.View view) {

        String nameText = nameEditText.getText().toString();

        if (nameText.trim().length() == 0) {
            Context context = getApplicationContext();

            String enterName = getResources().getString(R.string.person_enter_name_toast);

            Toast toast = Toast.makeText(context, enterName, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        savePerson();
    }

    private void savePerson() {
        String ageText = ageEditText.getText().toString();
        String livesInYears = locationYearsEditText.getText().toString();

        Person person = new Person();

        person.personid = personId;
        person.name = nameEditText.getText().toString();

        if (ageText.trim().length() > 0) {
            person.age = Integer.parseInt(ageText);
        }
        if (livesInYears.trim().length() > 0) {
            person.livesinyears = Integer.parseInt(livesInYears);
        }
        person.gender = genderSpinner.getSelectedItem().toString();
        person.livesin = locationEditText.getText().toString();
        person.firstlanguage = firstLanguageAutocomplete.getText().toString();
        person.secondlanguage = secondLanguageAutocomplete.getText().toString();
        person.thirdlanguage = thirdLanguageAutocomplete.getText().toString();
        person.otherlanguages = otherLanguagesAutocomplete.getText().toString();
        person.education = educatedToSpinner.getSelectedItem().toString();
        person.latitude = latitude;
        person.longitude = longitude;

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        String message;

        if (editMode) {
            db.updatePerson(person);
            message = getResources().getString(R.string.person_updated_toast);
        } else {
            db.insertPerson(person);
            message = getResources().getString(R.string.person_added_toast);
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(INTENT_PERSONSAVED, message);
        intent.putExtra(INTENT_PERSONSAVEDID, person.personid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    private void makeUseOfNewLocation(Location location) {

        // only use new location if creating a new person and location exists (dur)

        if (location != null && !editMode) {
            Log.i("LanguageApp", location.toString());
            String myLocation = getResources().getString(R.string.person_mylocation_label);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            mylocationTextView.setText(myLocation + ": Lat "
                    + latitude + ", Long " + longitude);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if (lastKnownLocation != null) {
            Log.i("LanguageApp", lastKnownLocation.toString());
        }
    }
}
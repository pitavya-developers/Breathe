package com.subham.breathe;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import sm.euzee.github.com.servicemanager.ServiceManager;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        dayChangeChooser();
        setupBreakTimeSpinner();
        SetCredentialsFromGoogleSignIn();

    }

    private void SetCredentialsFromGoogleSignIn() {
        config = new Config();
        config.Id = "1";
        config.Name = "Subham";
        config.Email = "subhamkumarchandrawansi@gmail.com";
    }

    // TODO start service from config
    public void activate_breathe(View V) {
        Toast.makeText(this, config.toString(), Toast.LENGTH_SHORT).show();
        ServiceManager.runService(getApplicationContext(), () -> {
            },true);

    }

    public void showTimePicker(final View V) {


        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Time time = new Time(hourOfDay, minute);
                        ((TextView)V).setText(time.toString());

                        if (V.getId() == R.id.home_work_start_time){
                            config.StartTime = time;
                        }
                        else{
                            config.EndTime = new Time(hourOfDay, minute);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void dayChangeChooser() {
        ((MaterialDayPicker) findViewById(R.id.day_picker)).setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(MaterialDayPicker.Weekday weekday, boolean b) {
                config.setWeekDays(weekday, b);
            }
        });
    }

    // frequency spinner
    private void setupBreakTimeSpinner() {
        Spinner breakTimeSpinner = (Spinner) findViewById(R.id.home_break_time_gap);
        (breakTimeSpinner).setOnItemSelectedListener(Home.this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, BreakTime.getDefaultBreakTimes());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breakTimeSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        config.breakTimeInMinutes = new BreakTime(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
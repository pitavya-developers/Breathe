package com.subham.breathe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static Config config;
    ConfigPersistanceStorage configPersistanceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        init();
    }

    private void init() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        configPersistanceStorage = new ConfigPersistanceStorage(this);

        InitializeConfigParameters();

        dayChangeChooser();
        setupBreakTimeSpinner();
        manageAutoPermission();
    }

    private void InitializeConfigParameters() {
        config = new Config();
        // TODO Faiz setup from google sign in
        config.Id = "1";
        config.Name = "Guest";
        config.Email = "guest@xyz";
        // TODO End Faiz setup from google sign in

        config.activated = configPersistanceStorage.getActivated();
        config.WorkDays = new ArrayList<>();

        if (config.activated) {
            config.WorkDays = configPersistanceStorage.getWeekDays();
            config.StartTime = configPersistanceStorage.getStartTime();
            config.EndTime = configPersistanceStorage.getEndTime();
            config.breakTimeInMinutes = configPersistanceStorage.getBreakTime();

        }
        else{
            config.WorkDays.add(MaterialDayPicker.Weekday.MONDAY);
            config.WorkDays.add(MaterialDayPicker.Weekday.TUESDAY);
            config.WorkDays.add(MaterialDayPicker.Weekday.WEDNESDAY);
            config.WorkDays.add(MaterialDayPicker.Weekday.THURSDAY);
            config.WorkDays.add(MaterialDayPicker.Weekday.FRIDAY);

        }

        ((MaterialDayPicker) findViewById(R.id.day_picker)).setSelectedDays(config.WorkDays);
        ((TextView) findViewById(R.id.home_work_start_time)).setText(config.EndTime.toString());
        ((TextView) findViewById(R.id.home_work_start_time)).setText(config.StartTime.toString());
        ((Switch) findViewById(R.id.home_activate_switch)).setChecked(config.activated);
    }


    // Auto Start permission
    private void manageAutoPermission() {
        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                .getInstance().isAutoStartPermissionAvailable(this);

        if (autoStartFeatureAvailable) {
            new AutoStartPermissionDialog().show(getSupportFragmentManager(), "AUTOSTART");
        }
    }

    public static class AutoStartPermissionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_autostart_info)
                    .setPositiveButton("Grant", (dialog, id) -> {

                        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                                .getInstance().isAutoStartPermissionAvailable(getContext());

                        if (autoStartFeatureAvailable) {
                            config.permissionGiven = AutoStartPermissionHelper
                                    .getInstance().getAutoStartPermission(getContext());

                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.dismiss();
                    });
            return builder.create();
        }
    }
    // end of autostart permission


    private static final String TAG = "Home";

    // TODO start service from config
    public void activate_breathe(View V) {

        if (((Switch)V).isChecked()){
            config.activated = true;
            config.firstTimeActivated = true;
            configPersistanceStorage.update(config);
            startBreakService();
        }
        else{
            config.activated = false;
            config.firstTimeActivated = false;
            configPersistanceStorage.update(config);
            stopBreakService();
        }
    }

    private void stopBreakService() {
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(7979);
        Log.d(TAG, "job cancelled");
    }

    private void startBreakService() {
        ComponentName serviceComponent = new ComponentName(this, ScheduleService.class);
        int breakTime = config.breakTimeInMinutes.time * 60 * 1000;
        JobInfo jobInfo
                = new JobInfo.Builder(7979, serviceComponent)
                .setPeriodic(breakTime)
                .setPersisted(true).build();
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "job scheduled");
        }
        else{
            Log.d(TAG, "job scheduled failed");
        }

    }

    public void showTimePicker(final View V) {

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    Time time = new Time(hourOfDay, minute);
                    ((TextView)V).setText(time.toString());

                    if (V.getId() == R.id.home_work_start_time){
                        config.StartTime = time;
                        configPersistanceStorage.update(config);

                    }
                    else{
                        config.EndTime = new Time(hourOfDay, minute);
                        configPersistanceStorage.update(config);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void dayChangeChooser() {
        ((MaterialDayPicker) findViewById(R.id.day_picker))
                .setDayPressedListener((weekday, b) -> {
                    config.setWeekDays(weekday, b);
                    configPersistanceStorage.update(config);
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

        int selectedIndex = dataAdapter.getPosition(config.breakTimeInMinutes.toString());
        breakTimeSpinner.setSelection(selectedIndex);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        config.breakTimeInMinutes = new BreakTime(item);
        configPersistanceStorage.update(config);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
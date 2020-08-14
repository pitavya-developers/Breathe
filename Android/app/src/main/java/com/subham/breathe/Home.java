package com.subham.breathe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.subham.breathe.databinding.HomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Home";
    static Config config;
    ConfigPersistanceStorage configPersistanceStorage;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private HomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        config.Id = configPersistanceStorage.getGId();
        config.Name = configPersistanceStorage.getGName();
        config.Email = configPersistanceStorage.getGEmail();

        config.activated = configPersistanceStorage.getActivated();
        config.WorkDays = new ArrayList<>();

        if (config.activated) {
            config.WorkDays = configPersistanceStorage.getWeekDays();
            config.StartTime = configPersistanceStorage.getStartTime();
            config.EndTime = configPersistanceStorage.getEndTime();
            config.breakTimeInMinutes = configPersistanceStorage.getBreakTime();

        } else {
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
        ((TextView) findViewById(R.id.home_greeting_to)).setText(config.Name);

    }

    // Auto Start permission
    private void manageAutoPermission() {
        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                .getInstance().isAutoStartPermissionAvailable(this);

        if (autoStartFeatureAvailable) {
            new AutoStartPermissionDialog().show(getSupportFragmentManager(), "AUTOSTART");
        }
    }
    // end of autostart permission

    private void toogleService(boolean activate) {

        if (activate) {
            Intent intent = new Intent(getApplicationContext(), SchedulerServiceViaIntentService.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, SchedulerReceiverForAlarmService.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis();
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    60 * 1000, pIntent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SchedulerServiceViaIntentService.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, SchedulerReceiverForAlarmService.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
        }
    }


    // TODO start service from config
    public void activate_breathe(View V) {

        if (((Switch) V).isChecked()) {
            config.activated = true;
            config.firstTimeActivated = true;
            configPersistanceStorage.update(config);
            //startBreakService();
            // setting up alarm service
            setupRequestResponseForInstantVerification();
        } else {
            config.activated = false;
            config.firstTimeActivated = false;
            configPersistanceStorage.update(config);
            //stopBreakService();
            stopRequestResponseForInstantVerification();
        }
    }


    private void setupRequestResponseForInstantVerification() {
        // Setup a PendingIntent that will perform a broadcast
        Log.e("customService", ": ------------------------------------------------------------   :request is being setup");

        Intent alarmIntent = new Intent(this, AlarmServiceReciever.class);
        //alarmIntent.putExtra("anyString", "anyString");

        pendingIntent = PendingIntent.getBroadcast(this, 100, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        startRequestResponseForInstantVerification();
    }

    private void startRequestResponseForInstantVerification() {
        /*
         *  This is method responsible for the following
         * 1.  hit the broadcast service at the interval provided by the user */

        long interval = Long.parseLong(String.valueOf(binding.homeBreakTimeGap.getSelectedItem())) * 60 * 1000;

        // How ever it has been set for 10 sec , but internally it will be of minimum of 60 seconds ad per android ..
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Request Initiated", Toast.LENGTH_SHORT).show();
        Log.e("alarmService : ", "-------------------------------------Started--------------------------------------------");

    }

    private void stopRequestResponseForInstantVerification() {
        /*  This method goes off when user logs out */
        if (manager != null) {
            manager.cancel(pendingIntent);
            //  Toast.makeText(this, "Request Canceled", Toast.LENGTH_SHORT).show();
            Log.e("alarmService : ", "-------------------------------------Stopped--------------------------------------------");
        }
    }

    private void stopBreakService() {
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(7979);
        Log.d(TAG, "job cancelled");
    }

    @SuppressLint("MissingPermission")
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
        } else {
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
                    ((TextView) V).setText(time.toString());

                    if (V.getId() == R.id.home_work_start_time) {
                        config.StartTime = time;
                        configPersistanceStorage.update(config);

                    } else {
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
        Spinner breakTimeSpinner = findViewById(R.id.home_break_time_gap);
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
}
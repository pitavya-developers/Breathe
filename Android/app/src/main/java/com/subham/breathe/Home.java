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

import java.util.Calendar;
import ca.antonious.materialdaypicker.MaterialDayPicker;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        init();
    }

    private void init() {
        dayChangeChooser();
        setupBreakTimeSpinner();
        SetCredentialsFromGoogleSignIn();
        manageAutoPermission();
    }

    private void SetCredentialsFromGoogleSignIn() {
        config = new Config();
        config.Id = "1";
        config.Name = "Subham";
        config.Email = "subhamkumarchandrawansi@gmail.com";
    }


    // Auto Start permission
    private void manageAutoPermission() {
        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                .getInstance().isAutoStartPermissionAvailable(this);

        if (autoStartFeatureAvailable) {
            new AutoStartPermissionDialog().show(getSupportFragmentManager(), "AUTOSTART");
        }
    }

    static boolean permissionGiven;
    public static class AutoStartPermissionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_autostart_info)
                    .setPositiveButton("Grant", (dialog, id) -> {

                        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                                .getInstance().isAutoStartPermissionAvailable(getContext());

                        if (autoStartFeatureAvailable) {
                            permissionGiven = AutoStartPermissionHelper
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
            startBreakService();
        }
        else{
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

        JobInfo jobInfo
                = new JobInfo.Builder(7979, serviceComponent)
//                .setMinimumLatency(1 * 1000)
//                .setOverrideDeadline(3 * 1000)
//                TODO uncomment me
                .setPeriodic(config.breakTimeInMinutes.time * 60 * 1000)
//                .setPeriodic(15 * 60 * 1000)
//                .setMinimumLatency(5000)
                .setPersisted(true).build();
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
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
                    }
                    else{
                        config.EndTime = new Time(hourOfDay, minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void dayChangeChooser() {
        ((MaterialDayPicker) findViewById(R.id.day_picker))
                .setDayPressedListener((weekday, b) -> config.setWeekDays(weekday, b));
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
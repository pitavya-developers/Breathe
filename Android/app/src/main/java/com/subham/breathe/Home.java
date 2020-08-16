package com.subham.breathe;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.Calendar;
import java.util.Objects;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Home";
    static Config config;
    ConfigPersistanceStorage configPersistanceStorage;
    BreakService breakService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        init();
    }

    private void init() {
        breakService = new BreakService(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        configPersistanceStorage = new ConfigPersistanceStorage(this);

        InitializeConfigParameters();
        setUI();
        dayChangeChooser();
        setupBreakTimeSpinner();
        manageAutoPermission();

    }

    private boolean ifServiceRunning() {
        return true;
    }

    private void InitializeConfigParameters() {
        config = new Config();

        config.Id = configPersistanceStorage.getGId();
        config.Name = configPersistanceStorage.getGName();
        config.Email = configPersistanceStorage.getGEmail();
        config.activated = configPersistanceStorage.getActivated();

        if (config.activated) {
            if (!ifServiceRunning()) {
                breakService.startBreakService(configPersistanceStorage.getBreakTime().time);
            }
        } else {
            config.WorkDays = configPersistanceStorage.getWeekDays();
            config.StartTime = configPersistanceStorage.getStartTime();
            config.EndTime = configPersistanceStorage.getEndTime();
            config.breakTimeInMinutes = configPersistanceStorage.getBreakTime();
        }

    }

    private void setUI() {
        ((MaterialDayPicker) findViewById(R.id.day_picker)).setSelectedDays(config.WorkDays);
        ((TextView) findViewById(R.id.home_work_start_time)).setText(config.EndTime.toString());
        ((TextView) findViewById(R.id.home_work_start_time)).setText(config.StartTime.toString());
        ((Switch) findViewById(R.id.home_activate_switch)).setChecked(config.activated);
        ((TextView) findViewById(R.id.home_greeting_to)).setText(config.Name);

    }

    private void manageAutoPermission() {
        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                .getInstance().isAutoStartPermissionAvailable(this);

        if (autoStartFeatureAvailable) {
            new AutoStartPermissionDialog().show(getSupportFragmentManager(), "AUTOSTART");
        }
    }


    public void activateBreathe(View V) {

        if (((Switch) V).isChecked()) {
            config.activated = true;
            config.firstTimeActivated = true;
            configPersistanceStorage.update(config);
            breakService.startBreakService(configPersistanceStorage.getBreakTime().time);
        } else {
            config.activated = false;
            config.firstTimeActivated = false;
            configPersistanceStorage.update(config);
            breakService.stopBreakService();
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
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
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setMessage(R.string.dialog_autostart_info)
                    .setPositiveButton("Grant", (dialog, id) -> {

                        boolean autoStartFeatureAvailable = AutoStartPermissionHelper
                                .getInstance().isAutoStartPermissionAvailable(Objects.requireNonNull(getContext()));

                        if (autoStartFeatureAvailable) {
                            config.permissionGiven = AutoStartPermissionHelper
                                    .getInstance().getAutoStartPermission(getContext());

                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            return builder.create();
        }
    }
}
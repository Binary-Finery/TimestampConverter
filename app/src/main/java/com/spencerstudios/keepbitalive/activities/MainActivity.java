package com.spencerstudios.keepbitalive.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.spencerstudios.keepbitalive.R;
import com.spencerstudios.keepbitalive.fragments.DatePickerFragment;
import com.spencerstudios.keepbitalive.fragments.TimePickerFragment;
import com.spencerstudios.keepbitalive.models.Date;
import com.spencerstudios.keepbitalive.utilities.CalendarUtil;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText etInput;
    private TextView tvOutput;
    private FloatingActionButton fabSave;

    private static int[] date_args = new int[5];

    private static final String DATE_PICKER_TAG = "date_picker";
    private static final String TIME_PICKER_TAG = "time_picker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            long time_ms = Long.parseLong(etInput.getText().toString());
            tvOutput.setText(DateFormat.getDateInstance(DateFormat.FULL).format(time_ms).concat("\n").concat(DateFormat.getTimeInstance().format(time_ms)));
        } catch (Exception e) {
            tvOutput.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date_args[0] = year;
        date_args[1] = month;
        date_args[2] = dayOfMonth;
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        date_args[3] = hourOfDay;
        date_args[4] = minute;
        long time_millis = new CalendarUtil().setDate(date_args);
        etInput.setText(String.valueOf(time_millis));
        setCursor();
    }

    private void initViews() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        etInput = findViewById(R.id.et_millisecond_input);
        tvOutput = findViewById(R.id.tv_formatted_time);
        fabSave = findViewById(R.id.fab_save);
        etInput.addTextChangedListener(this);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispSaveDialog();
            }
        });

        etInput.setText(String.valueOf(System.currentTimeMillis()));
        setCursor();
    }

    public void openDateTimePicker(View v) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    public void clearInput(View v) {
        etInput.setText("");
    }

    private void setCursor() {
        int len = etInput.getText().toString().length();
        if (len > 0) {
            etInput.setSelection(len);
        }
    }

    private String formatContent(TextView tvOutput, EditText etInput) {
        return String.format("formatted date:\n%s\n\nlong timestamp:\n%s", tvOutput.getText().toString(), etInput.getText().toString());
    }

    private boolean validateHasContent(TextView tv, EditText et) {
        return tv.getText().toString().length() > 0 && et.getText().toString().length() > 0;
    }

    void dispSaveDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        @SuppressLint("InflateParams") View v = LayoutInflater.from(this).inflate(R.layout.save_date_dialog, null);
        final EditText etLabel = v.findViewById(R.id.et_label);
        Button btnCancel = v.findViewById(R.id.btn_cancel);
        Button btnSave = v.findViewById(R.id.btn_save);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = etLabel.getText().toString().trim();
                if (label.length() > 0) {
                    Date date;
                    String formatted = tvOutput.getText().toString();
                    String millis = etInput.getText().toString();

                    date = new Date(label, formatted.concat(" ").concat(millis));
                    //JetDB.putListOfObjects(getApplicationContext(), dateList, "dates");
                    dialog.dismiss();

                }
            }
        });

        dialog.setView(v);

        dialog.show();
    }

    public void fabClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fab_saved_dates:
                startActivity(new Intent(this, SavedDatesActivity.class));
                break;

            case R.id.fab_save:
                dispSaveDialog();
                break;

            default:
                if (validateHasContent(tvOutput, etInput)) {

                }
                break;
        }
    }
}
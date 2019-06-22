package com.spencerstudios.keepbitalive.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.spencerstudios.keepbitalive.R;
import com.spencerstudios.keepbitalive.fragments.DatePickerFragment;
import com.spencerstudios.keepbitalive.fragments.TimePickerFragment;
import com.spencerstudios.keepbitalive.utilities.CalendarUtil;
import com.spencerstudios.keepbitalive.utilities.TextUtils;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText etInput;
    private TextView tvOutput;

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
        etInput.addTextChangedListener(this);

        etInput.setText(String.valueOf(System.currentTimeMillis()));
        setCursor();
    }

    public void openDateTimePicker(View v) {
        hideKeyboard(v);
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    public void clearInput(View v) {
        etInput.setText("");
    }

    private void setCursor() {
        int len = etInput.getText().toString().length();
        if (len > 0)
            etInput.setSelection(len);
    }

    private String formatContent(TextView tvOutput, EditText etInput) {
        return String.format("formatted date:\n%s\n\nlong timestamp:\n%s", tvOutput.getText().toString(), etInput.getText().toString());
    }

    private boolean validateHasContent(TextView tv, EditText et) {
        return tv.getText().toString().length() > 0 && et.getText().toString().length() > 0;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_copy:
                if (validateHasContent(tvOutput, etInput))
                    new TextUtils(getApplicationContext(), formatContent(tvOutput, etInput)).copy();
                break;
            default:
                if (validateHasContent(tvOutput, etInput))
                    new TextUtils(getApplicationContext(), formatContent(tvOutput, etInput)).share();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

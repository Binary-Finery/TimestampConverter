package com.spencerstudios.keepbitalive.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.spencerstudios.keepbitalive.utilities.CalendarUtil;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int[] i = new CalendarUtil().getDate();
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), i[0], i[1], i[2]);
    }
}
